plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(project(":subproject:presentation"))
    implementation(project(":subproject:application"))
    implementation(project(":subproject:domain"))
    implementation(project(":subproject:infrastructure"))

    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)

    implementation(libs.logback.classic)

    testImplementation(project(":subproject:presentation"))
    testImplementation(project(":subproject:application"))
    testImplementation(project(":subproject:domain"))
    testImplementation(project(":subproject:infrastructure"))

    testImplementation(libs.bundles.exposed)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.ktor.client.content.negotiation)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.bundles.testcontainers)
}

// TestResources 관련 작업을할떄, 작업 등록
tasks.processTestResources {
    dependsOn("copyInitScript")
}

tasks.register<Copy>("copyInitScript") {
    // root Path에 있는 init-script가 classpath에 포함되도록
    from("${rootProject.projectDir}/database/create-tables.sql")
    into("${project.buildDir}/resources/test")
}