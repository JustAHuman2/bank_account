package de.dkb.account.service

import de.dkb.account.data.AccountRepository
import de.dkb.account.exception.AccountNotFoundException
import de.dkb.account.model.Account
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AccountService(
    val accountRepository: AccountRepository
) {
    fun getAccount(accountId: UUID) = accountRepository.findByAccountId(accountId) ?:
    throw AccountNotFoundException("Account with id:$accountId can not be found")

    fun updateAccount(account: Account) = accountRepository.saveAndFlush(account)
}
