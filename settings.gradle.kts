pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/release") }
        gradlePluginPortal()
    }
}
rootProject.name = "pulseq"
include(
    "pulseq",
    "pulseq-client",
    "pulseq-core-starter"
)