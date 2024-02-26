package com.iambenbradley.p151.model.serial

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllPokemonResult(
    val count: Int,
    val results: List<PokemonSummary>,
)

@Serializable
data class PokemonSummary(
    val name: String,
    val url: String,
)

@Serializable
data class PokemonDetail(
    val id: Long,
    val name: String,
    val species: SpeciesReference,
    val sprites: Sprites,
    val types: Set<SerialType>,
)

@Serializable
data class SpeciesReference(
    val name: String,
    val url: String,
)

@Serializable
data class SpeciesDetail(
    val color: SerialColor,
    @SerialName("evolution_chain")
    val evolutionChain: EvolutionChainReference,
    @SerialName("evolves_from_species")
    val evolvesFromSpecies: SpeciesReference?,
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<FlavorText>,
    val habitat: Habitat? = null,
    @SerialName("is_legendary")
    val isLegendary: Boolean,
)

@Serializable
data class Habitat(
    val name: String,
)

@Serializable
data class FlavorText(
    @SerialName("flavor_text")
    val text: String,
    val version: SerialVersion,
)

@Serializable
data class SerialVersion(
    val name: String,
    val url: String,
)

@Serializable
data class Sprites(
    @SerialName("front_default")
    val frontDefault: String,
    val other: OtherSprites,
)

@Serializable
data class OtherSprites(
    val home: HomeSprites,
)

@Serializable
data class HomeSprites(
    @SerialName("front_default")
    val frontDefault: String,
)

@Serializable
data class EvolutionChainReference(
    val url: String,
)

@Serializable
data class EvolutionChain(
    val chain: EvolutionChainInnerData,
)

@Serializable
data class EvolutionChainInnerData(
    @SerialName("evolves_to")
    val evolvesTo: List<EvolutionChainInnerData>? = null,
    val species: SpeciesReference,
)

@Serializable
data class SerialColor(
    val name: String,
    val url: String,
)

@Serializable
data class SerialType(
    val type: SerialInnerType,
)

@Serializable
data class SerialInnerType(
    val name: String,
    val url: String,
)
