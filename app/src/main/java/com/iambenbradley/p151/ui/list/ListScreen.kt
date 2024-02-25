package com.iambenbradley.p151.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iambenbradley.p151.main.LocalUserTypePreference
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.ui.common.FailureScreen
import com.iambenbradley.p151.ui.common.LoadingScreen
import com.iambenbradley.p151.ui.common.PokemonSummaryCard
import com.iambenbradley.p151.ui.theme.LightBlue
import com.iambenbradley.p151.ui.theme.LightRed
import com.iambenbradley.p151.ui.theme.LightYellow

@Composable
fun ListScreen(
    onClickPokemon: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ListViewModel = hiltViewModel()
    val backgroundColor = when (LocalUserTypePreference.current) {
        PokeVersion.Red -> LightRed
        PokeVersion.Blue -> LightBlue
        PokeVersion.Yellow -> LightYellow
        PokeVersion.Other -> Color.LightGray
    }

    val pokemonState by viewModel.pokemon.collectAsState()

    when (val state = pokemonState) {
        PokemonSummaryResult.Failure -> FailureScreen(
            text = "We had trouble finding your pokemon. Make sure you are connected to " +
                "the internet, or try again later."
        )
        PokemonSummaryResult.Loading -> LoadingScreen()
        is PokemonSummaryResult.Success -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = modifier
                    .background(color = backgroundColor)
                    .padding(4.dp)
            ) {
                items(
                    items = state.summaries,
                    key = { it.name }
                ) {pokemon ->
                    PokemonSummaryCard(
                        name = pokemon.name,
                        id = pokemon.id,
                        onClick = onClickPokemon,
                    )
                }
            }
        }
    }
}