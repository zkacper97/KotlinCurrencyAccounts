package com.kotlin.kotlinCurrencyAccounts.exception

import com.kotlin.kotlinCurrencyAccounts.exception.custom.*
import jakarta.validation.ConstraintViolationException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return createErrorResponseEntity(HttpStatus.NOT_FOUND, ex)
    }

    @ExceptionHandler(
        value = [
            InsufficientFundsException::class,
            CurrencyExchangeFailedException::class,
            SameCurrencyExchangeException::class,
            PeselValidationException::class,
            UserAlreadyExistsException::class,
            UserAgeValidationException::class,
            ConstraintViolationException::class,
            IllegalArgumentException::class,
            HttpMessageNotReadableException::class]
    )
    fun handleBadRequestExceptions(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, ex)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        return createErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex)
    }

    private fun createErrorResponseEntity(
        internalServerError: HttpStatus,
        ex: Exception
    ): ResponseEntity<ErrorResponse> {
        logger.error("Error occurred: {}  {}", ex.message, ex)
        val errorResponse = ErrorResponse(internalServerError.value(), ex.message, LocalDateTime.now())
        return ResponseEntity(errorResponse, internalServerError)
    }
}