package com.iambenbradley.p151.repository

import android.util.Log
import com.iambenbradley.p151.data.api.PokeService
import com.iambenbradley.p151.model.PokeMapper
import com.iambenbradley.p151.model.result.PokemonDetailResult
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.util.ApplicationProcessScope
import com.iambenbradley.p151.util.DefaultDispatcher
import com.iambenbradley.p151.util.IoDispatcher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PokemonRepositoryBindingModule {

    @Binds
    @Singleton
    fun bindPokemonRepository(impl: PokemonRepositoryImpl): PokemonRepository
}

interface PokemonRepository {
    fun updateSummaries()
    val pokemonSummaries: Flow<PokemonSummaryResult>
    fun getPokemonDetail(id: Long): Flow<PokemonDetailResult>
}

class PokemonRepositoryImpl @Inject constructor(
    private val pokeMapper: PokeMapper,
    private val pokeService: PokeService,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationProcessScope private val coroutineScope: CoroutineScope,
) : PokemonRepository {

    private val _summaries = MutableSharedFlow<PokemonSummaryResult>(
        replay = 1,
    )

    init {
        updateSummaries()
    }

    override fun updateSummaries() {
        coroutineScope.launch(ioDispatcher) {
            _summaries.emit(
                pokeService.getAllOneFiftyOne().let { response ->
                    Log.wtf("bbradley", "$response")
                    if (response.isSuccessful) {
                        // in such a small thing, it's silly to switch dispatchers here, but in a real
                        // complex situation with big mapping jobs, you'd do it. So I did it here.
                        withContext(defaultDispatcher) {
                            PokemonSummaryResult.Success(
                                summaries = response.body()?.results?.map {
                                    pokeMapper.serialToDomainSummary(it)
                                } ?: emptyList()
                            )
                        }
                    } else {
                        PokemonSummaryResult.Failure
                    }
                }
            )
        }

    }

    override val pokemonSummaries: Flow<PokemonSummaryResult> = _summaries

    override fun getPokemonDetail(id: Long): Flow<PokemonDetailResult> = flow {
        val pokemon = pokeService.getPokemon(pokemonId = id)
            .takeUnless { !it.isSuccessful }
            ?.body()
            ?: run {
                emit(PokemonDetailResult.Failure)
                return@flow
            }

        val species = pokeService.getSpecies(pokemonId = id)
            .takeUnless { !it.isSuccessful }
            ?.body()
            ?: run {
                emit(PokemonDetailResult.Failure)
                return@flow
            }

        val evolutionChain = species
            .evolutionChain
            .url
            .substringBeforeLast('/')
            .toLongOrNull()
            ?.let { evolutionChainId ->
                pokeService.getEvolutionChain(evolutionChainId = evolutionChainId)
            }?.takeUnless { !it.isSuccessful }
            ?.body()
            ?: run {
                emit(PokemonDetailResult.Failure)
                return@flow
            }
        val pokeData = pokeMapper.constructPokeData(
            pokemon = pokemon,
            species = species,
            evolutionChain = evolutionChain,
        )
        emit(PokemonDetailResult.Success(pokeData))
    }.flowOn(ioDispatcher)
}