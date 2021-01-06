package ru.mejiomah17.mosenergo

import io.github.rybalkinsd.kohttp.dsl.context.HttpPostContext
import io.github.rybalkinsd.kohttp.dsl.httpPost
import io.github.rybalkinsd.kohttp.ext.url
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.time.YearMonth

class Mosenergo(private val session: String, private val client: OkHttpClient) {
    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }

        fun create(login: String, password: String, client: OkHttpClient = OkHttpClient()): Mosenergo {
            val rs = httpPost(client) {
                url("https://my.mosenergosbyt.ru/gate_lkcomu?action=auth&query=login")
                body {
                    form {
                        "login" to login
                        "psw" to password
                    }
                }
            }.body()!!.string()
            val root = json.decodeFromString<Root<SessionData>>(rs)
            return Mosenergo(root.data.first().session, client)
        }
    }

    fun getList(): List<ListData> {
        return post<Root<ListData>> {
            url("https://my.mosenergosbyt.ru/gate_lkcomu?action=sql&query=LSList&session=$session")
        }.data
    }


    /**
     * returns pdf byte array
     */
    fun getBilling(
        yearMonth: YearMonth,
        /**
         * From [getList]
         */
        vlProvider: String
    ): ByteArray {
        val data = post<Root<BytProxyData>> {
            url("https://my.mosenergosbyt.ru/gate_lkcomu?action=sql&query=bytProxy&session=$session")
            body {
                form {
                    "dt_period" to "${yearMonth.year}-${yearMonth.month.value}-01 00:00:00"
                    "kd_provider" to "1"
                    "vl_provider" to vlProvider
                    "plugin" to "bytProxy"
                    "proxyquery" to "GetPrintBillLink"
                }
            }

        }.data.first()

        return httpPost(client) {
            url(data.nm_link)
            body {
                form {
                    "params" to data.params.params
                    "anstype" to data.params.anstype
                }
            }
        }.body()!!.byteStream().readAllBytes()
    }

    private inline fun <reified T> post(noinline init: HttpPostContext.() -> Unit): T {
        return json.decodeFromString(httpPost(client, init).body()!!.string())
    }


    private val BytProxyData.params: Params get() = json.decodeFromString(this.vl_params)

    @Serializable
    private data class Params(val params: String, val anstype: String)
}
