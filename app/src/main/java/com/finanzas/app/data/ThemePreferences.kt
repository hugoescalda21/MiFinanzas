package com.finanzas.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

enum class Currency(
    val code: String,
    val displayName: String,
    val symbol: String,
    val locale: String
) {
    ARS("ARS", "Peso Argentino", "$", "es_AR"),
    USD("USD", "Dólar Estadounidense", "$", "en_US"),
    EUR("EUR", "Euro", "€", "es_ES"),
    MXN("MXN", "Peso Mexicano", "$", "es_MX"),
    CLP("CLP", "Peso Chileno", "$", "es_CL"),
    COP("COP", "Peso Colombiano", "$", "es_CO")
}

class ThemePreferences(private val context: Context) {
    
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_mode")
        private val CURRENCY_KEY = stringPreferencesKey("currency")
        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications_enabled")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
    }
    
    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name
        try {
            ThemeMode.valueOf(themeName)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }
    
    val currency: Flow<Currency> = context.dataStore.data.map { preferences ->
        val currencyName = preferences[CURRENCY_KEY] ?: Currency.ARS.name
        try {
            Currency.valueOf(currencyName)
        } catch (e: IllegalArgumentException) {
            Currency.ARS
        }
    }
    
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_KEY] ?: true
    }
    
    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY] ?: "Usuario"
    }
    
    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = mode.name
        }
    }
    
    suspend fun setCurrency(currency: Currency) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY_KEY] = currency.name
        }
    }
    
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_KEY] = enabled
        }
    }
    
    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }
}
