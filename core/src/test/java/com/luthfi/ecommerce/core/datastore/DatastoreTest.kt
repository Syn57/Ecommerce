package com.luthfi.ecommerce.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.luthfi.ecommerce.core.data.datasource.preferences.UserPreferences
import com.luthfi.ecommerce.core.datastore.dummy.DummyPreferences.accessToken
import com.luthfi.ecommerce.core.datastore.dummy.DummyPreferences.locale
import com.luthfi.ecommerce.core.datastore.dummy.DummyPreferences.refreshToken
import com.luthfi.ecommerce.core.datastore.dummy.DummyPreferences.username
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DatastoreTest {

    private var userPreferences: UserPreferences? = null

    @Before
    fun initiation() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dataStore = PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("ECOMMERCE_PREF")
            }
        )
        userPreferences = UserPreferences(dataStore)
    }

    @After
    fun termination() {
        userPreferences = null
    }

    @Test
    fun `test get and set refresh token`() = runTest {
        userPreferences?.setRefreshToken(refreshToken)
        val actualData = userPreferences?.getRefreshToken()?.first()
        assertEquals(refreshToken, actualData)
    }

    @Test
    fun `test get and set locale`() = runTest {
        userPreferences?.setLocale(locale)
        val actualData = userPreferences?.getLocale()?.first()
        assertEquals(locale, actualData)
    }

    @Test
    fun `test get and set access token`() = runTest {
        userPreferences?.setAccessToken(accessToken)
        val actualData = userPreferences?.getAccessToken()?.first()
        assertEquals(accessToken, actualData)
    }

    @Test
    fun `test get and set is onboard`() = runTest {
        userPreferences?.setIsOnBoard(true)
        val actualData = userPreferences?.getIsOnboard()?.first()
        assertEquals(true, actualData)
    }

    @Test
    fun `test get and set is light`() = runTest {
        userPreferences?.setIsLight(true)
        val actualData = userPreferences?.getIsLight()?.first()
        assertEquals(true, actualData)
    }

    @Test
    fun `test get and set username`() = runTest {
        userPreferences?.setUsername(username)
        val actualData = userPreferences?.getUsername()?.first()
        assertEquals(username, actualData)
    }

    @Test
    fun `test get and set is login`() = runTest {
        userPreferences?.setIsLogin(true)
        val actualData = userPreferences?.getIsLogin()?.first()
        assertEquals(true, actualData)
    }

    @Test
    fun `test logout`() = runTest {
        userPreferences?.logout()
        val actualData = userPreferences?.getIsLogin()?.first()
        assertEquals(false, actualData)
    }
}
