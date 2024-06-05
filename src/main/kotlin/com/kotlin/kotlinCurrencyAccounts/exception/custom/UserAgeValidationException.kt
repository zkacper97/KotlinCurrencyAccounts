package com.kotlin.kotlinCurrencyAccounts.exception.custom

class UserAgeValidationException(pesel: String) : Exception("User with PESEL $pesel is not an adult.") {
}