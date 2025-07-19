plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.argon2.jvm)
    implementation(libs.ktor.server.auth.jwt)
}