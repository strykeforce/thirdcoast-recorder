package org.strykeforce.tcr

import kotlinx.serialization.Serializable

@Serializable
data class Telemetry(var timestamp: Long, val data: List<Double>) {
    fun adjustTimestamp(offset: Long) {
        timestamp -= offset
    }

    fun toCsv() = "$timestamp,${data.joinToString(separator = ",")}"
}