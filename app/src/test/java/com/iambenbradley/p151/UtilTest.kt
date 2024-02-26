package com.iambenbradley.p151

import androidx.compose.ui.graphics.Color
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.ui.theme.LightBlue
import com.iambenbradley.p151.ui.theme.LightRed
import com.iambenbradley.p151.ui.theme.LightYellow
import com.iambenbradley.p151.util.getPokemonId
import com.iambenbradley.p151.util.toBackgroundColor
import org.junit.Assert
import org.junit.Test

class UtilTest {

    @Test
    fun `version parsing correctly gets version or null`() {
        val path = "xyz/1/"
        val badPath = "xyz/1"

        Assert.assertEquals(1L, path.getPokemonId())
        Assert.assertEquals(null, badPath.getPokemonId())
    }

    @Test
    fun `background color from version gets correct background color`() {
        val versions = listOf(
            PokeVersion.Red,
            PokeVersion.Blue,
            PokeVersion.Yellow,
            PokeVersion.Other,
        )
        val expected = listOf(LightRed, LightBlue, LightYellow, Color.LightGray)

        Assert.assertEquals(
            expected,
            versions.map { it.toBackgroundColor() },
        )
    }
}
