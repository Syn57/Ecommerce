package com.luthfi.ecommerce.core.data.datasource.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.luthfi.ecommerce.core.R
import com.luthfi.ecommerce.core.data.datasource.api.responses.PaymentResponse
import com.luthfi.ecommerce.core.data.datasource.firebase.data.PromoFcm
import com.luthfi.ecommerce.core.data.datasource.firebase.data.toEntity
import com.luthfi.ecommerce.core.data.datasource.room.dao.NotificationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FirebaseDatasource : FirebaseMessagingService() {

    private val remoteConfig: FirebaseRemoteConfig by inject()

    private val notificationDao: NotificationDao by inject()

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val gson = Gson()
            val promoString = gson.toJson(remoteMessage.data)
            val promo = gson.fromJson(promoString, PromoFcm::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                notificationDao.insertNotification(promo.toEntity())
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    fun getPaymentConfig(): List<PaymentResponse.Data> {
        // Firebase remote config
        val payments = remoteConfig.getString(FIREBASE_KEY_PAYMENT)
        return Gson().fromJson(payments, PaymentResponse::class.java).data
    }

    companion object {
        private const val FIREBASE_KEY_PAYMENT = "payment"
    }
}
