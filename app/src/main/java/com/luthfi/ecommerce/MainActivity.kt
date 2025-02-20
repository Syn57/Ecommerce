package com.luthfi.ecommerce

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.luthfi.ecommerce.core.data.datasource.api.responses.TransactionResponse
import com.luthfi.ecommerce.databinding.ActivityMainBinding
import com.luthfi.ecommerce.ui.main.MainFragmentDirections
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val remoteConfig: FirebaseRemoteConfig by inject()

    private val fcm: FirebaseMessaging by inject()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val permissionState =
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
        fcm.subscribeToTopic(TOPIC_KEY_FIREBASE)
    }

    fun moveToDetail(data: String, variant: String) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navToDetail = MainFragmentDirections.actionMainFragmentToDetailFragment(data)
        navToDetail.variantName = variant
        val navController = navHostFragment.navController
        navController.navigate(navToDetail)
    }

    fun moveToStatus(data: TransactionResponse.Data) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navToStatus = MainFragmentDirections.actionMainFragmentToStatusFragment(
            data.invoiceId!!,
            data.status!!,
            data.date!!,
            data.time!!,
            data.payment!!,
            data.total!!,
            data.review,
            data.rating ?: 0,
            FLAG_TRANSACTION
        )
        val navController = navHostFragment.navController
        navController.navigate(navToStatus)
    }

    companion object {
        const val TOPIC_KEY_FIREBASE = "promo"
        const val FLAG_TRANSACTION = "transaction"
    }
}
