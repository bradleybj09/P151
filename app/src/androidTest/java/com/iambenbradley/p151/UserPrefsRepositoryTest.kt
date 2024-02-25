package com.iambenbradley.p151

import app.cash.turbine.test
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.iambenbradley.p151.data.prefs.DataStoreUserPreferencesRepository
import com.iambenbradley.p151.data.prefs.UserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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
                awaitItem()
            )
            underTest.setPreference(testKey, "test")
            Assert.assertEquals(
                "test",
                awaitItem()
            )
        }
    }
}