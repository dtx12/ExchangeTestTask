package com.example.echangeapp.data.repositories

import com.example.echangeapp.data.network.ExchangeRateApi
import com.example.echangeapp.domain.models.Currency
import com.example.echangeapp.domain.models.ExchangeRate
import com.example.echangeapp.domain.repositories.ExchangeRatesRepository
import java.math.BigDecimal
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi
) : ExchangeRatesRepository {

    override suspend fun listExchangeRates(): List<ExchangeRate> {
        return Currency
            .values()
            .map { api.loadExchangeRates(it.name) }
            .map {
                ExchangeRate(
                    Currency.valueOf(it.base),
                    it
                        .rates
                        .filter { entry -> Currency.values().map { value -> value.name }.contains(entry.key) }
                        .map { entry -> Currency.valueOf(entry.key) to BigDecimal(entry.value) }.toMap()
                )
            }
    }
}