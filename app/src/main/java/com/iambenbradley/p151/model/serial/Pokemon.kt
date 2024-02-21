package com.iambenbradley.p151.model.serial

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonSummary(
    val name: String,
    val url: String,
)

@Serializable
data class PokemonDetail(
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
    val evolutionChain: EvolutionChain,
    @SerialName("evolves_from_species")
    val evolvesFromSpecies: SpeciesReference?,
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<FlavorText>,
    val habitat: String,
    val legendary: Boolean,
)

@Serializable
data class FlavorText(
    val text: String,
    val version: SerialVersion
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
)

@Serializable
data class EvolutionChain(
    val evolvesTo: List<EvolutionChain>,
    val species: SpeciesReference,
)


@Serializable
data class SerialColor(
    val name: String,
    val url: String,
)

@Serializable
data class SerialType(
    val name: String,
    val url: String,
)