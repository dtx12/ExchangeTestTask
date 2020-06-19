package com.example.echangeapp.presentation.ui.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.echangeapp.domain.usecases.ExchangeCurrencyUseCase
import com.example.echangeapp.domain.usecases.LoadExchangeRatesUseCase
import com.example.echangeapp.domain.usecases.ReadAvailableCurrencyAmountUseCase
import javax.inject.Inject

class ExchangeViewModelFactory @Inject constructor(
    private val exchangeCurrencyUseCase: ExchangeCurrencyUseCase,
    private val readAvailableCurrencyAmountUseCase: ReadAvailableCurrencyAmountUseCase,
    private val loadExchangeRatesUseCase: LoadExchangeRatesUseCase
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ExchangeViewModel::class.java)) {
            ExchangeViewModel(exchangeCurrencyUseCase, readAvailableCurrencyAmountUseCase, loadExchangeRatesUseCase) as T
        } else {
            throw IllegalStateException()
        }
    }
}