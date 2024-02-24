package com.iambenbradley.p151.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.iambenbradley.p151.main.LocalUserTypePreference
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.ui.common.PokemonSummaryCard

@Composable
fun ListScreen(
    onClickPokemon: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ListViewModel = hiltViewModel()
    val preferredTypeColor = LocalUserTypePreference.current.getTint()

    val pokemonState by viewModel.pokemon.collectAsState()

    when (val state = pokemonState) {
        PokemonSummaryResult.Failure -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "We had trouble finding your pokemon. Make sure you are connected to " +
                        "the internet, or try again later.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        PokemonSummaryResult.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
        is PokemonSummaryResult.Success -> {
            LazyColumn(
                modifier = modifier
            ) {
                items(
                    items = state.summaries,
                    key = { it.name }
                ) {pokemon ->
                    PokemonSummaryCard(
                        pokemon = pokemon,
                        onClick = onClickPokemon,
                    )
                }
            }
        }
    }
}