import com.example.pokedex.pokemon.RedBlue
import com.example.pokedex.pokemon.Yellow
import com.google.gson.annotations.SerializedName

data class GenerationI(
    @SerializedName("red-blue") val red_blue: RedBlue,
    val yellow: Yellow
)