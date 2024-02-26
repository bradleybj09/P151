package com.iambenbradley.p151.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iambenbradley.p151.main.pokemonIdArg
import com.iambenbradley.p151.main.pokemonNameArg
import com.iambenbradley.p151.model.result.PokemonDetailResult
import com.iambenbradley.p151.repository.PokemonRepository
import com.iambenbradley.p151.util.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus

@HiltViewModel
class DetailViewModel @Inject constructor(
    repository: PokemonRepository,
    savedStateHandle: SavedStateHandle,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val pokemonId = savedStateHandle.getStateFlow<Long?>(pokemonIdArg, null)
    val pokemonName = savedStateHandle.getStateFlow<String?>(pokemonNameArg, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentPokemon: StateFlow<PokemonDetailResult> = pokemonId.flatMapLatest { id ->
        if (id == null) {
            flowOf(PokemonDetailResult.Loading)
        } else {
            repository.getPokemonDetail(id)
        }
    }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope + defaultDispatcher,
            started = SharingStarted.Lazily,
            initialValue = PokemonDetailResult.Loading,
        )
}
