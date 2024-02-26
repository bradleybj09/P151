package com.iambenbradley.p151.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iambenbradley.p151.main.LocalUserTypePreference
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.ui.common.FailureScreen
import com.iambenbradley.p151.ui.common.LoadingScreen
import com.iambenbradley.p151.ui.common.PokemonSummaryCard
import com.iambenbradley.p151.util.toBackgroundColor

@Composable
fun ListScreen(
    onClickPokemon: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel(),
) {
    val backgroundColor = LocalUserTypePreference.current.toBackgroundColor()

    val pokemonState by viewModel.pokemon.collectAsState()

    when (val state = pokemonState) {
        PokemonSummaryResult.Failure -> FailureScreen(
            text = "We had trouble finding your pokemon. Make sure you are connected to " +
                "the internet, or try again later."
        )
        PokemonSummaryResult.Loading -> LoadingScreen()
        is PokemonSummaryResult.Success -> {
            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = modifier
                    .background(color = backgroundColor)
                    .padding(4.dp)
                    .semantics { testTag = "PokeList" }
            ) {
                items(
                    items = state.summaries,
                    key = { it.name }
                ) {pokemon ->
                    PokemonSummaryCard(
                        name = pokemon.name,
                        id = pokemon.id,
                        onClick = onClickPokemon,
                        useBackButton = false,
                        onBackButtonClick = {}
                    )
                }
            }
        }
    }
}