package com.iambenbradley.p151

import com.iambenbradley.p151.model.PokeMapper
import com.iambenbradley.p151.model.domain.PokeColor
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.model.domain.PokemonDetailImpl
import com.iambenbradley.p151.model.domain.PokemonSummaryImpl
import com.iambenbradley.p151.model.domain.Type
import com.iambenbradley.p151.model.serial.EvolutionChain
import com.iambenbradley.p151.model.serial.EvolutionChainInnerData
import com.iambenbradley.p151.model.serial.EvolutionChainReference
import com.iambenbradley.p151.model.serial.FlavorText
import com.iambenbradley.p151.model.serial.Habitat
import com.iambenbradley.p151.model.serial.HomeSprites
import com.iambenbradley.p151.model.serial.OtherSprites
import com.iambenbradley.p151.model.serial.PokemonDetail
import com.iambenbradley.p151.model.serial.PokemonSummary as SerialSummary
import com.iambenbradley.p151.model.serial.SerialColor
import com.iambenbradley.p151.model.serial.SerialInnerType
import com.iambenbradley.p151.model.serial.SerialType
import com.iambenbradley.p151.model.serial.SerialVersion
import com.iambenbradley.p151.model.serial.SpeciesDetail
import com.iambenbradley.p151.model.serial.SpeciesReference
import com.iambenbradley.p151.model.serial.Sprites
import org.junit.Assert
import org.junit.Test

class PokeMapperTest {

    private val underTest = PokeMapper()

    @Test
    fun `color mapping gets color from known option, and Unknown from unknown option`() {
        val knownColor = SerialColor("red", "")
        val unknownColor = SerialColor("blargh!", "")
        Assert.assertEquals(
            "Known color 'red' should map to PokeColor.Red",
            PokeColor.Red,
            underTest.serialToDomainColor(knownColor)
        )
        Assert.assertEquals(
            "Unknown color 'blargh!' should map to PokeColor.Unknown",
            PokeColor.Unknown,
            underTest.serialToDomainColor(unknownColor)
        )
    }

    @Test
    fun `type mapping gets type from known option, and Unknown from unknown option`() {
        val knownType = SerialType(SerialInnerType("fire", ""))
        val unknownType = SerialType(SerialInnerType("blargh!", ""))
        Assert.assertEquals(
            "Known type 'fire' should map to Type.Fire",
            Type.Fire,
            underTest.serialToDomainType(knownType)
        )
        Assert.assertEquals(
            "Unknown type 'blargh!' should map to Type.Unknown",
            Type.Unknown,
            underTest.serialToDomainType(unknownType)
        )
    }

    @Test
    fun `summary mapping maps correctly, including extracting pokemon Id`() {
        val serial = SerialSummary(
            name = "charmeleon",
            url = "https://pokeapi.co/api/v2/pokemon/5/"
        )
        Assert.assertEquals(
            "Summary mapping failed name or id conversion",
            PokemonSummaryImpl(5, "charmeleon"),
            underTest.serialToDomainSummary(serial)
        )
    }

    @Test
    fun `species to summary mapping maps correctly, including null to null`() {
        val serial = SpeciesReference(
            name = "charmeleon",
            url = "https://pokeapi.co/api/v2/pokemon-species/5/"
        )
        Assert.assertEquals(
            "Species to summary mapping failed name or id conversion",
            PokemonSummaryImpl(5, "charmeleon"),
            underTest.speciesReferenceToPokemonSummary(serial)
        )
        Assert.assertEquals(
            "Species to summary mapping did not handle null correctly",
            null,
            underTest.speciesReferenceToPokemonSummary(null)
        )
    }

    @Test
    fun `flavor text cleaning correctly removes unwanted escapes`() {
        val serial = "The flame on its\ntail indicates\nCHARMANDER's life\u000cforce. If it is\nhealthy, the flame\nburns brightly."
        val expected = "The flame on its tail indicates CHARMANDER's life force. If it is healthy, the flame burns brightly."
        Assert.assertEquals(
            "Flavor text cleaning did not handle escapes correctly",
            expected,
            underTest.run { serial.cleanFlavorText() }
        )
    }

