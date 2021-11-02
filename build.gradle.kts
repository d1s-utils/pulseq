plugins {
    id("org.jetbrains.dokka") version "1.5.30" apply false
    id("org.springframework.boot") version "2.5.6" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("jvm") version "1.5.31" apply false
    kotlin("plugin.spring") version "1.5.31" apply false
}

subprojects {
    apply {
        plugin("org.jetbrains.dokka")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }

    extra["dokkaVersion"] = "1.5.30"

    group = "uno.d1s"
    version = "0.0.1-pre-alpha.1"

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
    }

    tasks.withType<JavaCompile>() {
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
    }

    tasks.register("wrapper") {}
    tasks.register("prepareKotlinBuildScriptModel") {}
}
