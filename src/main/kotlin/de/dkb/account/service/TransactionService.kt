package de.dkb.account.service

import de.dkb.account.data.TransactionRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TransactionService(
    val transactionRepository: TransactionRepository
) {
    fun getTransactions(accountId: UUID) = transactionRepository.findAllByAccountId(accountId)
}