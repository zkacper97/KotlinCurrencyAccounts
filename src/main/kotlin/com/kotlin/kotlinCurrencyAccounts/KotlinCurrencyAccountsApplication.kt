package com.kotlin.kotlinCurrencyAccounts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class KotlinCurrencyAccountsApplication

fun main(args: Array<String>) {
	runApplication<KotlinCurrencyAccountsApplication>(*args)
}
