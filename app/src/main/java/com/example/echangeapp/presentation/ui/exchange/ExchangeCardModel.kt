package com.example.echangeapp.presentation.ui.exchange

import com.example.echangeapp.domain.models.Currency
import com.example.echangeapp.domain.models.CurrencyAmount
import java.io.Serializable
import java.math.BigDecimal

data class ExchangeCardViewModel(
    val yourCurrency: CurrencyAmount,
    val rate: BigDecimal,
    val requiredCurrency: Currency,
    val selected: Boolean,
    val type: ExchangeCardType,
    val enteredAmount: String
) : Serializable

enum class ExchangeCardType {
    FROM, TO
}

fun ExchangeCardType.sign(): String {
    return when (this) {
        ExchangeCardType.FROM -> "-"
        ExchangeCardType.TO -> "+"
    }
}