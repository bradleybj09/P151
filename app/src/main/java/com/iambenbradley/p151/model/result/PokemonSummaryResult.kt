package com.iambenbradley.p151.model.result

import com.iambenbradley.p151.model.domain.PokemonSummary

sealed interface PokemonSummaryResult {
    data object Failure : PokemonSummaryResult
    data class Success(
        val summaries: List<PokemonSummary>
    ) : PokemonSummaryResult
}