plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":subproject:domain"))
    implementation(libs.config)
    implementation(libs.bundles.exposed)
    implementation(libs.hikaricp)
    implementation(libs.postgresql)

}