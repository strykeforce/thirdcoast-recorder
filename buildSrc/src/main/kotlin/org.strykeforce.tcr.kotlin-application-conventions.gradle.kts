plugins {
    id("org.strykeforce.tcr.kotlin-common-conventions")

    application
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.11")
}