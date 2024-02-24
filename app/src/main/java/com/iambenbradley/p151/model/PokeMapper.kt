package com.iambenbradley.p151.model

import com.iambenbradley.p151.model.domain.PokeColor
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.model.domain.Type
import com.iambenbradley.p151.model.domain.PokemonDetail
import com.iambenbradley.p151.model.domain.PokemonDetailImpl
import com.iambenbradley.p151.model.domain.PokemonSummary
import com.iambenbradley.p151.model.domain.PokemonSummaryImpl
import com.iambenbradley.p151.model.serial.EvolutionChain
import com.iambenbradley.p151.model.serial.FlavorText
import com.iambenbradley.p151.model.serial.PokemonDetail as SerialDetail
import com.iambenbradley.p151.model.serial.PokemonSummary as SerialSummary
import com.iambenbradley.p151.model.serial.SerialColor
import com.iambenbradley.p151.model.serial.SerialType
import com.iambenbradley.p151.model.serial.SpeciesDetail
import com.iambenbradley.p151.model.serial.SpeciesReference
import javax.inject.Inject

class PokeMapper @Inject constructor() {

    fun serialToDomainColor(serial: SerialColor): PokeColor {
        return PokeColor.values().firstOrNull { color ->
            color.serialName == serial.name
        } ?: PokeColor.Unknown
    }

    fun serialToDomainType(serial: SerialType): Type {
        return Type.values().firstOrNull { type ->
            type.serialName == serial.name
        } ?: Type.Unknown
    }

    fun serialToDomainSummary(serial: SerialSummary): PokemonSummary {
        return PokemonSummaryImpl(
            id = serial.url.substringBeforeLast('/').toLongOrNull() ?: 0,
            name = serial.name,
        )
    }

    fun constructPokeData(
        pokemon: SerialDetail,
        species: SpeciesDetail,
        evolutionChain: EvolutionChain,
    ): PokemonDetail {
        return PokemonDetailImpl(
            id = pokemon.id,
            name = pokemon.name,
            sprite = pokemon.sprites.frontDefault,
            color = serialToDomainColor(species.color),
            relatedPokemon = getRelatedPokemonFromChain(evolutionChain),
            evolvesFrom = speciesReferenceToPokemonSummary(species.evolvesFromSpecies),
            flavorText = getFlavorTextFromSpecies(species.flavorTextEntries),
            habitat = species.habitat,
            isLegendary = species.isLegendary
        )
    }

    private fun getRelatedPokemonFromChain(
        evolutionChain: EvolutionChain,
    ): List<PokemonSummary> {
        // and there I was, telling people "I am never going to write a recursive function in
        // an android app"
        val evolvesTo = evolutionChain.evolvesTo
        val thisPokemon = speciesReferenceToPokemonSummary(evolutionChain.species)
        val others = evolvesTo.flatMap { chain ->
            getRelatedPokemonFromChain(chain)
        }
        return if (thisPokemon != null) {
            others + thisPokemon
        } else {
            others
        }
    }

    private fun speciesReferenceToPokemonSummary(
        speciesReference: SpeciesReference?
    ): PokemonSummary? {
        return if (speciesReference == null) {
            null
        } else {
            speciesReference.url.substringBeforeLast('/').toLongOrNull()?.let { id ->
                PokemonSummaryImpl(
                    name = speciesReference.name,
                    id = id
                )
            }
        }
    }

    private fun getFlavorTextFromSpecies(flavorTexts: List<FlavorText>): Map<PokeVersion, String> {
        return mapOf(
            PokeVersion.Red to flavorTexts.firstOrNull {
                it.version.name == "red"
            }?.text.orEmpty(),
            PokeVersion.Blue to flavorTexts.firstOrNull {
                it.version.name == "blue"
            }?.text.orEmpty(),
            PokeVersion.Yellow to flavorTexts.firstOrNull {
                it.version.name == "yellow"
            }?.text.orEmpty(),
        )
    }
}