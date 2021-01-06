plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    `maven-publish`
}

group = "ru.mejiomah17"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.rybalkinsd:kohttp:0+")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1+")
}

val githubToken: String by project
val githubUser: String by project

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/MEJIOMAH17/mosenergo-api")
            credentials {
                username = githubUser
                password = githubToken
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}