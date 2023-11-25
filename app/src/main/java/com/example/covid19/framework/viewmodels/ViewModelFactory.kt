package com.example.covid19.framework.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covid19.data.repository.Covid19Repository

class Covid19ViewModelFactory(private val repository: Covid19Repository) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Covid19ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return Covid19ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
