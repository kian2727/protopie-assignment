plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":subproject:domain"))


}