package com.example.echangeapp.domain.models

import java.io.Serializable
import java.math.BigDecimal

data class CurrencyAmount(val base: Currency, val amount: BigDecimal)

