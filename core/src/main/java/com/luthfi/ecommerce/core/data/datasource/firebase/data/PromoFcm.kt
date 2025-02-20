package com.luthfi.ecommerce.core.data.datasource.firebase.data

import com.luthfi.ecommerce.core.data.datasource.room.entity.NotificationEntity

data class PromoFcm(
    val body: String,
    val date: String,
    val image: String,
    val time: String,
    val title: String,
    val type: String
)

fun PromoFcm.toEntity(): NotificationEntity {
    return NotificationEntity(
        title = this.title,
        body = this.body,
        date = this.date,
        image = this.image,
        time = this.time,
        type = this.type,
        isChecked = false
    )
}
