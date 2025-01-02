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

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

tasks {
    create("stage").dependsOn("installDist")

    jar {
        manifest {
            attributes["Main-Class"] = "io.ktor.server.netty.EngineMain"
        }
        // Set the archive base name directly as a string
        archiveBaseName.set("example")

        // Include all dependencies
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map { zipTree(it) }
        })
    }
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