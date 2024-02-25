package com.iambenbradley.p151.model

import com.iambenbradley.p151.model.domain.PokeColor
import com.iambenbradley.p151.model.domain.PokeVersion
import com.iambenbradley.p151.model.domain.Type
import com.iambenbradley.p151.model.domain.PokemonDetail
import com.iambenbradley.p151.model.domain.PokemonDetailImpl
import com.iambenbradley.p151.model.domain.PokemonSummary
import com.iambenbradley.p151.model.domain.PokemonSummaryImpl
import com.iambenbradley.p151.model.serial.EvolutionChain
import com.iambenbradley.p151.model.serial.EvolutionChainInnerData
import com.iambenbradley.p151.model.serial.FlavorText
import com.iambenbradley.p151.model.serial.PokemonDetail as SerialDetail
import com.iambenbradley.p151.model.serial.PokemonSummary as SerialSummary
import com.iambenbradley.p151.model.serial.SerialColor
import com.iambenbradley.p151.model.serial.SerialType
import com.iambenbradley.p151.model.serial.SpeciesDetail
import com.iambenbradley.p151.model.serial.SpeciesReference
import com.iambenbradley.p151.util.getPokemonId
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

class PokeMapper @Inject constructor() {

    @VisibleForTesting
    fun serialToDomainColor(serial: SerialColor): PokeColor {
        return PokeColor.values().firstOrNull { color ->
            color.serialName == serial.name
        } ?: PokeColor.Unknown
    }

    @VisibleForTesting
    fun serialToDomainType(serial: SerialType): Type {
        return Type.values().firstOrNull { type ->
            type.serialName == serial.type.name
        } ?: Type.Unknown
    }

    fun serialToDomainSummary(serial: SerialSummary): PokemonSummary {
        return PokemonSummaryImpl(
            id = serial.url.getPokemonId() ?: 0,
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
            sprite = pokemon.sprites.other.home.frontDefault,
            color = serialToDomainColor(species.color),
            relatedPokemon = getRelatedPokemonFromChain(evolutionChain.chain).filter {
                it.id != pokemon.id && it.id <= 151 // I'm looking at you, Eevee gen2 evolutions
            }.reversed(),
            evolvesFrom = speciesReferenceToPokemonSummary(species.evolvesFromSpecies)
                ?.takeUnless { it.id > 151 }, // Jynx...
            flavorText = getFlavorTextFromSpecies(species.flavorTextEntries),
            habitat = species.habitat?.name,
            isLegendary = species.isLegendary,
            types = pokemon.types.map { serialToDomainType(it) }.toSet()
        )
    }

    @VisibleForTesting
    fun getRelatedPokemonFromChain(
        evolutionChain: EvolutionChainInnerData,
    ): List<PokemonSummary> {
        val evolvesTo = evolutionChain.evolvesTo
        val thisPokemon = speciesReferenceToPokemonSummary(evolutionChain.species)
        val others = evolvesTo?.flatMap { chain ->
            getRelatedPokemonFromChain(chain)
        }.orEmpty()
        return if (thisPokemon != null) {
            others + thisPokemon
        } else {
            others
        }
    }

    @VisibleForTesting
    fun speciesReferenceToPokemonSummary(
        speciesReference: SpeciesReference?
    ): PokemonSummary? {
        return if (speciesReference == null) {
            null
        } else {
            speciesReference
                .url.getPokemonId()
                ?.let { id ->
                PokemonSummaryImpl(
                    name = speciesReference.name,
                    id = id
                )
            }
        }
    }

    @VisibleForTesting
    fun getFlavorTextFromSpecies(flavorTexts: List<FlavorText>): Map<PokeVersion, String> {
        return mapOf(
            PokeVersion.Red to flavorTexts.firstOrNull {
                it.version.name == "red"
            }?.text.orEmpty().cleanFlavorText(),
            PokeVersion.Blue to flavorTexts.firstOrNull {
                it.version.name == "blue"
            }?.text.orEmpty().cleanFlavorText(),
            PokeVersion.Yellow to flavorTexts.firstOrNull {
                it.version.name == "yellow"
            }?.text.orEmpty().cleanFlavorText(),
        )
    }

    @VisibleForTesting
    fun String.cleanFlavorText(): String {
        // weird \f form feed escapes---------------------------------------▼
        return this.replace("\n", " ").replace("\u000c", " ")
    }
}