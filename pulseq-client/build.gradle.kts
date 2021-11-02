version = project.version

extra["jnativeHookVersion"] = "2.2-SNAPSHOT"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation("com.github.kwhat:jnativehook:${property("jnativeHookVersion")}")
}