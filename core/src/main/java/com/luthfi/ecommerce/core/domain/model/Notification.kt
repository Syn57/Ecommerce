package com.luthfi.ecommerce.core.domain.model

import com.luthfi.ecommerce.core.data.datasource.room.entity.NotificationEntity

data class Notification(
    val id: Int,
    val body: String,
    val date: String,
    val image: String,
    val time: String,
    val title: String,
    val type: String,
    var isChecked: Boolean = false
)

fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = this.id,
        body = this.body,
        date = this.date,
        image = this.image,
        time = this.time,
        title = this.title,
        type = this.type,
        isChecked = this.isChecked
    )
}
