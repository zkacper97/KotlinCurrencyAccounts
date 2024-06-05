package com.kotlin.kotlinCurrencyAccounts.model

data class ExchangeCurrency(

    var currency: String,
    var code: String,
    var rates: List<CurrencyRate>
)
