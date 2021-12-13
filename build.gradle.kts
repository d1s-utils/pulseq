plugins {
    id("org.jetbrains.dokka") version "1.5.30"
    id("org.springframework.boot") version "2.5.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
}

extra["dokkaVersion"] = "1.5.30"
extra["springMockkVersion"] = "3.0.1"
extra["discordWebhooksVersion"] = "0.7.2"
extra["commonsIOVersion"] = "2.11.0"
extra["caffeineVersion"] = "3.0.4"
extra["springdocVersion"] = "1.5.13"

group = "uno.d1s"
version = "0.2.1-alpha.0"

repositories {
    maven {
        url = uri("https://repo.spring.io/release")
    }
    mavenCentral()
}

dependencies {
    val dokkaHtmlPlugin by configurations
    val implementation by configurations
    val annotationProcessor by configurations
    val testImplementation by configurations

    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:${property("dokkaVersion")}")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:${property("springMockkVersion")}")
    implementation("org.springdoc:springdoc-openapi-ui:${property("springdocVersion")}")
    implementation("org.springdoc:springdoc-openapi-kotlin:${property("springdocVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-security")
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

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_11.majorVersion
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events.addAll(
            listOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
            )
        )
    }
}