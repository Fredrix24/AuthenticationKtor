plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    id("io.ktor.plugin") version "3.4.1"
    application
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:3.4.1")
    implementation("io.ktor:ktor-server-netty:3.4.1")
    implementation("io.ktor:ktor-server-content-negotiation:3.4.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.1")
    implementation("io.ktor:ktor-server-auth:3.4.1")
    implementation("io.ktor:ktor-server-auth-jwt:3.4.1")
    implementation("io.ktor:ktor-server-status-pages:3.4.1")
    implementation("io.ktor:ktor-server-call-logging:3.4.1")
    implementation("io.ktor:ktor-server-cors:3.4.1")
    implementation("io.ktor:ktor-server-swagger:3.4.1")
    implementation("io.ktor:ktor-server-openapi:3.4.1")
    implementation("io.ktor:ktor-server-openapi-jvm:3.4.1")
    implementation("io.ktor:ktor-server-routing-openapi:3.4.1")
    testImplementation("io.ktor:ktor-server-test-host:3.4.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.3.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

configurations.all {
    resolutionStrategy {
        force("io.ktor:ktor-server-openapi:3.4.1")
        force("io.ktor:ktor-server-routing-openapi:3.4.1")
    }
}

ktor {
    openApi {
        enabled = true
    }
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1
    systemProperty("junit.jupiter.execution.parallel.enabled", "false")
    systemProperty("junit.jupiter.execution.parallel.mode.default", "same_thread")
    systemProperty("junit.jupiter.execution.parallel.mode.classes.default", "same_thread")
    systemProperty("ktor.test.throwOnException", "false")
}

application {
    mainClass.set("com.example.ApplicationKt")
}

kotlin {
    jvmToolchain(21)
}