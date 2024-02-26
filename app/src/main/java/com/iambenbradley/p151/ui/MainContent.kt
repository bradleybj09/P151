package com.iambenbradley.p151.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iambenbradley.p151.R
import com.iambenbradley.p151.main.pokemonIdArg
import com.iambenbradley.p151.main.pokemonNameArg
import com.iambenbradley.p151.ui.detail.DetailScreen
import com.iambenbradley.p151.ui.list.ListScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(
    onLogoLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.pokemon_logo),
            contentDescription = "Pokemon",
            modifier = Modifier
                .combinedClickable(
                    onClick = {},
                    onLongClick = onLogoLongClick,
                    onLongClickLabel = "Change Version"
                )
                .height(76.dp)
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(8.dp)
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
                DetailScreen(
                    onRelatedPokemonClick = { id, name ->
                        navController.navigate("detail/$id/$name")
                    },
                    onBackButtonClick = {
                        navController.popBackStack(
                            route = "list",
                            inclusive = false
                        )
                    }
                )
            }
        }
    }
}