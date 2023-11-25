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

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Covid19Theme {
                // Initialize the ViewModel with a custom factory.
                val viewModel: Covid19ViewModel = viewModel(factory = Covid19ViewModelFactory(
                    Covid19Repository(ApiClient.service)
                )
                )

                // Observe the ViewModel states.
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
                            // If there is data, display the quick statistics and list
                            covidDataState.value?.let { dataList ->
                                QuickStatistics(dataList) // Make sure this is defined and implemented correctly
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
    // Calculate the total and new cases
    val totalCases = data.sumOf { it.totalCases }
    val newCases = data.sumOf { it.newCases }

    // Display the statistics in a Card or another Composable
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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Loading...", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun ErrorView(message: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Error: $message", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun CovidDataList(data: List<CovidCaseObject>, countryName: String) {
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
                    text = "New Cases: ${covidData.newCases}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Total Cases: ${covidData.totalCases}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
