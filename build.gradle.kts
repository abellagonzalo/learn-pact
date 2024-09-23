plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:latest.release")
    implementation("io.ktor:ktor-server-core:latest.release")
    implementation("io.ktor:ktor-server-netty:latest.release")
    implementation("io.ktor:ktor-server-content-negotiation:latest.release")
    implementation("io.ktor:ktor-serialization-kotlinx-json:latest.release")


    testImplementation("au.com.dius.pact.consumer:junit5:latest.release")
    testImplementation("au.com.dius.pact.provider:junit5:latest.release")
    testImplementation("org.hamcrest:hamcrest:latest.release")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.+")
    testImplementation(kotlin("test"))

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.+")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(19)
}