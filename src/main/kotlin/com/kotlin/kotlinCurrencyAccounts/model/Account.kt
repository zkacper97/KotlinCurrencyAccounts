package com.kotlin.kotlinCurrencyAccounts.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.math.BigDecimal
import java.math.RoundingMode

@Entity
data class Account(

    @Id
    @Column(name = "pesel")
    var pesel: String,

    @Column(name = "first_name")
    var firstName: String,

    @Column(name = "last_name")
    var lastName: String,

    @Column(name = "balance_pln")
    var balancePLN: BigDecimal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN),

    @Column(name = "balance_usd")
    var balanceUSD: BigDecimal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN)

)
