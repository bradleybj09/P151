package com.iambenbradley.p151.data.api

import com.iambenbradley.p151.model.serial.AllPokemonResult
import com.iambenbradley.p151.model.serial.EvolutionChain
import com.iambenbradley.p151.model.serial.PokemonDetail
import com.iambenbradley.p151.model.serial.PokemonSummary
import com.iambenbradley.p151.model.serial.SpeciesDetail
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

interface PokeService {

    @GET("pokemon?limit=151")
    suspend fun getAllOneFiftyOne(): Response<AllPokemonResult>

    @GET("pokemon/{id}/")
    suspend fun getPokemon(@Path("id") pokemonId: Long): Response<PokemonDetail>

    @GET("pokemon-species/{id}/")
    suspend fun getSpecies(@Path("id") pokemonId: Long): Response<SpeciesDetail>

    @GET("evolution0chain/{id}/")
    suspend fun getEvolutionChain(@Path("id") evolutionChainId: Long): Response<EvolutionChain>
}

@Module
@InstallIn(SingletonComponent::class)
class PokeServiceProviderModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Suppress("JSON_FORMAT_REDUNDANT")
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }.asConverterFactory("application/json".toMediaType()),
        )
        .build()

    @Provides
    @Singleton
    fun providePokeService(retrofit: Retrofit): PokeService = retrofit
        .create(PokeService::class.java)
}