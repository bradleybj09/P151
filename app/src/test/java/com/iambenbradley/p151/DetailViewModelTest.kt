package com.iambenbradley.p151

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.iambenbradley.p151.main.pokemonIdArg
import com.iambenbradley.p151.main.pokemonNameArg
import com.iambenbradley.p151.model.domain.PokeColor
import com.iambenbradley.p151.model.domain.PokemonDetailImpl
import com.iambenbradley.p151.model.result.PokemonDetailResult
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.repository.PokemonRepository
import com.iambenbradley.p151.ui.detail.DetailViewModel
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DetailViewModelTest {

    private val pokemonDetail = PokemonDetailImpl(
        id = 1, name = "1", sprite = "", color = PokeColor.Red, relatedPokemon = emptyList(),
        evolvesFrom = null, flavorText = emptyMap(), habitat = null, isLegendary = false,
        types = emptySet(),
    )

    private val pokemonRepository = object : PokemonRepository {
        override suspend fun updateSummaries() {
            TODO("Not yet implemented")
        }

        override val pokemonSummaries: Flow<PokemonSummaryResult>
            get() = TODO("Not yet implemented")

        override fun getPokemonDetail(id: Long): Flow<PokemonDetailResult> {
            return flowOf(PokemonDetailResult.Success(pokemonDetail))
        }
    }

    private val savedStateHandle = SavedStateHandle()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var underTest: DetailViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        underTest = DetailViewModel(pokemonRepository, savedStateHandle, testDispatcher)
    }

    @Test
    fun `viewModel initially emits Loading, followed by repository value`() = runTest {
        underTest.currentPokemon.test {
            savedStateHandle[pokemonIdArg] = 0L
            savedStateHandle[pokemonNameArg] = ""
            Assert.assertEquals(
                PokemonDetailResult.Loading,
                awaitItem(),
            )
            Assert.assertEquals(
                PokemonDetailResult.Success(pokemonDetail),
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
