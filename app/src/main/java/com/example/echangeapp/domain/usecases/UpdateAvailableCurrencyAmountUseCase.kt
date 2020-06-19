package com.example.echangeapp.domain.usecases

import com.example.echangeapp.domain.models.CurrencyAmount
import com.example.echangeapp.domain.repositories.AvailableCurrencyRepository
import javax.inject.Inject

class UpdateAvailableCurrencyAmountUseCase @Inject constructor(private val availableCurrencyRepo: AvailableCurrencyRepository) {

    fun execute(amount: CurrencyAmount) {
        availableCurrencyRepo.update(amount)
    }
}