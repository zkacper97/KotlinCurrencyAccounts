package com.kotlin.kotlinCurrencyAccounts.exception.custom

class UserNotFoundException(pesel: String) : Exception("User with PESEL $pesel doesn't exist.") {
}