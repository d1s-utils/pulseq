version = project.version

extra["jnativeHookVersion"] = "2.2-SNAPSHOT"
extra["ktorVersion"] = "1.6.5"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":pulseq-core-starter"))
    implementation("com.github.kwhat:jnativehook:${property("jnativeHookVersion")}")
    implementation("io.ktor:ktor-client-core:${property("ktorVersion")}")
    implementation("io.ktor:ktor-client-cio:${property("ktorVersion")}")
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
    this.imageName = "ghcr.io/d1snin/pulseq"
}