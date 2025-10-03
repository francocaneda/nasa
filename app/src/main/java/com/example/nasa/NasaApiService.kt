package com.example.nasa  // mismo package que MainActivity y ApodResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {

    @GET("planetary/apod")
    fun getApod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Call<ApodResponse>
}

