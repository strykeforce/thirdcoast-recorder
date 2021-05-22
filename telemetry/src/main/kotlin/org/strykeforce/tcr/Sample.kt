package org.strykeforce.tcr

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Serializable
class Sample {
    fun sum(a: Int, b: Int): Int {
        logger.debug { "sum is ${a + b}" }
        return a + b
    }
}

@Serializable
data class Project(val name: String, val language: String)

fun Project.toJson()  = Json.encodeToString(this)