package com.iambenbradley.p151.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iambenbradley.p151.R
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
            val userTypePreference by userPrefsRepository.observe(
                UserPreferencesRepository.ColorPreferenceKey
            ).collectAsState(initial = "Other")
            val userType = PokeVersion.valueOf(userTypePreference ?: "Other")
            CompositionLocalProvider(LocalUserTypePreference provides(userType)) {
                P151Theme(colorTheme = userType) {

                    val navController = rememberNavController()

                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.pokemon_logo),
                            contentDescription = "Pokemon",
                            modifier = Modifier
                                .height(96.dp)
                                .background(color = MaterialTheme.colorScheme.primary)
                                .padding(4.dp)
                                .fillMaxWidth()
                        )
                        NavHost(
                            navController = navController,
                            startDestination = "list",
                        ) {
                            composable(route = "list") {
                                ListScreen(
                                    onClickPokemon = { id, name ->
                                        navController.navigate("detail/$id/$name")
                                    },
                                )
                            }
                            composable(
                                route = "detail/{$pokemonIdArg}/{$pokemonNameArg}",
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
}

val LocalUserTypePreference: ProvidableCompositionLocal<PokeVersion> = compositionLocalOf {
    PokeVersion.Red
}
