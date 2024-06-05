package com.kotlin.kotlinCurrencyAccounts.exception

import java.time.LocalDateTime

data class ErrorResponse(
    var statusCode: Int,
    var message: String?,
    var timestamp: LocalDateTime
)
