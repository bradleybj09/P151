package com.iambenbradley.p151.model.result

import com.iambenbradley.p151.model.domain.PokemonSummary

/**
 * For a simple app, a simple singular state passed from repository to ui.
 * If the project expands, keep one for UI and make another for the backend
 *
 * Represents a simple model of a pokemon, fit for the list screen.
 */
sealed interface PokemonSummaryResult {
    data object Loading : PokemonSummaryResult
    data object Failure : PokemonSummaryResult
    data class Success(
        val summaries: List<PokemonSummary>,
    ) : PokemonSummaryResult
}
