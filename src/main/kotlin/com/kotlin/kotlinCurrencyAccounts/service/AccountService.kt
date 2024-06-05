package com.kotlin.kotlinCurrencyAccounts.service

import com.kotlin.kotlinCurrencyAccounts.exception.custom.PeselValidationException
import com.kotlin.kotlinCurrencyAccounts.exception.custom.UserAgeValidationException
import com.kotlin.kotlinCurrencyAccounts.exception.custom.UserAlreadyExistsException
import com.kotlin.kotlinCurrencyAccounts.exception.custom.UserNotFoundException
import com.kotlin.kotlinCurrencyAccounts.generated.model.CreateAccountRequest
import com.kotlin.kotlinCurrencyAccounts.model.Account
import com.kotlin.kotlinCurrencyAccounts.repository.AccountRepository
import com.kotlin.kotlinCurrencyAccounts.repository.mapper.AccountMapper
import mu.KotlinLogging
import org.springframework.stereotype.Service
import pl.foltak.polishidnumbers.pesel.InvalidPeselException
import pl.foltak.polishidnumbers.pesel.Pesel
import java.time.LocalDate

@Service
class AccountService(
    var accountRepository: AccountRepository,
    var accountMapper: AccountMapper
) {
    companion object {
        private val logger = KotlinLogging.logger {}
        private const val ADULT_AGE = 18L
    }

    fun getAccount(pesel: String): Account {
        logger.info("Getting user account with PESEL {}", pesel)

        return accountRepository.findById(pesel)
            .orElseThrow { UserNotFoundException(pesel) }
    }

    fun createUserAccount(accountRequest: CreateAccountRequest) {
        logger.info("Creating user account by PESEL {}", accountRequest.pesel)
        val pesel = getValidatedPesel(accountRequest.pesel)

        validateUserAge(pesel.birthDate, accountRequest.pesel)
        validateUserExisting(accountRequest.pesel)

        val account = accountMapper.map(accountRequest)
        accountRepository.save(account)
    }

    private fun validateUserExisting(pesel: String) {
        if (accountRepository.existsById(pesel)) {
            throw UserAlreadyExistsException(pesel)
        }
    }

    private fun validateUserAge(birthDate: LocalDate, pesel: String) {
        if (!isAdult(birthDate)) {
            throw UserAgeValidationException(pesel)
        }
    }

    private fun isAdult(birthDate: LocalDate): Boolean {
        val now = LocalDate.now()
        val adultDate = birthDate.plusYears(ADULT_AGE)

        return !now.isBefore(adultDate)
    }

    private fun getValidatedPesel(pesel: String): Pesel {
        try {
            return Pesel(pesel)
        } catch (e: InvalidPeselException) {
            throw PeselValidationException(e.message)
        }
    }
}
