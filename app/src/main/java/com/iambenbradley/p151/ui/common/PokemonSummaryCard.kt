package com.iambenbradley.p151.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    useBackButton: Boolean,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .height(56.dp)
            .clickable { onClick(id, name) }
    ) {
        Row(
            modifier = Modifier.padding(4.dp)
                .fillMaxHeight()
        ) {
            if (useBackButton) {
                IconButton(
                    onClick = onBackButtonClick,
                    modifier = Modifier
                        .weight(1f)
                        .size(24.dp)
                        .align(Alignment.CenterVertically)

                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back to list",
                    )

                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.pokeball),
                    contentDescription = "pokeball",
                    modifier = Modifier
                        .weight(1f)
                        .size(48.dp)
                )
            }
            Text(
                text = id.toString().padStart(3, '0'),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = name.uppercase(),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}