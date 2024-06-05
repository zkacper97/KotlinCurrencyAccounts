package com.kotlin.kotlinCurrencyAccounts.service

import com.kotlin.kotlinCurrencyAccounts.client.CurrencyExchangeClient
import com.kotlin.kotlinCurrencyAccounts.exception.custom.CurrencyExchangeFailedException
import com.kotlin.kotlinCurrencyAccounts.exception.custom.InsufficientFundsException
import com.kotlin.kotlinCurrencyAccounts.exception.custom.SameCurrencyExchangeException
import com.kotlin.kotlinCurrencyAccounts.exception.custom.UserNotFoundException
import com.kotlin.kotlinCurrencyAccounts.model.*
import com.kotlin.kotlinCurrencyAccounts.repository.AccountRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class CurrencyExchangeService(
    var currencyExchangeClient: CurrencyExchangeClient,
    var accountRepository: AccountRepository
) {
    companion object {
        private val logger = KotlinLogging.logger {}
        private val SCALE = 2
        private val ROUNDING_MODE = RoundingMode.HALF_DOWN
    }

    fun exchangeCurrency(exchange: Exchange): Account {
        logger.info("Exchange {} {} to {}", exchange.amount, exchange.fromCurrency, exchange.toCurrency)
        val userAccount = getUserAccount(exchange)

        validateExchangeRequest(exchange)

        val availableBalanceFrom = getBalanceInCurrency(userAccount, exchange.fromCurrency)
        val availableBalanceTo = getBalanceInCurrency(userAccount, exchange.toCurrency)

        val actualRate = getActualRate()
        logger.info("Actual rate for dolar {}", actualRate)

        if (isExchangeAvailable(exchange, availableBalanceFrom)) {
            logger.info(
                "Exchange {} {} to {} is available",
                exchange.amount,
                exchange.fromCurrency,
                exchange.toCurrency
            )
            val exchangedAmount = calculateExchangedAmount(exchange.amount, actualRate, exchange.fromCurrency)
            val newBalanceFrom = availableBalanceFrom.subtract(exchange.amount.setScale(SCALE, ROUNDING_MODE))
            val newBalanceTo = availableBalanceTo.add(exchangedAmount.setScale(SCALE, ROUNDING_MODE))

            updateBalances(userAccount, exchange.fromCurrency, newBalanceFrom, newBalanceTo)

            return userAccount
        } else {
            throw InsufficientFundsException(userAccount.pesel)
        }
    }

    private fun getUserAccount(exchange: Exchange): Account {
        return accountRepository.findById(exchange.pesel)
            .orElseThrow { UserNotFoundException(exchange.pesel) }
    }


    private fun getActualRate(): Double {
        val exchangeCurrency = currencyExchangeClient.getExchangeRates()

        validateExchangeCurrency(exchangeCurrency)

        return exchangeCurrency.rates.stream()
            .map(CurrencyRate::exchangeRate)
            .findFirst()
            .orElseThrow { CurrencyExchangeFailedException() }
    }

    private fun calculateExchangedAmount(
        requestedAmount: BigDecimal,
        actualRate: Double,
        fromCurrencyCode: CurrencyCode
    ): BigDecimal {
        return if (CurrencyCode.PLN == fromCurrencyCode)
            requestedAmount.divide(BigDecimal.valueOf(actualRate), SCALE, ROUNDING_MODE)
        else requestedAmount.multiply(BigDecimal.valueOf(actualRate)).setScale(SCALE, ROUNDING_MODE)
    }

    private fun updateBalances(
        userAccount: Account,
        fromCurrencyCode: CurrencyCode,
        newBalanceFrom: BigDecimal,
        newBalanceTo: BigDecimal
    ) {
        if (CurrencyCode.PLN == fromCurrencyCode) {
            userAccount.balancePLN = newBalanceFrom
            userAccount.balanceUSD = newBalanceTo
        } else {
            userAccount.balanceUSD = newBalanceFrom
            userAccount.balancePLN = newBalanceTo
        }
        accountRepository.save(userAccount)
    }

    private fun validateExchangeCurrency(exchangeCurrency: ExchangeCurrency) {
        if (exchangeCurrency == null || exchangeCurrency.rates == null || exchangeCurrency.rates.isEmpty()) {
            throw CurrencyExchangeFailedException()
        }
    }

    private fun getBalanceInCurrency(userAccount: Account, currencyCode: CurrencyCode): BigDecimal {
        return if (CurrencyCode.PLN == currencyCode)
            userAccount.balancePLN
        else userAccount.balanceUSD
    }

    private fun validateExchangeRequest(exchangeBody: Exchange) {
        if (exchangeBody.fromCurrency == exchangeBody.toCurrency) {
            throw SameCurrencyExchangeException()
        }
    }

    private fun isExchangeAvailable(exchangeBody: Exchange, availableBalanceFrom: BigDecimal): Boolean {
        return availableBalanceFrom.compareTo(exchangeBody.amount) >= 0
    }
}
