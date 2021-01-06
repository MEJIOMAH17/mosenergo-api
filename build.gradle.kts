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
