plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "protopie"

include("subproject:boot")
include("subproject:presentation")
include("subproject:domain")
include("subproject:infrastructure")
include("subproject:application")
