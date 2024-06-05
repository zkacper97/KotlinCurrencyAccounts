package com.kotlin.kotlinCurrencyAccounts.exception.custom

class InsufficientFundsException(pesel: String) :
    Exception("Insufficient funds for user $pesel to currency exchange.") {
}