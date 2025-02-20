package com.luthfi.ecommerce.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.luthfi.ecommerce.core.data.RepositoryImpl
import com.luthfi.ecommerce.core.data.datasource.firebase.FirebaseDatasource
import com.luthfi.ecommerce.core.data.datasource.preferences.UserPreferences
import com.luthfi.ecommerce.core.data.datasource.room.ProductDatabase
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.main.MainFragmentViewModel
import com.luthfi.ecommerce.ui.main.chart.ChartViewModel
import com.luthfi.ecommerce.ui.main.checkout.CheckoutViewModel
import com.luthfi.ecommerce.ui.main.detail.DetailViewModel
import com.luthfi.ecommerce.ui.main.home.HomeViewModel
import com.luthfi.ecommerce.ui.main.notification.NotificationViewModel
import com.luthfi.ecommerce.ui.main.profile.ProfileViewModel
import com.luthfi.ecommerce.ui.main.review.ReviewViewModel
import com.luthfi.ecommerce.ui.main.status.StatusViewModel
import com.luthfi.ecommerce.ui.main.store.SearchDialogViewModel
import com.luthfi.ecommerce.ui.main.store.StoreViewModel
import com.luthfi.ecommerce.ui.main.transaction.TransactionViewModel
import com.luthfi.ecommerce.ui.main.wishlist.WishlistViewModel
import com.luthfi.ecommerce.ui.prelogin.login.LoginViewModel
import com.luthfi.ecommerce.ui.prelogin.onboarding.OnboardingViewModel
import com.luthfi.ecommerce.ui.prelogin.register.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ECOMMERCE_PREF")

val preferenceModule = module {
    single { androidContext().dataStore }
    single { UserPreferences(get()) }
}

val repoModule = module {
    factory<Repository> { RepositoryImpl(get(), get(), get(), get(), get()) }
}

val dataModule = module {
    factory { get<ProductDatabase>().chartDao() }
    factory { get<ProductDatabase>().notificationDao() }
    factory { get<ProductDatabase>().favoriteDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            ProductDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}

val viewModelModule = module {
    viewModel { MainFragmentViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SearchDialogViewModel(get()) }
    viewModel { StoreViewModel(get()) }
    viewModel { DetailViewModel(get(), get()) }
    viewModel { ReviewViewModel(get()) }
    viewModel { WishlistViewModel(get()) }
    viewModel { ChartViewModel(get()) }
    viewModel { CheckoutViewModel(get()) }
    viewModel { StatusViewModel(get()) }
    viewModel { TransactionViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
}

val firebaseModule = module {
    single { Firebase.remoteConfig }
    single { FirebaseMessaging.getInstance() }
    single { FirebaseDatasource() }
    single { Firebase.analytics }
}

const val DATABASE_NAME = "product_db"
