package com.iambenbradley.p151.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.iambenbradley.p151.model.domain.PokeVersion

private val RedColorScheme = lightColorScheme(
    primary = Red,
    secondary = DarkRed,
    tertiary = LightRed,
)

private val BlueColorScheme = lightColorScheme(
    primary = Blue,
    secondary = DarkBlue,
    tertiary = LightBlue,
)

private val YellowColorScheme = lightColorScheme(
    primary = Yellow,
    secondary = DarkYellow,
    tertiary = LightYellow,
)

private val OtherColorScheme = lightColorScheme(
    primary = Color.Gray,
    secondary = Color.DarkGray,
    tertiary = Color.LightGray,
)

private val RedColorSchemeDark = darkColorScheme(
    primary = Red,
    secondary = DarkRed,
    tertiary = LightRed,
)

private val BlueColorSchemeDark = darkColorScheme(
    primary = Blue,
    secondary = DarkBlue,
    tertiary = LightBlue,
)

private val YellowColorSchemeDark = darkColorScheme(
    primary = Yellow,
    secondary = DarkYellow,
    tertiary = LightYellow,
)

private val OtherColorSchemeDark = darkColorScheme(
    primary = Color.Gray,
    secondary = Color.DarkGray,
    tertiary = Color.LightGray,
)


@Composable
fun P151Theme(
    colorTheme: PokeVersion,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when (darkTheme) {
        true -> when (colorTheme) {
            PokeVersion.Red -> RedColorSchemeDark
            PokeVersion.Blue -> BlueColorSchemeDark
            PokeVersion.Yellow -> YellowColorSchemeDark
            PokeVersion.Other -> OtherColorSchemeDark
        }
        false -> when (colorTheme) {
            PokeVersion.Red -> RedColorScheme
            PokeVersion.Blue -> BlueColorScheme
            PokeVersion.Yellow -> YellowColorScheme
            PokeVersion.Other -> OtherColorScheme
        }
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}