    @Test
    fun `flavor text mapping correctly maps red, blue, and yellow texts`() {
        val serial = listOf(
            FlavorText(text = "RedText", version = SerialVersion(name = "red", url = "")),
            FlavorText(text = "BlueText", version = SerialVersion(name = "blue", url = "")),
            FlavorText(text = "YellowText", version = SerialVersion(name = "yellow", url = "")),
            FlavorText(text = "SilverText", version = SerialVersion(name = "Silver", url = "")),
        )
        val expected = mapOf(
            PokeVersion.Red to "RedText",
            PokeVersion.Blue to "BlueText",
            PokeVersion.Yellow to "YellowText",
        )
        Assert.assertEquals(
            "Flavor text mapping failed to map correct versions",
            expected,
            underTest.getFlavorTextFromSpecies(serial)
        )
    }

    @Test
    fun `evolution chain mapping correctly gets all pokemon from the chain`() {
        val serial = EvolutionChainInnerData(
            evolvesTo = listOf(
                EvolutionChainInnerData(
                    evolvesTo = listOf(
                        EvolutionChainInnerData(
                            evolvesTo = null,
                            species = SpeciesReference("1","/1/"),
                        ),
                    ),
                    species = SpeciesReference("2","/2/"),
                ),
                EvolutionChainInnerData(
                    evolvesTo = null,
                    species = SpeciesReference("3","/3/"),
                )
            ),
            species = SpeciesReference("4","/4/"),
        )
        val expected = listOf(
            PokemonSummaryImpl(1, "1"),
            PokemonSummaryImpl(2, "2"),
            PokemonSummaryImpl(3, "3"),
            PokemonSummaryImpl(4, "4"),
        )
        Assert.assertEquals(
            "EvolutionChain parsing failed to find related pokemon",
            expected,
            underTest.getRelatedPokemonFromChain(serial)
        )
    }

    @Test
    fun `pokeData construction correctly maps data, including filtering self and id over 151 from related pokemon`() {
        val evoChain = EvolutionChain(
            EvolutionChainInnerData(
                evolvesTo = listOf(
                    EvolutionChainInnerData(
                        evolvesTo = listOf(
                            EvolutionChainInnerData(
                                evolvesTo = null,
                                species = SpeciesReference("1","/1/"),
                            ),
                        ),
                        species = SpeciesReference("2","/2/"),
                    ),
                    EvolutionChainInnerData(
                        evolvesTo = null,
                        species = SpeciesReference("3","/3/"),
                    )
                ),
                species = SpeciesReference("200","/200/"),
            )
        )
        val species = SpeciesReference("1", "/1/")
        val sprites = Sprites(
            frontDefault = "pixelSprite",
            other = OtherSprites(
                home = HomeSprites(
                    frontDefault = "homeSprite"
                )
            )
        )
        val types = setOf(
            SerialType(SerialInnerType("fire","")),
            SerialType(SerialInnerType("flying", "")),
        )
        val pokemon = PokemonDetail(1, "1", species, sprites, types)
        val speciesDetail = SpeciesDetail(
            color = SerialColor("red", ""),
            evolutionChain = EvolutionChainReference(""),
            evolvesFromSpecies = SpeciesReference("2","/2/"),
            flavorTextEntries = listOf(
                FlavorText(text = "RedText", version = SerialVersion(name = "red", url = "")),
                FlavorText(text = "BlueText", version = SerialVersion(name = "blue", url = "")),
                FlavorText(text = "YellowText", version = SerialVersion(name = "yellow", url = "")),
                FlavorText(text = "SilverText", version = SerialVersion(name = "Silver", url = "")),
            ),
            habitat = Habitat("hab"),
            isLegendary = true,
        )

        val expected = PokemonDetailImpl(
            id = 1,
            name = "1",
            sprite = "homeSprite",
            color = PokeColor.Red,
            relatedPokemon = listOf(
                PokemonSummaryImpl(3, "3"),
                PokemonSummaryImpl(2, "2"),
            ),
            evolvesFrom = PokemonSummaryImpl(2, "2"),
            flavorText = mapOf(
                PokeVersion.Red to "RedText",
                PokeVersion.Blue to "BlueText",
                PokeVersion.Yellow to "YellowText",
            ),
            habitat = "hab",
            isLegendary = true,
            types = setOf(Type.Fire, Type.Flying)
        )

        Assert.assertEquals(
            "PokeData mapping failed",
            expected,
            underTest.constructPokeData(
                pokemon = pokemon,
                species = speciesDetail,
                evolutionChain = evoChain,
            )
        )
    }
}