package com.luthfi.ecommerce.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.luthfi.ecommerce.core.BuildConfig
import com.luthfi.ecommerce.core.data.RepositoryImpl
import com.luthfi.ecommerce.core.data.datasource.api.ApiService
import com.luthfi.ecommerce.core.data.datasource.api.AuthorizationInterceptor
import com.luthfi.ecommerce.core.data.datasource.api.TokenAuthenticator
import com.luthfi.ecommerce.core.data.datasource.firebase.FirebaseDatasource
import com.luthfi.ecommerce.core.data.datasource.preferences.UserPreferences
import com.luthfi.ecommerce.core.data.datasource.room.ProductDatabase
import com.luthfi.ecommerce.core.domain.repository.Repository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ECOMMERCE_PREF")

val dataStoreModule = module {
    single { androidContext().dataStore }
    single { UserPreferences(get()) }
}

val repositoryModule = module {
    single<Repository> {
        RepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}

val databaseModule = module {
    factory { get<ProductDatabase>().chartDao() }
    factory { get<ProductDatabase>().favoriteDao() }
    factory { get<ProductDatabase>().notificationDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            ProductDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}

val networkModule = module {
    single { AuthorizationInterceptor(get()) }
    single { TokenAuthenticator(get(), get(), get()) }
    single {
        val chuckerCollector = ChuckerCollector(
            context = androidContext(),
            showNotification = true
        )
        ChuckerInterceptor.Builder(androidContext())
            .collector(chuckerCollector)
            .alwaysReadResponseBody(true)
            .createShortcut(true)
            .build()
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(get<ChuckerInterceptor>())
            .addInterceptor(get<AuthorizationInterceptor>())
            .connectTimeout(7, TimeUnit.SECONDS)
            .readTimeout(7, TimeUnit.SECONDS)
            .authenticator(get<TokenAuthenticator>())
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val firebaseCoreModule = module {
    single { Firebase.remoteConfig }
    single { FirebaseMessaging.getInstance() }
    single { FirebaseDatasource() }
}

const val DATABASE_NAME = "product_db"
