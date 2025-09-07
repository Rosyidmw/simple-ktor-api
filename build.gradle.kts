plugins {
    kotlin("jvm") version "2.2.0"
    id("io.ktor.plugin") version "2.3.12"
    application
    kotlin("plugin.serialization") version "2.2.0"
}

group = "org.study.rotibow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}


dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.12")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.12")
    implementation("ch.qos.logback:logback-classic:1.4.14") // buat logging

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.0.20")
    // Routing dan content negotiation
    implementation("io.ktor:ktor-server-content-negotiation:2.3.12")

    // Serialization JSON
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}