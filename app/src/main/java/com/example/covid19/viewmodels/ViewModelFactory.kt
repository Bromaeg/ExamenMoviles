package com.example.covid19.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covid19.repository.Covid19Repository

class Covid19ViewModelFactory(private val repository: Covid19Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Covid19ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return Covid19ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
