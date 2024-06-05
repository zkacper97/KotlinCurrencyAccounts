package com.kotlin.kotlinCurrencyAccounts.exception.custom

class CurrencyExchangeFailedException() :
    Exception("Failed to fetch currency exchange rates.") {
}