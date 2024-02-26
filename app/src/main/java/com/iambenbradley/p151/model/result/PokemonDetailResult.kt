package com.iambenbradley.p151.model.result

import com.iambenbradley.p151.model.domain.PokemonDetail

/**
 * For a simple app, a simple singular state passed from repository to ui.
 * If the project expands, keep one for UI and make another for the backend
 *
 * Represents a complex model of a pokemon, fit for the detail screen.
 */
sealed interface PokemonDetailResult {
    data object Loading : PokemonDetailResult
    data object Failure : PokemonDetailResult
    data class Success(
        val detail: PokemonDetail,
    ) : PokemonDetailResult
}
