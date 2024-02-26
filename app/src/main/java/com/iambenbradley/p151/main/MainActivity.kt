package com.iambenbradley.p151.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import com.iambenbradley.p151.data.prefs.UserPreferencesRepository
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.ui.MainContent
import com.iambenbradley.p151.ui.theme.P151Theme
import com.iambenbradley.p151.util.IoDispatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var userPrefsRepository: UserPreferencesRepository

    @Inject @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userTypePreference by userPrefsRepository.observe(
                UserPreferencesRepository.ColorPreferenceKey,
            ).collectAsState(initial = "Other")
            val userType = PokeVersion.valueOf(userTypePreference ?: "Yellow")
            CompositionLocalProvider(LocalUserTypePreference provides(userType)) {
                P151Theme(colorTheme = userType) {
                    MainContent(
                        onLogoLongClick = {
                            CoroutineScope(ioDispatcher).launch {
                                userPrefsRepository.setPreference(
                                    UserPreferencesRepository.ColorPreferenceKey,
                                    userType.next().name,
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}

val LocalUserTypePreference: ProvidableCompositionLocal<PokeVersion> = compositionLocalOf {
    PokeVersion.Red
}
