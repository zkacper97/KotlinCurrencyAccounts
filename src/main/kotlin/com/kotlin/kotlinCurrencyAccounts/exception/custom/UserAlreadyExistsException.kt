package com.kotlin.kotlinCurrencyAccounts.exception.custom

class UserAlreadyExistsException(pesel: String) : Exception("User with PESEL $pesel already has an account.") {
}