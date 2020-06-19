package com.example.echangeapp.domain.models

import java.math.BigDecimal

data class ExchangeRate(val base: Currency, val rates: Map<Currency, BigDecimal>) {
    companion object {
        fun empty(base: Currency): ExchangeRate {
            return ExchangeRate(base, Currency.values().map { it to BigDecimal(1) }.toMap())
        }
    }
}