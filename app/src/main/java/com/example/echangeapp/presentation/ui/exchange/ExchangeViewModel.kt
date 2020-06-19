package com.example.echangeapp.presentation.ui.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echangeapp.common.OneTimeEvent
import com.example.echangeapp.domain.CurrencyFormatter
import com.example.echangeapp.domain.models.Currency
import com.example.echangeapp.domain.models.ExchangeRate
import com.example.echangeapp.domain.models.symbol
import com.example.echangeapp.domain.usecases.ExchangeCurrencyUseCase
import com.example.echangeapp.domain.usecases.LoadExchangeRatesUseCase
import com.example.echangeapp.domain.usecases.ReadAvailableCurrencyAmountUseCase
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.concurrent.fixedRateTimer

class ExchangeViewModel(
    private val exchangeCurrencyUseCase: ExchangeCurrencyUseCase,
    private val readAvailableCurrencyAmountUseCase: ReadAvailableCurrencyAmountUseCase,
    private val loadExchangeRatesUseCase: LoadExchangeRatesUseCase
): ViewModel() {

    private var yourCurrency: Currency = Currency.USD
    private var requiredCurrency: Currency = Currency.USD
    private var enteredAmount: BigDecimal = BigDecimal.ZERO

    private val rates = mutableMapOf<Currency, ExchangeRate>()

    private val _screenViewModel = MutableLiveData<ExchangeScreenModel>()
    val screenModel: LiveData<ExchangeScreenModel> = _screenViewModel

    private val _exchangeSuccessEvent = MutableLiveData<OneTimeEvent<Unit>>()
    val exchangeSuccessEvent: LiveData<OneTimeEvent<Unit>> = _exchangeSuccessEvent

    private val _exchangeFailEvent = MutableLiveData<OneTimeEvent<Unit>>()
    val exchangeFailEvent: LiveData<OneTimeEvent<Unit>> = _exchangeFailEvent

    private val periodicUpdatesTimer = fixedRateTimer(startAt = Date(), period = 30_000) {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            val result = loadExchangeRatesUseCase.execute()
            for (rate in result) {
                rates[rate.base] = rate
            }
            reloadScreen()
        }
    }

    fun onChangeEnteredAmount(amount: String, cardViewModel: ExchangeCardViewModel) {
        var result = if (amount.isEmpty() || amount == ".") BigDecimal.ZERO else BigDecimal(amount)
        if (cardViewModel.type == ExchangeCardType.TO) {
            result = result.multiply(cardViewModel.rate)
        }
        enteredAmount = result
        reloadScreen()
    }

    fun onYourCurrencyChanged(
        currency: Currency
    ) {
        yourCurrency = currency
        reloadScreen()
    }

    fun onRequiredCurrencyChanged(
        currency: Currency
    ) {
        requiredCurrency = currency
        reloadScreen()
    }

    fun exchangeCurrency() {
        if (exchangeCurrencyUseCase.execute(enteredAmount, rates[yourCurrency]!!, rates[requiredCurrency]!!)) {
            enteredAmount = BigDecimal.ZERO
            _exchangeSuccessEvent.value = OneTimeEvent(Unit)
        } else {
            _exchangeFailEvent.value = OneTimeEvent(Unit)
        }
        reloadScreen()
    }

    private fun reloadScreen() {
        val yourCurrencyCards = Currency.values().map {

            val fromRate = rates[it]?.rates?.get(requiredCurrency) ?: BigDecimal.ONE

            ExchangeCardViewModel(
                readAvailableCurrencyAmountUseCase.execute(it),
                fromRate,
                requiredCurrency,
                it == yourCurrency,
                ExchangeCardType.FROM,
                CurrencyFormatter.formatCurrency(enteredAmount)
            )
        }

        val requiredCurrencyCards = Currency.values().map {

            val toRate = rates[it]?.rates?.get(yourCurrency)
                ?: BigDecimal.ONE

            ExchangeCardViewModel(
                readAvailableCurrencyAmountUseCase.execute(it),
                toRate,
                yourCurrency,
                it == requiredCurrency,
                ExchangeCardType.TO,
                CurrencyFormatter.formatCurrency(enteredAmount.divide(toRate, 2, RoundingMode.DOWN))
            )
        }

        val currentRate =
            rates[yourCurrency]?.rates?.get(requiredCurrency)
                ?: BigDecimal.ONE

        val screen = ExchangeScreenModel(
            yourCurrencyCards,
            requiredCurrencyCards,
            "1${yourCurrency.symbol()} = ${CurrencyFormatter.formatCurrency(currentRate)}${requiredCurrency.symbol()}"
        )
        _screenViewModel.value = screen
    }

    override fun onCleared() {
        super.onCleared()
        periodicUpdatesTimer.cancel()
    }

    init {
        init()
    }
}