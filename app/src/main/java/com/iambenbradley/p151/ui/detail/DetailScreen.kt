package com.iambenbradley.p151.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.iambenbradley.p151.main.LocalUserTypePreference
import com.iambenbradley.p151.model.result.PokemonDetailResult
import com.iambenbradley.p151.ui.common.FailureScreen
import com.iambenbradley.p151.ui.common.LoadingScreen
import com.iambenbradley.p151.ui.common.PokemonSummaryCard

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: DetailViewModel = hiltViewModel()
    val preferredType = LocalUserTypePreference.current

    val pokemonState by viewModel.currentPokemon.collectAsState()
    val pokemonName by viewModel.pokemonName.collectAsState()
    val pokemonId by viewModel.pokemonId.collectAsState()

    Column(modifier = modifier) {
        PokemonSummaryCard(
            name = pokemonName.orEmpty(),
            id = pokemonId ?: 0,
            onClick = { _, _ -> }
        )

        when (val state = pokemonState) {
            PokemonDetailResult.Failure -> FailureScreen(text = "We had trouble catching this " +
                "pokemon. Please check your internet or try again later.")
            PokemonDetailResult.Loading -> LoadingScreen()
            is PokemonDetailResult.Success -> Text(text = "hooray", modifier = modifier.weight(1f), textAlign = TextAlign.Center)
        }
    }

}