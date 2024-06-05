package com.kotlin.kotlinCurrencyAccounts.client

import com.kotlin.kotlinCurrencyAccounts.model.ExchangeCurrency
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "currency-exchange-service", url = "\${configuration.exchange.service.url}")
interface CurrencyExchangeClient {
    @GetMapping
    fun getExchangeRates(): ExchangeCurrency
}
