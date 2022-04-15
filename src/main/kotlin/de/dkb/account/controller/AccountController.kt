package de.dkb.account.controller

import de.dkb.account.service.AccountService
import de.dkb.account.service.TransactionService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID
import javax.validation.constraints.Positive

@RestController
@Validated
class AccountController(
    val accountService: AccountService,
    val transactionService: TransactionService
) {
    @GetMapping("account")
    fun getAccount(
        @RequestParam("accountId") accountId: UUID
    ) = accountService.getAccount(accountId)

    @GetMapping("account/transaction")
    fun getTransactions(
        @RequestParam("accountId") accountId: UUID
    ) = transactionService.getTransactions(accountId)

    @PostMapping("account")
    fun makeTransaction(
        @RequestParam accountId: UUID,
        @RequestParam debtorId: UUID,
        @RequestParam @Positive amount: Int,
        @RequestParam description: String,
    ) = transactionService.makeTransaction(accountId, debtorId, amount, description)
}
