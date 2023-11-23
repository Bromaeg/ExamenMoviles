package com.example.pokedex.pokemon
import com.google.gson.annotations.SerializedName

data class GenerationIii(
    val emerald: Emerald,
    @SerializedName("firered-leafgreen") val fireredLeafgreen: FireredLeafgreen,
    @SerializedName("ruby-sapphire") val rubySapphire: RubySapphire
)
