plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":subproject:domain"))
    implementation(project(":subproject:application"))
    implementation(libs.bundles.ktor)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.bundles.testcontainers)

}