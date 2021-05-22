package org.strykeforce.tcr

import kotlinx.serialization.Serializable

@Serializable
data class Measurable(val id: Int, val type: String, val description: String)

@Serializable
data class Measure(val id: String, val description: String)

@Serializable
data class MeasurableMeasures(val deviceType: String, val deviceMeasures: List<Measure>)

@Serializable
data class Inventory(val items: List<Measurable>, val measures: List<MeasurableMeasures>)