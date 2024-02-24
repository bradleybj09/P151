package com.iambenbradley.p151.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface UserPreferencesRepository {
    fun <T> observe(key: Preferences.Key<T>): Flow<T?>
    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T)

    companion object {
        val ColorPreferenceKey = stringPreferencesKey("appColor")
    }
}

class DataStoreUserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesRepository {
    override fun <T> observe(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { prefs ->
            prefs[key]
        }
    }

    override suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
}