package com.iambenbradley.p151.util

import androidx.compose.ui.graphics.Color
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.ui.theme.LightBlue
import com.iambenbradley.p151.ui.theme.LightRed
import com.iambenbradley.p151.ui.theme.LightYellow

/**
 * This API doesn't use IDs, except for in one embedded payload. However, the ID is always
 * available in the same format /{id}/ at the end of the urls.
 *
 * If this was a real api in a real project, we'd be asking them to change their ways.
 */
fun String.getPokemonId() = substringBeforeLast('/')
    .substringAfterLast('/')
    .toLongOrNull()

/**
 * A convenience function for consistent background colors based on the "game version" the app is
 * set to via the user prefs repo.
 */
fun PokeVersion.toBackgroundColor() = when (this) {
    PokeVersion.Red -> LightRed
    PokeVersion.Blue -> LightBlue
    PokeVersion.Yellow -> LightYellow
    PokeVersion.Other -> Color.LightGray
}
