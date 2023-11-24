package com.example.covid19.views
import com.example.covid19.viewmodels.Covid19ViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.covid19.ui.theme.Covid19Theme
import com.example.covid19.model.Covid19Object
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import com.example.covid19.network.ApiClient
import com.example.covid19.repository.Covid19Repository
import com.example.covid19.viewmodels.Covid19ViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Covid19Theme {
                // Inicializa el viewModel con la factory personalizada.
                val viewModel: Covid19ViewModel = viewModel(factory = Covid19ViewModelFactory(
                    Covid19Repository(ApiClient.service)
                )
                )

                // Observa los estados del viewModel.
                val covidDataState = viewModel.covidData.collectAsState()
                val isLoadingState = viewModel.isLoading.collectAsState()
                val errorMessageState = viewModel.errorMessage.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when {
                        isLoadingState.value -> {
                            LoadingView()
                        }
                        errorMessageState.value?.isNotEmpty() == true -> {
                            ErrorView(errorMessageState.value)
                        }
                        else -> {
                            CovidDataList(covidDataState.value ?: emptyList())
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun LoadingView() {
    // Mostrar algún indicador de carga
    Text("Loading...")
}

@Composable
fun ErrorView(message: String?) {
    // Mostrar un mensaje de error
    Text("Error: $message")
}

@Composable
fun CovidDataList(data: List<Covid19Object>) {
    // Usa LazyColumn para manejar listas grandes eficientemente
    LazyColumn {
        items(data) { covidData ->
            CovidDataItem(covidData)
        }
    }
}

@Composable
fun CovidDataItem(covidData: Covid19Object) {
    // Usa Card para cada elemento para un mejor aspecto visual
    Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Country: ${covidData.country}")
            Text(text = "Region: ${covidData.region ?: "Not specified"}")
            Text(text = "Cases_ ${covidData.cases}")
            Text(text = "Total Cases_ ${covidData.totalCases}")
            Text(text = "Date: ${covidData.date}")

        }
    }
}

