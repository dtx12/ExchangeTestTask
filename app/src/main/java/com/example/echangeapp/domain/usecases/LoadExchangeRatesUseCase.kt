package com.example.echangeapp.domain.usecases

import com.example.echangeapp.domain.models.ExchangeRate
import com.example.echangeapp.domain.repositories.ExchangeRatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadExchangeRatesUseCase @Inject constructor(private val exchangeRatesRepository: ExchangeRatesRepository) {

    suspend fun execute(): List<ExchangeRate> {
        return withContext(Dispatchers.IO) {
            exchangeRatesRepository.listExchangeRates()
        }
    }
}