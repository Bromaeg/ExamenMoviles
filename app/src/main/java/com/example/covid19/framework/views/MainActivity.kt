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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.covid19.model.CovidCaseObject
import com.example.covid19.data.network.ApiClient
import com.example.covid19.data.repository.Covid19Repository
import com.example.covid19.ui.theme.Covid19Theme
import com.example.covid19.framework.viewmodels.Covid19ViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.data.LineData
import androidx.compose.runtime.remember


// La actividad principal de la aplicación.
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Covid19Theme {
                // ViewModel inicializado aquí con su factory
                val viewModel: Covid19ViewModel = viewModel(factory = Covid19ViewModelFactory(
                    Covid19Repository(ApiClient.service)
                ))

                val covidDataState by viewModel.covidData.collectAsState()
                val isLoadingState by viewModel.isLoading.collectAsState()
                val errorMessageState by viewModel.errorMessage.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when {
                        isLoadingState -> LoadingView()
                        errorMessageState?.isNotEmpty() == true -> ErrorView(errorMessageState)
                        else -> covidDataState?.let { dataList ->
                            if (dataList.isNotEmpty()) {
                                Column {
                                    QuickStatistics(dataList) // Pasamos toda la lista para estadísticas
                                    CovidChartCard(viewModel) // Tarjeta con la gráfica
                                    CovidDataList(dataList) // Lista de datos históricos
                                }
                            } else {
                                NoDataView()
                            }
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun NoDataView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No hay datos disponibles", style = MaterialTheme.typography.headlineMedium)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CovidChart(viewModel: Covid19ViewModel) {
    val context = LocalContext.current
    val chartData = remember { viewModel.getCovidChartData()}

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        factory = { ctx ->
            LineChart(ctx).apply {
                description.isEnabled = false
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.valueFormatter = IndexAxisValueFormatter(viewModel.getDateLabels())
            }
        },
        update = { chart ->
            val lineDataSet = LineDataSet(chartData, "Total Cases").apply {
                // Define los colores directamente
                color = Color(0xFF673AB7).toArgb()
                valueTextColor = Color(0xFF7B1FA2).toArgb()
                lineWidth = 2f
                setDrawValues(false)
                setDrawCircles(false)
                setDrawCircleHole(false)
            }
            chart.data = LineData(lineDataSet)
            chart.invalidate()
        }
    )
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CovidDataList(data: List<CovidCaseObject>) {
    // Componente para listar los datos de COVID-19.
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 4.dp
        ) {

        }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CovidChartCard(viewModel: Covid19ViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        CovidChart(viewModel)
    }
}