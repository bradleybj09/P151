package com.iambenbradley.p151.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iambenbradley.p151.R

@Composable
fun PokemonSummaryCard(
    name: String,
    id: Long,
    onClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .clickable { onClick(id, name) }
    ) {
        Row(
            modifier = Modifier.padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pokeball),
                contentDescription = "pokeball",
                modifier = Modifier
                    .weight(1f)
                    .size(48.dp)
            )
            Text(
                text = id.toString().padStart(3, '0'),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)
            )
            Text(
                text = name.uppercase(),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(2f)
                    .align(CenterVertically)
            )
        }
    }
}