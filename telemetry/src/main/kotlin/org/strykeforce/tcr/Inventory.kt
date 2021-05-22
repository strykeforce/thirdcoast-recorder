package org.strykeforce.tcr

import kotlinx.serialization.Serializable

@Serializable
data class Measurable(val id: Int, val type: String, val description: String)

@Serializable
data class Measure(val id: String, val description: String)

@Serializable
data class MeasurableMeasures(val deviceType: String, val deviceMeasures: List<Measure>)

@Serializable
data class Inventory(val items: List<Measurable>, val measures: List<MeasurableMeasures>) {

    fun measuresFor(measurable: Measurable) = measures.find { it.deviceType == measurable.type }?.deviceMeasures

    fun toSubscription(): SubscriptionRequest {
        val subscriptionItems = mutableListOf<Item>()
        for (measurable in items)
            for (measure in measuresFor(measurable)!!)
                subscriptionItems += Item(measurable.id, measure.id)
        return SubscriptionRequest(subscriptionItems)
    }
}