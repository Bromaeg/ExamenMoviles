package com.example.covid19.model

import com.google.gson.annotations.SerializedName

// Modelo para representar un caso individual
data class CovidCase(
    @SerializedName("total") val total: Int,
    @SerializedName("new") val new: Int
)
//Objeto
data class Covid19Object(
    @SerializedName("country") val country: String,
    @SerializedName("region") val region: String?,
    @SerializedName("cases") val cases: Map<String, CovidCase>
)

data class CovidCaseObject(
    val country: String,
    val region: String?,
    val date: String,
    val totalCases: Int,
    val newCases: Int
)
