package com.iambenbradley.p151.model.domain

data class PokemonSummaryImpl(
    override val id: Long,
    override val name: String,
) : PokemonSummary
