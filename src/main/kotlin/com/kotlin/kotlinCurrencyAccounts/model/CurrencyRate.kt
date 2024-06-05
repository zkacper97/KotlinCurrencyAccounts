package com.kotlin.kotlinCurrencyAccounts.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class CurrencyRate(

    @JsonProperty("no")
    var exchangeRateNo: String,
    var effectiveDate: LocalDate,
    @JsonProperty("mid")
    var exchangeRate: Double
)
