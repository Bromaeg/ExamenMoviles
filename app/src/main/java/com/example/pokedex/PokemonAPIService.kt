package com.example.pokedex

import com.example.pokedex.pokemon.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPIService {

    //https://pokeapi.co/api/v2/pokemon/?limit=1279
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit:Int
    ): PokedexObject

    //https://pokeapi.co/api/v2/pokemon/{number_pokemon}/
    @GET("pokemon/{numberPokemon}")
    suspend fun getPokemonInfo(
        @Path("numberPokemon") numberPokemon:Int
    ): Pokemon
}