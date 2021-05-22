package org.strykeforce.tcr

import kotlinx.serialization.Serializable

@Serializable
data class Item(val itemId: Int, val measurementId: String)

@Serializable
data class SubscriptionRequest constructor(val type: String, val subscription: List<Item>) {
    constructor(subscription: List<Item>) : this("start", subscription)
}

@Serializable
data class SubscriptionResponse(val type: String, val timestamp: Long, val descriptions: List<String>)