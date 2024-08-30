plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "net.bxx2004"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjdk.nashorn:nashorn-core:15.4")
}

tasks.test {
    useJUnitPlatform()
}
