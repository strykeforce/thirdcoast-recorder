plugins {
    id("org.strykeforce.tcr.kotlin-application-conventions")
}

dependencies {
    implementation(project(":telemetry"))
}

application {
    mainClass.set("org.strykeforce.tcr.app.AppKt")
    applicationName = "tcr"
}