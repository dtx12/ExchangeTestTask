package com.example.echangeapp.presentation.ui.exchange

data class ExchangeScreenModel(
    val yourCurrencies: List<ExchangeCardViewModel>,
    val targetCurrencies: List<ExchangeCardViewModel>,
    val title: String
)