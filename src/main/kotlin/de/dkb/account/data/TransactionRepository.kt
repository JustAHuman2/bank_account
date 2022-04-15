package de.dkb.account.data

import de.dkb.account.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import org.springframework.data.jpa.repository.Query

@Repository
interface TransactionRepository : JpaRepository<Transaction, UUID> {
    @Query("select transaction from Transaction as transaction where transaction.account.accountId = :accountId")
    fun findAllByAccountId(accountId: UUID) : List<Transaction>
}