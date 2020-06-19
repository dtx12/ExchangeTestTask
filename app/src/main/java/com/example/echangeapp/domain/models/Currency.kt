package com.example.echangeapp.domain.models

enum class Currency {
    USD, EUR, GBP
}

fun Currency.amountTag(): String {
    return "PREFS_${name}"
}

fun Currency.symbol(): String {
    return when (this) {
        Currency.USD -> "$"
        Currency.EUR -> "€"
        Currency.GBP -> "£"
    }
}