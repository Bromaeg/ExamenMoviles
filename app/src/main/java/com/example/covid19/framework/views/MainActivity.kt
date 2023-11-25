package com.example.covid19.framework.views

import android.os.Build
import com.example.covid19.framework.viewmodels.Covid19ViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.covid19.model.CovidCaseObject
import com.example.covid19.data.network.ApiClient
import com.example.covid19.data.repository.Covid19Repository
import com.example.covid19.ui.theme.Covid19Theme
import com.example.covid19.framework.viewmodels.Covid19ViewModelFactory

// La actividad principal de la aplicación.
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Covid19Theme {
                // Inicializa el ViewModel con un factory personalizado.
                val viewModel: Covid19ViewModel = viewModel(factory = Covid19ViewModelFactory(
                    Covid19Repository(ApiClient.service)
                ))

                // Observa los estados del ViewModel y actualiza la UI en consecuencia.
                val covidDataState = viewModel.covidData.collectAsState()
                val isLoadingState = viewModel.isLoading.collectAsState()
                val errorMessageState = viewModel.errorMessage.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Manejo de los diferentes estados de la aplicacion.
                    when {
                        isLoadingState.value -> {
                            // Muestra la vista de carga si los datos aun se estan cargando.
                            LoadingView()
                        }
                        errorMessageState.value?.isNotEmpty() == true -> {
                            // Muestra un mensaje de error si ocurre algun problema.
                            ErrorView(errorMessageState.value)
                        }
                        else -> {
                            // Si hay datos disponibles, muestra las estadisticas y la lista de datos.
                            covidDataState.value?.let { dataList ->
                                QuickStatistics(dataList)
                                CovidDataList(dataList, "Mexico")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickStatistics(data: List<CovidCaseObject>) {
    // Calcula y muestra estadisticas rapidas sobre los casos de COVID-19.
    val totalCases = data.sumOf { it.totalCases }
    val newCases = data.sumOf { it.newCases }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Estadísticas Rápidas",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Total de casos: $totalCases",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Casos nuevos: $newCases",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun LoadingView() {
    // Componente para mostrar durante la carga de datos.
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Cargando...", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun ErrorView(message: String?) {
    // Componente para mostrar en caso de error al cargar datos.
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Error: $message", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun CovidDataList(data: List<CovidCaseObject>, countryName: String) {
    // Componente para listar los datos de COVID-19.
    Column {
        Header(countryName)
        LazyColumn {
            items(data) { covidData ->
                CovidDataItem(covidData)
            }
        }
    }
}

@Composable
fun Header(countryName: String) {
    // Encabezado de la lista de datos.
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primaryContainer) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Datos históricos del Covid-19",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = countryName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun CovidDataItem(covidData: CovidCaseObject) {
    // Componente para mostrar un item individual de los datos de COVID-19.
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = covidData.date,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nuevos casos: ${covidData.newCases}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Total de casos: ${covidData.totalCases}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
