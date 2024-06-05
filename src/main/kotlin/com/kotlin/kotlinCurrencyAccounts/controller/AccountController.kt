package com.kotlin.kotlinCurrencyAccounts.controller

import com.kotlin.kotlinCurrencyAccounts.generated.api.AccountsApi
import com.kotlin.kotlinCurrencyAccounts.generated.model.CreateAccountRequest
import com.kotlin.kotlinCurrencyAccounts.generated.model.UserAccount
import com.kotlin.kotlinCurrencyAccounts.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(
    var accountService: AccountService
) : AccountsApi {

    override fun getUserAccountByPesel(pesel: String): ResponseEntity<UserAccount> {
        val account = accountService.getAccount(pesel)
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

    override fun createUserAccount(createAccountRequest: CreateAccountRequest): ResponseEntity<Unit> {
        accountService.createUserAccount(createAccountRequest)

        return ResponseEntity.ok().build()
    }
}