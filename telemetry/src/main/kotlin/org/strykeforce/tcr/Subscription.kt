package org.strykeforce.tcr

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Item(val itemId: Int, val measurementId: String)

@Serializable
data class SubscriptionRequest constructor(val type: String, val subscription: List<Item>) {
    constructor(subscription: List<Item>) : this("start", subscription)
}

@Serializable
data class SubscriptionResponse(val type: String, val timestamp: Long, val descriptions: List<String>)

fun readSubscriptionFromFile(pathname: String) = Json.decodeFromString<SubscriptionRequest>(File(pathname).readText())

fun SubscriptionRequest.writeToFile(pathname: String) =
    File(pathname).writeText(Json { prettyPrint = true }.encodeToString(this))