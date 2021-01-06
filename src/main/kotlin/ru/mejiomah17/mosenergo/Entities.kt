package ru.mejiomah17.mosenergo

import kotlinx.serialization.Serializable

@Serializable
internal data class Root<T : Data>(val data: List<T>)

internal interface Data

@Serializable
internal data class SessionData(val session: String) : Data

@Serializable
internal data class BytProxyData(val nm_link: String, val vl_params: String) : Data

@Serializable
data class ListData(
    /**
     * Номер лицевого счета
     */
    val nn_ls: String,
    /**
     * Адрес
     */
    val nm_ls_group_full: String,
    val vl_provider: String
) : Data

