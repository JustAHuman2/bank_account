package de.dkb.account.data

import de.dkb.account.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : JpaRepository<Account, UUID> {
    fun findByAccountId(accountId: UUID) : Account
}