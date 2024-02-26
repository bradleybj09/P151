package com.iambenbradley.p151

import app.cash.turbine.test
import com.iambenbradley.p151.model.result.PokemonDetailResult
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.repository.PokemonRepository
import com.iambenbradley.p151.ui.list.ListViewModel
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

class ListViewModelTest {

    private val repository = object : PokemonRepository {
        override suspend fun updateSummaries() {}

        override val pokemonSummaries: Flow<PokemonSummaryResult>
            get() = flowOf(PokemonSummaryResult.Failure)

        override fun getPokemonDetail(id: Long): Flow<PokemonDetailResult> {
            TODO("Not yet implemented")
        }
    }

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var underTest: ListViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        underTest = ListViewModel(
            repository,
            testDispatcher,
        )
    }

    @Test
    fun `viewModel initially emits Loading, followed by repository value`() = runTest {
        underTest.pokemon.test {
            Assert.assertEquals(
                PokemonSummaryResult.Loading,
                awaitItem(),
            )
            Assert.assertEquals(
                PokemonSummaryResult.Failure,
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
