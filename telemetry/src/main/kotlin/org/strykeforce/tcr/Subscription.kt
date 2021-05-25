package org.strykeforce.tcr

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

suspend fun readSubscriptionFromFile(file: File) = withContext(Dispatchers.IO) {
    Json.decodeFromString<SubscriptionRequest>(file.readText())
}

fun SubscriptionRequest.writeToFile(file: File) = file.writeText(Json { prettyPrint = true }.encodeToString(this))
fun SubscriptionRequest.asJson() = Json { prettyPrint = true }.encodeToString(this)