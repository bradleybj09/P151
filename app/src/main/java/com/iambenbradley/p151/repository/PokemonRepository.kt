package com.iambenbradley.p151.repository

import com.iambenbradley.p151.data.api.PokeService
import com.iambenbradley.p151.model.PokeMapper
import com.iambenbradley.p151.model.result.PokemonDetailResult
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.util.DefaultDispatcher
import com.iambenbradley.p151.util.IoDispatcher
import com.iambenbradley.p151.util.NetworkMonitor
import com.iambenbradley.p151.util.getPokemonId
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@Module
@InstallIn(SingletonComponent::class)
interface PokemonRepositoryBindingModule {

    @Binds
    @Singleton
    fun bindPokemonRepository(impl: PokemonRepositoryImpl): PokemonRepository
}

interface PokemonRepository {

    /**
     * Call to update flow of all pokemon with a list of the original 151
     */
    suspend fun updateSummaries()

    /**
     * Flow of simple results for original 151 pokemon, stays hot
     */
    val pokemonSummaries: Flow<PokemonSummaryResult>

    /**
     * Call to receive a more complicated payload for an individual pokemon
     */
    fun getPokemonDetail(id: Long): Flow<PokemonDetailResult>
}

class PokemonRepositoryImpl @Inject constructor(
    private val pokeMapper: PokeMapper,
    private val pokeService: PokeService,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val networkMonitor: NetworkMonitor,
) : PokemonRepository {

    private val _summaries = MutableSharedFlow<PokemonSummaryResult>(
        replay = 1,
    )

    override suspend fun updateSummaries() {
        withContext(ioDispatcher) {
            if (networkMonitor.networkAvailable.first()) {
                withTimeoutHandling(
                    onTimeout = { _summaries.emit(PokemonSummaryResult.Failure) },
                ) {
                    _summaries.emit(
                        pokeService.getAllOneFiftyOne().let { response ->
                            if (response.isSuccessful) {
                                // in such a small thing, it's silly to switch dispatchers here, but in a real
                                // complex situation with big mapping jobs, you'd do it. So I did it here.
                                withContext(defaultDispatcher) {
                                    PokemonSummaryResult.Success(
                                        summaries = response.body()?.results?.map {
                                            pokeMapper.serialToDomainSummary(it)
                                        } ?: emptyList(),
                                    )
                                }
                            } else {
                                PokemonSummaryResult.Failure
                            }
                        },
                    )
                }
            } else {
                _summaries.emit(PokemonSummaryResult.Failure)
            }
        }
    }

    override val pokemonSummaries: Flow<PokemonSummaryResult> = _summaries

    override fun getPokemonDetail(id: Long): Flow<PokemonDetailResult> = flow {
        if (networkMonitor.networkAvailable.first()) {
            withTimeoutHandling(
                onTimeout = { emit(PokemonDetailResult.Failure) },
            ) {
                val pokemon = pokeService.getPokemon(pokemonId = id).let { response ->
                    if (response.isSuccessful) {
                        response.body()!!
                    } else {
                        emit(PokemonDetailResult.Failure)
                        return@withTimeoutHandling
                    }
                }

                val species = pokeService.getSpecies(pokemonId = id).let { response ->
                    if (response.isSuccessful) {
                        response.body()!!
                    } else {
                        emit(PokemonDetailResult.Failure)
                        return@withTimeoutHandling
                    }
                }

                val evolutionChain = species
                    .evolutionChain
                    .url.getPokemonId()
                    ?.let { evolutionChainId ->
                        pokeService.getEvolutionChain(
                            evolutionChainId = evolutionChainId
                        ).let { response ->
                            if (response.isSuccessful) {
                                response.body()!!
                            } else {
                                emit(PokemonDetailResult.Failure)
                                return@withTimeoutHandling
                            }
                        }
                    } ?: run {
                    emit(PokemonDetailResult.Failure)
                    return@withTimeoutHandling
                }

                val pokeData = pokeMapper.constructPokeData(
                    pokemon = pokemon,
                    species = species,
                    evolutionChain = evolutionChain,
                )
                emit(PokemonDetailResult.Success(pokeData))
            }
        } else {
            emit(PokemonDetailResult.Failure)
        }
    }.flowOn(ioDispatcher)

    private suspend fun withTimeoutHandling(
        onTimeout: suspend () -> Unit,
        block: suspend () -> Unit,
    ) {
        try {
            block()
        } catch (e: SocketTimeoutException) {
            onTimeout()
        }
    }
}
