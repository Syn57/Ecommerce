package com.luthfi.ecommerce.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.ecommerce.uiassets.R

fun getSpan(context: Context, sentence: String, spanString: Array<String>): SpannableStringBuilder {
    val spannable = SpannableStringBuilder(sentence)
    spanString.forEach {
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.p_40, context.theme)),
            sentence.indexOf(it),
            sentence.indexOf(it) + it.length,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }
    return spannable
}
