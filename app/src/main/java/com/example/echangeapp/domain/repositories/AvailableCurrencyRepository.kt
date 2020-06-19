package com.example.echangeapp.domain.repositories

import com.example.echangeapp.domain.models.Currency
import com.example.echangeapp.domain.models.CurrencyAmount

interface AvailableCurrencyRepository {
    fun read(base: Currency): CurrencyAmount
    fun update(amount: CurrencyAmount)
}