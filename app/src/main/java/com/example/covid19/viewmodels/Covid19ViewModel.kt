package com.example.covid19.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.model.CovidCase
import com.example.covid19.model.CovidCaseObject
import com.example.covid19.model.Covid19Object
import com.example.covid19.repository.Covid19Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Covid19ViewModel(private val repository: Covid19Repository) : ViewModel() {

    private val _covidData = MutableStateFlow<List<CovidCaseObject>?>(null)
    val covidData: StateFlow<List<CovidCaseObject>?> = _covidData.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        getCovidData()
    }

    private fun getCovidData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getCovid19Data("mexico")
                response?.let { dataList ->
                    val casesList = mutableListOf<CovidCaseObject>()
                    dataList.forEach { data ->
                        data.cases?.forEach { (date, case) ->
                            val covidCaseObject = CovidCaseObject(
                                country = data.country,
                                region = data.region ?: "N/A", // Handle possible null region
                                date = date,
                                totalCases = case.total,
                                newCases = case.new
                            )
                            casesList.add(covidCaseObject)
                        }
                    }
                    _covidData.value = casesList
                } ?: run {
                    _errorMessage.value = "Error: Response is null"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
