package org.strykeforce.tcr

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SampleTest {
    private val testSample = Sample()

    @Test
    internal fun testSum() {
        val expected = 42
        assertEquals(expected, testSample.sum(40, 2))
    }
}