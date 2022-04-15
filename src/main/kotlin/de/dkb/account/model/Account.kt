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

    val type: String, //todo: it will be better to change String to Enum & make table with
    // Type values in DB & link it with Account table to make it type-safe,
    //also change h2 to normal DB and move test data from resources to the test package &
    //maybe add testContainer to check how it works inside DB and rest-assured for endpoints testing

    @JsonIgnore
    val balance: Int,

    val holder: String,

    @OneToMany(mappedBy = "account")
    val transactions: List<Transaction>
) {
    val currentBalance get() = balance - transactions.sumOf { it.amount }
}

enum class Type { CHECKING, SAVINGS, LOAN }
