package com.luthfi.ecommerce.core.data.datasource.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luthfi.ecommerce.core.domain.model.Notification

@Entity(tableName = "notification")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val body: String,
    val date: String,
    val image: String,
    val time: String,
    val type: String,
    var isChecked: Boolean = false
)

fun NotificationEntity.toNotification(): Notification {
    return Notification(
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

fun List<NotificationEntity>.toNotification(): List<Notification> {
    val listNotification = mutableListOf<Notification>()
    this.forEach {
        listNotification.add(
            Notification(
                id = it.id,
                body = it.body,
                date = it.date,
                image = it.image,
                time = it.time,
                title = it.title,
                type = it.type,
                isChecked = it.isChecked
            )
        )
    }
    return listNotification
}
