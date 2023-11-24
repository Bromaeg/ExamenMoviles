package com.example.covid19.model

import Covid19ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class Covid19ViewModelFactory(private val repository: Covid19Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Covid19ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return Covid19ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
