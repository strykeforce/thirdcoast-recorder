plugins {
    id("org.strykeforce.tcr.kotlin-common-conventions")

    `java-library`
}

dependencies {
    testRuntimeOnly("org.slf4j:slf4j-nop:1.7.30")
}