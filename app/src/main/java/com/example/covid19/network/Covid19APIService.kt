package com.example.covid19.network

import com.example.covid19.model.Covid19Object
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient




interface ApiService {
    @GET("/v1/covid19")
    suspend fun getCovidData(
        @Query("country") country: String
    ): Response<List<Covid19Object>>
}

object ApiClient {
    private const val BASE_URL = "https://api.api-ninjas.com/v1/covid19/"
    private const val API_KEY = "aQ5yvAPcTc3rz+lpMz2P4g==taw6i4mHSicpgZSP" // Asegúrate de que tu API key sea correcta

    // Crea el interceptor de logging para ver las solicitudes y respuestas en el log
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Cambia a Level.BASIC si quieres menos detalles
    }

    // Crea un cliente de OkHttpClient y añade tus interceptores
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("X-Api-Key", API_KEY)
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(loggingInterceptor) // Añade el logging interceptor aquí
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

