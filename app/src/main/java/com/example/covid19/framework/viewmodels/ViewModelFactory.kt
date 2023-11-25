package com.example.covid19.framework.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covid19.data.repository.Covid19Repository

// Factory para crear instancias del ViewModel para inyectar dependencias en el ViewModel.
class Covid19ViewModelFactory(private val repository: Covid19Repository) : ViewModelProvider.Factory {

    // Esta función crea una instancia del ViewModel.
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica si el ViewModel solicitado es del tipo Covid19ViewModel.
        if (modelClass.isAssignableFrom(Covid19ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return Covid19ViewModel(repository) as T
        }

        // Si no, lanza una excepción indicando que no se reconoce el tipo de ViewModel.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
