package com.kotlin.kotlinCurrencyAccounts.model

import com.kotlin.kotlinCurrencyAccounts.generated.model.Currency
import java.math.BigDecimal

data class Exchange(

    var pesel: String,
    var amount: BigDecimal,
    var fromCurrency: CurrencyCode,
    var toCurrency: CurrencyCode
)
