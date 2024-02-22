package com.iambenbradley.p151.model.result

import com.iambenbradley.p151.model.domain.PokemonDetail

sealed interface PokemonDetailResult {

    data object Failure : PokemonDetailResult
    data class Success(
        val detail: PokemonDetail
    ) : PokemonDetailResult
}