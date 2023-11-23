package com.example.pokedex.pokemon

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.PokemonBase
import com.example.pokedex.PokemonViewHolder
import com.example.pokedex.databinding.ItemPokemonBinding

class PokemonAdapter: RecyclerView.Adapter<PokemonViewHolder>() {
    var data:ArrayList<PokemonBase> = ArrayList()


    fun PokemonAdapter(basicData : ArrayList<PokemonBase>, context:Context){
        this.data = basicData
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, holder.itemView.context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Necesitas obtener el LifecycleScope del LifecycleOwner (usualmente tu Activity o Fragment)
        val lifecycleScope = (parent.context as LifecycleOwner).lifecycleScope
        return PokemonViewHolder(binding, lifecycleScope)
    }

    override fun getItemCount(): Int {
        return data.size
    }


}
