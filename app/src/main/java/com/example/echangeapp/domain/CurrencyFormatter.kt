package com.example.echangeapp.domain

import java.math.BigDecimal
import java.text.DecimalFormat

object CurrencyFormatter {

    private val formatter = DecimalFormat()

    init {
        formatter.maximumFractionDigits = 2

        formatter.minimumFractionDigits = 0


        formatter.isGroupingUsed = false
    }

    fun formatCurrency(number: BigDecimal): String {
        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return ""
        }
        return formatter.format(number)
    }
}