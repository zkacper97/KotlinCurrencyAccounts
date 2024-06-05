package com.kotlin.kotlinCurrencyAccounts.repository

import com.kotlin.kotlinCurrencyAccounts.model.Account
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : CrudRepository<Account, String> {

}
