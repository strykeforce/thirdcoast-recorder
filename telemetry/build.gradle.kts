plugins {
    id("org.strykeforce.tcr.kotlin-library-conventions")
    kotlin("plugin.serialization") version "1.5.0"
}

var ktorClientVersion = "1.5.4"
var wpiVersion = "2021.3.1"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

    implementation("io.ktor:ktor-client-core:$ktorClientVersion")
    implementation("io.ktor:ktor-client-cio:$ktorClientVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorClientVersion")
    implementation("io.ktor:ktor-network:$ktorClientVersion")

    implementation("edu.wpi.first.ntcore:ntcore-java:$wpiVersion")
    implementation("edu.wpi.first.ntcore:ntcore-jni:$wpiVersion:osxx86-64")
    implementation("edu.wpi.first.wpiutil:wpiutil-java:$wpiVersion")
}
