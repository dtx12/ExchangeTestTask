package com.example.echangeapp.domain.usecases

import com.example.echangeapp.domain.models.CurrencyAmount
import com.example.echangeapp.domain.models.ExchangeRate
import com.example.echangeapp.domain.repositories.AvailableCurrencyRepository
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ExchangeCurrencyUseCase @Inject constructor(private val availableCurrencyRepo: AvailableCurrencyRepository) {

    fun execute(enteredAmount: BigDecimal,
                yourCurrencyRate: ExchangeRate,
                requiredCurrencyRate: ExchangeRate): Boolean {
        val currencyLeft = availableCurrencyRepo.read(yourCurrencyRate.base)
        if (currencyLeft.amount < enteredAmount) {
            return false
        }
        val exchangedCurrency = currencyLeft.amount.minus(enteredAmount)
        availableCurrencyRepo.update(currencyLeft.copy(amount = exchangedCurrency))

        val targetCurrencyAmount = availableCurrencyRepo.read(requiredCurrencyRate.base)
        val receivedCurrency = enteredAmount
            .divide(
                requiredCurrencyRate.rates[yourCurrencyRate.base]
                    ?: BigDecimal.ONE, 2, RoundingMode.DOWN
            )
            .plus(targetCurrencyAmount.amount)

        availableCurrencyRepo.update(targetCurrencyAmount.copy(amount = receivedCurrency))

        return true
    }
}