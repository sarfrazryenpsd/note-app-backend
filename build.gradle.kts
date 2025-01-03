import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    application
}

group = "com.example"
version = "0.0.1"

repositories {
    mavenCentral()
}

// In build.gradle.kts

// Add these configurations at the top level
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

// Update the application block
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    applicationDefaultJvmArgs = listOf(
        "-Xmx256m", // Maximum heap size
        "-Xms128m", // Initial heap size
        "-XX:+UseG1GC", // Use G1 Garbage Collector
        "-XX:MaxMetaspaceSize=128m",
        "-XX:+UseStringDeduplication",
        "-Dio.ktor.development=false" // Set to false for production
    )
}

// Add this configuration
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11) // Match your JDK version
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

// Optional: Configure the Java process for the build itself
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs = listOf("-Xlint:deprecation")
}



dependencies {
    implementation(libs.ktor.sessons)
    implementation(libs.ktor.resources)
    implementation(libs.ktor.server.resources)
    implementation(libs.authentication)
    implementation(libs.jwt)
    implementation(libs.content.negotiation)
    implementation(libs.kotlinx.serialization)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.hikari)
    implementation(libs.postgres)
    implementation(libs.gson)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}