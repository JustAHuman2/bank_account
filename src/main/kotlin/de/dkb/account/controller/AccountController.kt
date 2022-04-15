package de.dkb.account.controller

import de.dkb.account.service.AccountService
import de.dkb.account.service.TransactionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import org.springframework.web.bind.annotation.PathVariable

@RestController
class AccountController(
    val accountService: AccountService,
    val transactionService: TransactionService
) {
    @GetMapping("account")
    fun getAccount(
        @RequestParam("accountId") accountId: UUID
    ) = accountService.getAccount(accountId)

    @GetMapping("account/{accountId}/transaction")
    fun getTransactions(
        @PathVariable("accountId") accountId: UUID
    ) = transactionService.getTransactions(accountId)
}