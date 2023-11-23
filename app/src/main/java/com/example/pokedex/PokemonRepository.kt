package com.example.pokedex

import com.example.pokedex.pokemon.Pokemon

class PokemonRepository {
    private val api: PokemonAPIService = NetworkModuleDI.retrofit.create(PokemonAPIService::class.java)

    suspend fun getPokemonList(limit: Int): PokedexObject? {
        return try {
            api.getPokemonList(limit)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getPokemonInfo(numberPokemon: Int): Pokemon? {
        return try {
            api.getPokemonInfo(numberPokemon)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
