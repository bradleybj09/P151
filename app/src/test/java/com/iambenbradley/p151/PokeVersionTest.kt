package com.iambenbradley.p151

import com.iambenbradley.p151.model.domain.PokeVersion
import org.junit.Assert
import org.junit.Test

class PokeVersionTest {

    @Test
    fun `pokeVersion rotation follows red to blue to yellow`() {

        var pokeVersion = PokeVersion.Other

        Assert.assertEquals(
            PokeVersion.Red,
            pokeVersion.next()
        )

        pokeVersion = PokeVersion.Red
        Assert.assertEquals(
            PokeVersion.Blue,
            pokeVersion.next()
        )

        pokeVersion = PokeVersion.Blue
        Assert.assertEquals(
            PokeVersion.Yellow,
            pokeVersion.next()
        )

        pokeVersion = PokeVersion.Yellow
        Assert.assertEquals(
            PokeVersion.Red,
            pokeVersion.next()
        )
    }
}