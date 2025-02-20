package com.luthfi.ecommerce.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun format(number: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale(IND_LANGUAGE, IND_COUNTRY))
    format.maximumFractionDigits = 0
    return format.format(number)
}

fun formatBottomSheet(original: String): String {
    var originalString = original
    if (originalString.contains(",")) {
        originalString = originalString.replace(",".toRegex(), "")
    }
    val longval: Long = originalString.toLong()
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    formatter.applyPattern("#,###,###,###")
    return formatter.format(longval)
}

const val IND_LANGUAGE = "ID"
const val IND_COUNTRY = "ID"
