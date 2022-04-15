package de.dkb.account.service

import de.dkb.account.data.TransactionRepository
import de.dkb.account.exception.TransactionException
import de.dkb.account.model.Account
import de.dkb.account.model.Transaction
import de.dkb.account.model.Type
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class TransactionService(
    private val transactionRepo: TransactionRepository,
    private val accountService: AccountService
) {
    fun getTransactions(accountId: UUID) = transactionRepo.findAllByAccountId(accountId)

    @Transactional
    fun makeTransaction(
        creditorId: UUID, debtorId: UUID, amount: Int, description: String
    ): Transaction {
        val creditorAcc = accountService.getAccount(creditorId)
        val debtorAcc = accountService.getAccount(debtorId)
        validateAccounts(creditorAcc, debtorAcc, amount)
        accountService.updateAccount(creditorAcc.copy(balance = creditorAcc.balance - amount))
        accountService.updateAccount(debtorAcc.copy(balance = debtorAcc.balance + amount))
        return transactionRepo.saveAndFlush(
            createTransaction(creditorAcc, debtorAcc, amount, description)
        )
    }

    private fun validateAccounts(creditorAcc: Account, debtorAcc: Account, amount: Int) {
        if (creditorAcc.type == Type.LOAN.name) {
            throw TransactionException("It is not allowed to transfer funds from the loan account")
        }
        if (creditorAcc.type == Type.SAVINGS.name && debtorAcc.type != Type.CHECKING.name) {
            throw TransactionException(
                "It is not allowed to transfer funds from the savings account to the ${debtorAcc.type.lowercase()} account"
            )
        }
        if (amount > creditorAcc.balance) throw TransactionException("Not enough funds")
    }

    private fun createTransaction(
        creditorAcc: Account, debtorAcc: Account, amount: Int, description: String?
    ): Transaction = Transaction(
        UUID.randomUUID(),
        debtorAcc.iban,
        creditorAcc.iban,
        amount,
        description = description ?: "",
        debtorAcc
    )
}
