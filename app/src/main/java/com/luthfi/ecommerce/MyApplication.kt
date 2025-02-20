package com.luthfi.ecommerce

import android.app.Application
import com.luthfi.ecommerce.core.di.dataStoreModule
import com.luthfi.ecommerce.core.di.databaseModule
import com.luthfi.ecommerce.core.di.firebaseCoreModule
import com.luthfi.ecommerce.core.di.networkModule
import com.luthfi.ecommerce.core.di.repositoryModule
import com.luthfi.ecommerce.di.dataModule
import com.luthfi.ecommerce.di.firebaseModule
import com.luthfi.ecommerce.di.preferenceModule
import com.luthfi.ecommerce.di.repoModule
import com.luthfi.ecommerce.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    dataStoreModule,
                    repositoryModule,
                    viewModelModule,
                    repoModule,
                    networkModule,
                    databaseModule,
                    dataModule,
                    firebaseModule,
                    firebaseCoreModule,
                    preferenceModule
                )
            )
        }
    }
}
