package com.iambenbradley.p151.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iambenbradley.p151.data.prefs.UserPreferencesRepository
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.ui.detail.DetailScreen
import com.iambenbradley.p151.ui.list.ListScreen
import com.iambenbradley.p151.ui.theme.P151Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var userPrefsRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            P151Theme {
                val userTypePreference by userPrefsRepository.observe(
                    UserPreferencesRepository.ColorPreferenceKey
                ).collectAsState(initial = "Red")
                val userType = PokeVersion.valueOf(userTypePreference ?: "Red")

                CompositionLocalProvider(LocalUserTypePreference provides(userType)) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "list",
                    ) {
                        composable(route = "list") {
                            ListScreen(
                                onClickPokemon = { id ->
                                    navController.navigate("detail/$id")
                                }
                            )
                        }
                        composable(
                            route = "detail/{pokemonId}",
                            arguments = listOf(
                                navArgument("pokemonId") {
                                    type = NavType.LongType
                                }
                            )
                        ) {
                            DetailScreen()
                        }
                    }
                }
            }
        }
    }
}

val LocalUserTypePreference: ProvidableCompositionLocal<PokeVersion> = compositionLocalOf {
    PokeVersion.Red
}
