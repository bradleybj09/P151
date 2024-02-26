package com.iambenbradley.p151.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Module
@InstallIn(SingletonComponent::class)
interface UserPreferencesBindingModule {

    @Binds
    @Singleton
    fun bindUserPreferencesRepository(
        impl: DataStoreUserPreferencesRepository,
    ): UserPreferencesRepository
}

/**
 * If this was more complicated and thorough, we'd abstract this away from any knowledge
 * of Preferences: key, etc.
 */
interface UserPreferencesRepository {
    /**
     * Observe a preferences key to get a Flow of that data type.
     */
    fun <T> observe(key: Preferences.Key<T>): Flow<T?>

    /**
     * Update local preferences based on the preference key.
     */
    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T)

    companion object {
        /**
         * A preference key to use with changing the app's "game version".
         */
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
