package com.iambenbradley.p151.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.repository.PokemonRepository
import com.iambenbradley.p151.util.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    repository: PokemonRepository,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
): ViewModel() {

    val pokemon: StateFlow<PokemonSummaryResult> = repository.pokemonSummaries.stateIn(
        viewModelScope + defaultDispatcher,
        started = SharingStarted.Lazily,
        initialValue = PokemonSummaryResult.Loading
    )
}