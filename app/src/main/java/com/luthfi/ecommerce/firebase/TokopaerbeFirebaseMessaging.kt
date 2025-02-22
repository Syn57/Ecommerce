package com.luthfi.ecommerce.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.luthfi.ecommerce.MainActivity
import com.luthfi.ecommerce.R
import com.ecommerce.uiassets.R as RUI
import com.luthfi.ecommerce.core.data.datasource.firebase.data.PromoFcm
import com.luthfi.ecommerce.core.domain.model.Notification
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TokopaerbeFirebaseMessaging : FirebaseMessagingService() {

    private val repository: Repository by inject()
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val gson = Gson()
            val promoString = gson.toJson(remoteMessage.data)
            val promo = gson.fromJson(promoString, PromoFcm::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                repository.insertNotification(promo)
                val lastData = repository.getAllNotification().first()
                sendNotification(lastData[lastData.lastIndex])
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun sendNotification(data: Notification) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(RUI.drawable.ic_notification)
            .setContentTitle(data.title)
            .setContentText(data.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(createContentIntent())
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notificationID = data.id
        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    private fun createContentIntent(): PendingIntent {
        return NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.app_nav)
            .setDestination(R.id.notificationFragment)
            .createPendingIntent()
    }

    companion object {
        private const val CHANNEL_ID = "ECOMMERCE"
        private const val CHANNEL_NAME = "NOTIFICATION"
    }
}
