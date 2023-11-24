package com.example.covid19.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.model.Covid19Object
import com.example.covid19.repository.Covid19Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Covid19ViewModel(private val repository: Covid19Repository) : ViewModel() {

    // _covidData es privado y MutableStateFlow para cambios internos
    private val _covidData = MutableStateFlow<List<Covid19Object>?>(null)
    // covidData es público y de solo lectura para exponer el estado a la UI
    val covidData: StateFlow<List<Covid19Object>?> = _covidData.asStateFlow()

    // _isLoading es privado y MutableStateFlow para cambios internos
    private val _isLoading = MutableStateFlow(true)
    // isLoading es público y de solo lectura para exponer el estado a la UI
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // _errorMessage es privado y MutableStateFlow para cambios internos
    private val _errorMessage = MutableStateFlow<String?>(null)
    // errorMessage es público y de solo lectura para exponer el estado a la UI
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        getCovidData()
    }

    private fun getCovidData() {
        viewModelScope.launch {
            _isLoading.value = true // Actualiza el valor usando _isLoading
            try {
                val response = repository.getCovid19Data("mexico")
                if (response != null) {
                    _covidData.value = response // Actualiza el valor usando _covidData
                } else {
                    _errorMessage.value = "Error: Response is null" // Actualiza el valor usando _errorMessage
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error" // Actualiza el valor usando _errorMessage
            } finally {
                _isLoading.value = false // Actualiza el valor usando _isLoading
            }
        }
    }
}
