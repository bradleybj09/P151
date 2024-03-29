package com.iambenbradley.p151

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.iambenbradley.p151.data.prefs.DataStoreUserPreferencesRepository
import com.iambenbradley.p151.data.prefs.UserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserPrefsRepositoryTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val dataStore = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("test")
    }

    private val testKey = stringPreferencesKey("test")

    private lateinit var underTest: UserPreferencesRepository

    @Before
    fun setUp() {
        underTest = DataStoreUserPreferencesRepository(dataStore)
    }

    // Arguably, should this still just be jvm testing with a mocked datastore?
    @Test
    fun repositorySavesAndRecoversCorrectly() = runTest {
        underTest.observe(testKey).test {
            Assert.assertEquals(
                null,
                awaitItem(),
            )
            underTest.setPreference(testKey, "test")
            Assert.assertEquals(
                "test",
                awaitItem(),
            )
        }
    }
}
