package com.example.echangeapp.data.network

import com.example.echangeapp.data.models.ApiExchangeRate
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {

    @GET("latest/{base}")
    suspend fun loadExchangeRates(@Path("base") base: String): ApiExchangeRate
}