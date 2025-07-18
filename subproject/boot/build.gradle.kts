plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.exposed)

    implementation(libs.hikaricp)
    implementation(libs.postgresql)

    implementation(libs.logback.classic)


    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.bundles.testcontainers)
}