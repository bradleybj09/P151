package com.iambenbradley.p151

import app.cash.turbine.test
import com.iambenbradley.p151.data.api.PokeService
import com.iambenbradley.p151.model.PokeMapper
import com.iambenbradley.p151.model.domain.PokemonDetail as DomainPokemonDetail
import com.iambenbradley.p151.model.domain.PokemonSummary as DomainPokemonSummary
import com.iambenbradley.p151.model.result.PokemonDetailResult
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.model.serial.AllPokemonResult
import com.iambenbradley.p151.model.serial.EvolutionChain
import com.iambenbradley.p151.model.serial.EvolutionChainReference
import com.iambenbradley.p151.model.serial.PokemonDetail
import com.iambenbradley.p151.model.serial.PokemonSummary
import com.iambenbradley.p151.model.serial.SpeciesDetail
import com.iambenbradley.p151.repository.PokemonRepository
import com.iambenbradley.p151.repository.PokemonRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PokemonRepositoryTest {

    private lateinit var underTest: PokemonRepository
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var pokeService: PokeService

    @MockK
    private lateinit var pokeMapper: PokeMapper

    @MockK
    private lateinit var species: SpeciesDetail

    @MockK
    private lateinit var pokemonDetail: PokemonDetail

    @MockK
    private lateinit var evoChain: EvolutionChain

    @MockK
    private lateinit var evoChainReference: EvolutionChainReference

    @MockK
    private lateinit var domainPokemonDetail: DomainPokemonDetail

    @MockK
    private lateinit var aPokemon: PokemonSummary

    @MockK
    private lateinit var aDomainPokemon: DomainPokemonSummary

    private val responseErrorBody = "{\"thing\":[\"bad\"]}"
        .toResponseBody("application/json".toMediaTypeOrNull())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        underTest = PokemonRepositoryImpl(
            pokeMapper = pokeMapper,
            pokeService = pokeService,
            defaultDispatcher = testDispatcher,
            ioDispatcher = testDispatcher,
        )
        coEvery { pokeMapper.serialToDomainSummary(any()) } returns(aDomainPokemon)
        coEvery { species.evolutionChain } returns(evoChainReference)
        coEvery { evoChainReference.url } returns("/0/")
    }

    @Test
    fun `update summaries emits a success on successful api`() = runTest {
        coEvery { pokeService.getAllOneFiftyOne() } returns(
            Response.success(AllPokemonResult(count = 1, results = listOf(aPokemon)))
            )
        underTest.pokemonSummaries.test {
            underTest.updateSummaries()
            Assert.assertEquals(
                PokemonSummaryResult.Success(listOf(aDomainPokemon)),
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `update summaries emits a failure on unsuccessful api`() = runTest {
        coEvery { pokeService.getAllOneFiftyOne() } returns(
            Response.error(404, responseErrorBody)
            )
        underTest.pokemonSummaries.test {
            underTest.updateSummaries()
            Assert.assertEquals(
                PokemonSummaryResult.Failure,
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPokemonDetail emits failure on any failed call`() = runTest {
        coEvery { pokeService.getSpecies(any()) } returns(
            Response.success(species)
            )
        coEvery { pokeService.getEvolutionChain(any()) } returns(
            Response.success(evoChain)
            )
        coEvery { pokeService.getPokemon(any()) } returns(
            Response.error(404, responseErrorBody)
            )
        underTest.getPokemonDetail(0).test {
            Assert.assertEquals(
                "getDetail succeeded despite failing pokemon",
                PokemonDetailResult.Failure,
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }

        coEvery { pokeService.getSpecies(any()) } returns(
            Response.error(404, responseErrorBody)
            )
        coEvery { pokeService.getEvolutionChain(any()) } returns(
            Response.success(evoChain)
            )
        coEvery { pokeService.getPokemon(any()) } returns(
            Response.success(pokemonDetail)
            )
        underTest.getPokemonDetail(0).test {
            Assert.assertEquals(
                "getDetail succeeded despite failing species",
                PokemonDetailResult.Failure,
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }

        coEvery { pokeService.getSpecies(any()) } returns(
            Response.success(species)
            )
        coEvery { pokeService.getEvolutionChain(any()) } returns(
            Response.error(404, responseErrorBody)
            )
        coEvery { pokeService.getPokemon(any()) } returns(
            Response.success(pokemonDetail)
            )
        underTest.getPokemonDetail(0).test {
            Assert.assertEquals(
                "getDetail succeeded despite failing evoChain",
                PokemonDetailResult.Failure,
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPokemonDetail emits detail on successful calls`() = runTest {
        coEvery { pokeService.getSpecies(any()) } returns(
            Response.success(species)
            )
        coEvery { pokeService.getEvolutionChain(any()) } returns(
            Response.success(evoChain)
            )
        coEvery { pokeService.getPokemon(any()) } returns(
            Response.success(pokemonDetail)
            )
        coEvery { pokeMapper.constructPokeData(any(), any(), any()) } returns(
            domainPokemonDetail
            )
        underTest.getPokemonDetail(0).test {
            Assert.assertEquals(
                PokemonDetailResult.Success(domainPokemonDetail),
                awaitItem(),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
