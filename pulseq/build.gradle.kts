extra["discordWebhooksVersion"] = "0.7.2"
extra["commonsIOVersion"] = "2.11.0"
extra["caffeineVersion"] = "3.0.4"

version = project.version

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(project(":pulseq-core-starter"))
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("club.minnced:discord-webhooks:${property("discordWebhooksVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("commons-io:commons-io:${property("commonsIOVersion")}")
    implementation("com.github.ben-manes.caffeine:caffeine:${property("caffeineVersion")}")
}