package com.iambenbradley.p151.model.domain

data class PokemonDetailImpl(
    override val id: Long,
    override val name: String,
    override val sprite: String,
    override val color: PokeColor,
    override val relatedPokemon: List<PokemonSummary>,
    override val evolvesFrom: PokemonSummary?,
    override val flavorText: Map<PokeVersion, String>,
    override val habitat: String,
    override val isLegendary: Boolean,
) : PokemonSummary, PokemonDetail
