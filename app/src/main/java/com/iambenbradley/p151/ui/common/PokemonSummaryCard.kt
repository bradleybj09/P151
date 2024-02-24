package com.iambenbradley.p151.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.iambenbradley.p151.model.domain.PokemonSummary

@Composable
fun PokemonSummaryCard(
    pokemon: PokemonSummary,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clickable { onClick(pokemon.id) }
    ) {
        Row {
            Image(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "pokeball",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = pokemon.id.toString().padStart(3, '0'),
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = pokemon.name,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(2f)
            )
        }
    }
}