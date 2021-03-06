import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val junitVersion = "5.4.2"

plugins {
    kotlin("jvm") version "1.3.61"
}

group = "com.oxyggen.net"
version = "1.0.11"


repositories {
    mavenCentral()
    jcenter()
 }

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

