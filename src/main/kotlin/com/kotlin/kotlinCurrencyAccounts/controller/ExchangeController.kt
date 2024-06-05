package com.kotlin.kotlinCurrencyAccounts.controller

import com.kotlin.kotlinCurrencyAccounts.controller.mapper.ExchangeMapper
import com.kotlin.kotlinCurrencyAccounts.generated.api.ExchangeApi
import com.kotlin.kotlinCurrencyAccounts.generated.model.CurrencyExchangeRequest
import com.kotlin.kotlinCurrencyAccounts.generated.model.UserAccount
import com.kotlin.kotlinCurrencyAccounts.service.CurrencyExchangeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ExchangeController(
    var currencyExchangeService: CurrencyExchangeService,
    var exchangeMapper: ExchangeMapper
) : ExchangeApi {

    override fun exchangeCurrency(currencyExchangeRequest: CurrencyExchangeRequest): ResponseEntity<UserAccount> {
        var exchange = exchangeMapper.map(currencyExchangeRequest)
        val account = currencyExchangeService.exchangeCurrency(exchange)
        return ResponseEntity.ok().body(
            UserAccount(
                account.pesel,
                account.firstName,
                account.lastName,
                account.balancePLN,
                account.balanceUSD
            )
        )
    }
}