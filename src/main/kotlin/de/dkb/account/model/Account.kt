package de.dkb.account.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    val accountId: UUID,

    val iban: String,

    val type: String,

    @JsonIgnore
    val balance: Double,

    val holder: String,

    @OneToMany(mappedBy = "account")
    val transactions: List<Transaction>
) {
    val currentBalance get() = balance - transactions.sumOf { it.amount }
}

enum class Type { CHECKING, SAVINGS, LOAN }
