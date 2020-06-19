package com.example.echangeapp.domain.repositories

import com.example.echangeapp.domain.models.ExchangeRate

interface ExchangeRatesRepository {
    suspend fun listExchangeRates(): List<ExchangeRate>
}