package com.example.echangeapp.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.echangeapp.domain.models.Currency
import com.example.echangeapp.domain.models.CurrencyAmount
import com.example.echangeapp.domain.repositories.AvailableCurrencyRepository
import com.google.gson.Gson
import java.math.BigDecimal
import javax.inject.Inject

class AvailableCurrencyRepositoryImpl @Inject constructor(context: Context): AvailableCurrencyRepository {

    companion object {
        private const val PREFS_KEY = "PreferencesRepository"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun read(base: Currency): CurrencyAmount {
        val initial = CurrencyAmount(base, BigDecimal(100))
        return gson.fromJson(
            prefs.getString(base.name, gson.toJson(initial)),
            CurrencyAmount::class.java
        )
    }

    override fun update(amount: CurrencyAmount) {
        prefs.edit().putString(amount.base.name, gson.toJson(amount)).apply()
    }
}