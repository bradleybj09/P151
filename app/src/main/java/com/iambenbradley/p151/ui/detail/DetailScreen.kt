package com.iambenbradley.p151.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.iambenbradley.p151.main.LocalUserTypePreference
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.model.domain.PokemonDetail
import com.iambenbradley.p151.model.domain.PokemonSummary
import com.iambenbradley.p151.model.result.PokemonDetailResult
import com.iambenbradley.p151.ui.common.FailureScreen
import com.iambenbradley.p151.ui.common.LoadingScreen
import com.iambenbradley.p151.ui.common.PokemonSummaryCard
import com.iambenbradley.p151.util.toBackgroundColor
import java.util.Locale

@Composable
fun DetailScreen(
    onRelatedPokemonClick: (Long, String) -> Unit,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val pokemonState by viewModel.currentPokemon.collectAsState()
    val pokemonName by viewModel.pokemonName.collectAsState()
    val pokemonId by viewModel.pokemonId.collectAsState()
    val backgroundColor = LocalUserTypePreference.current.toBackgroundColor()

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .background(color = backgroundColor)
            .padding(4.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
    ) {
        PokemonSummaryCard(
            name = pokemonName.orEmpty(),
            id = pokemonId ?: 0,
            onClick = { _, _ -> },
            useBackButton = true,
            onBackButtonClick = onBackButtonClick,
        )

        when (val state = pokemonState) {
            PokemonDetailResult.Failure -> FailureScreen(
                text = "We had trouble catching this pokemon. Please check your internet or try " +
                    "again later.",
            )
            PokemonDetailResult.Loading -> LoadingScreen()
            is PokemonDetailResult.Success -> {
                PokemonDetails(
                    pokemon = state.detail,
                    onRelatedPokemonClick = onRelatedPokemonClick,
                )
            }
        }
    }
}

@Composable
fun PokemonDetails(
    pokemon: PokemonDetail,
    onRelatedPokemonClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val preferredType = LocalUserTypePreference.current

    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = modifier
            .fillMaxWidth()
            .semantics { testTag = "PokeDetail" },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        ) {
            AsyncImage(
                model = pokemon.sprite,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally),
            )
            if (pokemon.id in listOf(138L, 139L)) {
                Text(
                    text = "Praise Helix!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            AlignedDescriptor(
                startText = "Type: ",
                endText = pokemon.types.joinToString(" + "),
                modifier = Modifier.fillMaxWidth(),
            )
            pokemon.habitat?.let {
                AlignedDescriptor(
                    startText = "Habitat: ",
                    endText = it.replaceFirstChar { it.titlecase(Locale.getDefault()) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            pokemon.evolvesFrom?.let {
                AlignedDescriptor(
                    startText = "Evolves From: ",
                    endText = it.name.uppercase(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (pokemon.relatedPokemon.isNotEmpty()) {
                RelatedPokemon(
                    relatedPokemon = pokemon.relatedPokemon,
                    onRelatedPokemonClick = onRelatedPokemonClick,
                )
            }
            Text(
                text = pokemon.flavorText[preferredType]
                    ?: pokemon.flavorText[PokeVersion.Red].orEmpty(),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun AlignedDescriptor(
    startText: String,
    endText: String,
    textModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = startText,
            textAlign = TextAlign.End,
            modifier = textModifier.weight(1f),
        )
        Text(
            text = endText,
            textAlign = TextAlign.Start,
            modifier = textModifier.weight(1f),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RelatedPokemon(
    relatedPokemon: List<PokemonSummary>,
    onRelatedPokemonClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        Text(
            text = "Related Pokemon:",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            relatedPokemon.forEach { pokemon ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = LocalUserTypePreference.current.toBackgroundColor(),
                    ),
                    modifier = Modifier.clickable {
                        onRelatedPokemonClick(pokemon.id, pokemon.name)
                    },
                ) {
                    Text(
                        text = pokemon.name.uppercase(),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                    )
                }
            }
        }
    }
}
