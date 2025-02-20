package com.luthfi.ecommerce.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyFormatTest {

    @Test
    fun `test format`() {
        val actualData = format(10_000_000)
        val expectedData = "Rp10.000.000"
        assertEquals(expectedData, actualData)
    }
}
