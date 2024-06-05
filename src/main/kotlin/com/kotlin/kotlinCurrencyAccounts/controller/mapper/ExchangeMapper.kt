package com.kotlin.kotlinCurrencyAccounts.controller.mapper

import com.kotlin.kotlinCurrencyAccounts.generated.model.CurrencyExchangeRequest
import com.kotlin.kotlinCurrencyAccounts.model.Exchange
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ExchangeMapper {

    fun map(currencyExchangeRequest: CurrencyExchangeRequest): Exchange
}