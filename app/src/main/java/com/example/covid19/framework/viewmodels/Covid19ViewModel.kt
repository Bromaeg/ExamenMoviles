package com.example.covid19.framework.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.model.CovidCaseObject
import com.example.covid19.data.repository.Covid19Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ViewModel para manejar la lógica relacionada con los datos de la API.
@RequiresApi(Build.VERSION_CODES.O)
class Covid19ViewModel(private val repository: Covid19Repository) : ViewModel() {

    // Estado interno para manejar los datos de COVID.
    private val _covidData = MutableStateFlow<List<CovidCaseObject>?>(null)
    val covidData: StateFlow<List<CovidCaseObject>?> = _covidData.asStateFlow()

    // Estados para manejar el total de casos y los nuevos casos.
    private val _totalCases = MutableStateFlow<Int?>(null)
    private val _newCases = MutableStateFlow<Int?>(null)

    // Estado para manejar la carga de los datos.
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para manejar mensajes de error.
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Inicializa la obtención de datos al crear el ViewModel.
    init {
        getCovidData()
    }

    // Función para obtener datos de COVID desde el repositorio.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCovidData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getCovid19Data("mexico")
                response?.let { dataList ->
                    // Calculamos las estadísticas totales y nuevas.
                    _totalCases.value = dataList.sumOf { it.cases?.values?.sumOf { case -> case.total } ?: 0 }
                    _newCases.value = dataList.sumOf { it.cases?.values?.sumOf { case -> case.new } ?: 0 }

                    // Transforma los datos en una lista de CovidCaseObject ordenada por fecha descendente.
                    val casesList = dataList.flatMap { data ->
                        data.cases?.map { (date, case) ->
                            CovidCaseObject(
                                country = data.country,
                                region = data.region ?: "N/A",
                                date = date,
                                totalCases = case.total,
                                newCases = case.new
                            )
                        } ?: listOf()
                    }.sortedByDescending {
                        LocalDate.parse(it.date, DateTimeFormatter.ISO_LOCAL_DATE)
                    }
                    _covidData.value = casesList
                }
            } catch (e: Exception) {
                // Manejo de errores.
                _errorMessage.value = e.message ?: "Unknown error"
            } finally {
                // Indica que la carga de datos ha terminado.
                _isLoading.value = false
            }
        }
    }
}
