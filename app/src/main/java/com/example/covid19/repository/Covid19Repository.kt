package com.example.covid19.repository

import com.example.covid19.network.ApiService
import com.example.covid19.model.Covid19Object


// Covid19Repository.kt
class Covid19Repository(private val apiService: ApiService) {
   suspend fun getCovid19Data(country: String): List<Covid19Object> {
      // Realiza la llamada a la API en un contexto de IO para operaciones de red
      val response = apiService.getCovidData("mexico")
      if (response.isSuccessful) {
         // Si la respuesta es exitosa, devuelve el cuerpo de la respuesta
         return response.body() ?: emptyList()
      } else {
         // Maneja el caso de error
         throw Exception("Error: ${response.code()} ${response.message()}")
      }
   }
}

