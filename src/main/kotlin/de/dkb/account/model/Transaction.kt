package de.dkb.account.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "transactions")
class Transaction (
    @Id
    val transactionId: UUID,

    val debtorIban: String,

    val creditorIban: String,

    val amount: Double,

    val description: String,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="account_id", nullable=false)
    val account: Account
)
