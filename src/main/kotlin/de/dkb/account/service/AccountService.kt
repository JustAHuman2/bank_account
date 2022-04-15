package de.dkb.account.service

import de.dkb.account.data.AccountRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountService(
    val accountRepository: AccountRepository
) {
    fun getAccount(accountId: UUID) = accountRepository.findByAccountId(accountId)
}