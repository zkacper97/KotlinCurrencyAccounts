package com.kotlin.kotlinCurrencyAccounts.repository.mapper

import com.kotlin.kotlinCurrencyAccounts.generated.model.CreateAccountRequest
import com.kotlin.kotlinCurrencyAccounts.model.Account
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface AccountMapper {

    @Mapping(target = "balanceUSD", expression = "java(java.math.BigDecimal.ZERO.setScale(2, java.math.RoundingMode.HALF_DOWN))")
    @Mapping(target = "balancePLN", source = "initialBalancePLN")
    fun map(createAccountRequest: CreateAccountRequest): Account
}
