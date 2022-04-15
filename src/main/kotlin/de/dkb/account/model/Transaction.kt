package de.dkb.account.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "transactions")
class Transaction (
    @Id
    val transactionId: UUID,

    val debtorIban: String,

    val creditorIban: String,

    val amount: Int, //I think, it is safer to keep balance in Int, operating with cents on backend

    val description: String,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="account_id", nullable=false)
    val account: Account
)
