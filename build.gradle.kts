import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20-RC"
    application
}

group = "net.bxx2004"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.openjdk.nashorn:nashorn-core:15.4")
    implementation("org.yaml:snakeyaml:2.2")
    //implementation("com.github.a-langer:jsr223-commonjs-modules:1.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}