package com.iambenbradley.p151.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.repository.PokemonRepository
import com.iambenbradley.p151.util.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@HiltViewModel
class ListViewModel @Inject constructor(
    repository: PokemonRepository,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    init {
        viewModelScope.launch {
            // don't bother with scope here, this is main safe
            repository.updateSummaries()
        }
    }

    val pokemon: StateFlow<PokemonSummaryResult> = repository.pokemonSummaries.stateIn(
        viewModelScope + defaultDispatcher,
        started = SharingStarted.Lazily,
        initialValue = PokemonSummaryResult.Loading,
    )
}
