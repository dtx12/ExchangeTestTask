package com.example.echangeapp.domain.usecases

import com.example.echangeapp.domain.models.Currency
import com.example.echangeapp.domain.models.CurrencyAmount
import com.example.echangeapp.domain.repositories.AvailableCurrencyRepository
import javax.inject.Inject

class ReadAvailableCurrencyAmountUseCase @Inject constructor(private val availableCurrencyRepo: AvailableCurrencyRepository) {

    fun execute(base: Currency): CurrencyAmount {
        return availableCurrencyRepo.read(base)
    }
}