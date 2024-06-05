package com.kotlin.kotlinCurrencyAccounts.exception.custom

class SameCurrencyExchangeException() :
    Exception("Currencies to exchange should not be the same.") {
}