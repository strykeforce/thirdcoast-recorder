plugins {
    id("org.strykeforce.tcr.kotlin-library-conventions")
    kotlin("plugin.serialization") version "1.5.0"
}

val ktorClientVersion = "1.6.8"
val wpiVersion = "2022.4.1"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("io.ktor:ktor-client-core:$ktorClientVersion")
    implementation("io.ktor:ktor-client-cio:$ktorClientVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorClientVersion")
    implementation("io.ktor:ktor-network:$ktorClientVersion")

    implementation("edu.wpi.first.ntcore:ntcore-java:$wpiVersion")
    implementation("edu.wpi.first.ntcore:ntcore-jni:$wpiVersion:osxx86-64")
    implementation("edu.wpi.first.wpiutil:wpiutil-java:$wpiVersion")
}
