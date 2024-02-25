package com.iambenbradley.p151.util

import androidx.compose.ui.graphics.Color
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.ui.theme.LightBlue
import com.iambenbradley.p151.ui.theme.LightRed
import com.iambenbradley.p151.ui.theme.LightYellow

fun String.getPokemonId() = substringBeforeLast('/')
    .substringAfterLast('/')
    .toLongOrNull()

fun PokeVersion.toBackgroundColor() = when (this) {
    PokeVersion.Red -> LightRed
    PokeVersion.Blue -> LightBlue
    PokeVersion.Yellow -> LightYellow
    PokeVersion.Other -> Color.LightGray
}