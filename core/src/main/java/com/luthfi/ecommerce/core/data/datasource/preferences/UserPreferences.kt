package com.luthfi.ecommerce.core.data.datasource.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    private val refreshToken = stringPreferencesKey("refresh_token")
    private val locale = stringPreferencesKey("locale")
    private val accessToken = stringPreferencesKey("access_token")
    private val isOnboard = booleanPreferencesKey("is_onboard")
    private val isLight = booleanPreferencesKey("is_light_theme")
    private val username = stringPreferencesKey("username")
    private val isLogin = booleanPreferencesKey("is_login")

    fun getRefreshToken(): Flow<String> {
        return dataStore.data.map {
            it[refreshToken] ?: ""
        }
    }

    suspend fun setRefreshToken(value: String) {
        dataStore.edit {
            it[refreshToken] = value
        }
    }

    fun getLocale(): Flow<String> {
        return dataStore.data.map {
            it[locale] ?: "en"
        }
    }

//    fun getLocaleStatic(): String {
//        return dataStore.
//    }

    suspend fun setLocale(value: String) {
        dataStore.edit {
            it[locale] = value
        }
    }

    fun getAccessToken(): Flow<String> {
        return dataStore.data.map {
            it[accessToken] ?: ""
        }
    }

    suspend fun setAccessToken(value: String) {
        dataStore.edit {
            it[accessToken] = value
        }
    }

    fun getIsOnboard(): Flow<Boolean> {
        return dataStore.data.map {
            it[isOnboard] ?: false
        }
    }

    suspend fun setIsOnBoard(value: Boolean) {
        dataStore.edit {
            it[isOnboard] = value
        }
    }

    fun getIsLight(): Flow<Boolean> {
        return dataStore.data.map {
            it[isLight] ?: true
        }
    }

    suspend fun setIsLight(value: Boolean) {
        dataStore.edit {
            it[isLight] = value
        }
    }

    fun getUsername(): Flow<String> {
        return dataStore.data.map {
            it[username] ?: ""
        }
    }

    suspend fun setUsername(value: String) {
        dataStore.edit {
            it[username] = value
        }
    }

    fun getIsLogin(): Flow<Boolean> {
        return dataStore.data.map {
            it[isLogin] ?: false
        }
    }

    suspend fun setIsLogin(value: Boolean) {
        dataStore.edit {
            it[isLogin] = value
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it.remove(username)
            it.remove(accessToken)
            it.remove(refreshToken)
            it[isLogin] = false
        }
    }
}
