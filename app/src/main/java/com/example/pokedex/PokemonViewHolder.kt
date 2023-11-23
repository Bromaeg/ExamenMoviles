package com.example.pokedex

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.pokedex.databinding.ItemPokemonBinding
import com.example.pokedex.pokemon.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonViewHolder(
    private val binding: ItemPokemonBinding,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PokemonBase, context: Context) {
        binding.TVName.text = item.name
        // Cancelamos cualquier corutina anterior que podría estar cargando una imagen antigua.
        binding.imageView.setImageDrawable(null)
        lifecycleScope.launch {
            getPokemonInfo(item.url, binding.imageView, context)
        }
    }
}


private fun getPokemonInfo(url: String, imageView: ImageView, context: Context) {
    val regex = Regex("pokemon/(\\d+)/")
    val pokemonIdMatch = regex.find(url)?.groups?.get(1)?.value

    val pokemonNumber = pokemonIdMatch?.toIntOrNull()

    if (pokemonNumber != null) {
        CoroutineScope(Dispatchers.IO).launch {
            val pokemonRepository = PokemonRepository()
            val result: Pokemon? = pokemonRepository.getPokemonInfo(pokemonNumber)
            CoroutineScope(Dispatchers.Main).launch {
                result?.sprites?.other?.officialArtwork?.front_default?.let { urlImage ->
                    val requestOptions = RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .fitCenter()
                        .priority(Priority.HIGH)

                    Glide.with(context).load(urlImage)
                        .apply(requestOptions)
                        .into(imageView)
                } ?: run {
                    Log.e("PokemonViewHolder", "URL de imagen no encontrada")
                }
            }
        }
    } else {
        Log.e("PokemonViewHolder", "La URL no contiene un ID de Pokémon válido: $url")
    }
}


