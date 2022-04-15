package de.dkb.account.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import de.dkb.account.data.TransactionRepository
import de.dkb.account.exception.TransactionException
import de.dkb.account.model.Account
import de.dkb.account.model.Type
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.Mockito.doAnswer
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AccountServiceTest {

    private val accountService: AccountService = mock()
    private val transactionRepo: TransactionRepository = mock()
    private val transactionService = TransactionService(transactionRepo, accountService)
    private val checkingId = UUID.fromString("0-0-0-0-0")
    private val checkingId2 = UUID.fromString("1-1-1-1-1")
    private val savingsId = UUID.fromString("2-2-2-2-2")
    private val savingsId2 = UUID.fromString("3-3-3-3-3")
    private val loanId = UUID.fromString("4-4-4-4-4")
    private val loanId2 = UUID.fromString("5-5-5-5-5")
    private val errorMessage = "It is not allowed to transfer funds from the"
    private val initialSum = 20
    private val checkingAccount = Account(checkingId, "iban", Type.CHECKING.name, initialSum, "name", emptyList())
    private val checkingAccount2 = checkingAccount.copy(accountId = checkingId2)
    private val savingsAccount = checkingAccount.copy(accountId = savingsId, type = Type.SAVINGS.name)
    private val savingsAccount2 = savingsAccount.copy(accountId = savingsId2)
    private val loanAccount = savingsAccount.copy(accountId = loanId, type = Type.LOAN.name)
    private val loanAccount2 = loanAccount.copy(accountId = loanId2)

    init {
        listOf<Pair<UUID, Account>>(
            Pair(checkingId, checkingAccount), Pair(checkingId2, checkingAccount2),
            Pair(savingsId, savingsAccount), Pair(savingsId2, savingsAccount2),
            Pair(loanId, loanAccount), Pair(loanId2, loanAccount2)
        ).forEach { whenever(accountService.getAccount(it.first)).thenReturn(it.second) }
        doAnswer(returnsFirstArg<Any>()).whenever(accountService).updateAccount(any())
        doAnswer(returnsFirstArg<Any>()).whenever(transactionRepo).saveAndFlush(any())
    }

    @Test
    fun whenLoanTransfersToChecking_thenThrow() {
        val ex = assertFailsWith(TransactionException::class)
        { transactionService.makeTransaction(loanId, checkingId, 1, "Description") }
        assertEquals("$errorMessage loan account", ex.message)
    }

    @Test
    fun whenLoanTransfersToSavings_thenThrow() {
        val ex = assertFailsWith(TransactionException::class)
        { transactionService.makeTransaction(loanId, savingsId, 2, "Description") }
        assertEquals("$errorMessage loan account", ex.message)
    }

    @Test
    fun whenLoanTransfersToLoan_thenThrow() {
        val ex = assertFailsWith(TransactionException::class)
        { transactionService.makeTransaction(loanId, loanId2, 3, "Description") }
        assertEquals("$errorMessage loan account", ex.message)
    }

    @Test
    fun whenSavingsTransfersToLoan_thenThrow() {
        val ex = assertFailsWith(TransactionException::class)
        { transactionService.makeTransaction(savingsId, loanId, 4, "Description") }
        assertEquals("$errorMessage savings account to the loan account", ex.message)
    }

    @Test
    fun whenSavingsTransfersToSavings_thenThrow() {
        val ex = assertFailsWith(TransactionException::class)
        { transactionService.makeTransaction(savingsId, savingsId2, 5, "Description") }
        assertEquals("$errorMessage savings account to the savings account", ex.message)
    }

    @Test
    fun whenNotEnoughFunds_thenThrow() {
        val ex = assertFailsWith(TransactionException::class)
        { transactionService.makeTransaction(checkingId, loanId, 50, "Description") }
        assertEquals("Not enough funds", ex.message)
    }

    @Test
    fun whenSavingsTransfersToChecking_thenSuccess() {
        testTransaction(savingsAccount, checkingAccount, 7)
    }

    @Test
    fun whenCheckingTransfersToChecking_thenSuccess() {
        testTransaction(checkingAccount, checkingAccount2, 8)
    }

    @Test
    fun whenCheckingTransfersToSavings_thenSuccess() {
        testTransaction(checkingAccount, savingsAccount, 9)
    }

    @Test
    fun whenCheckingTransfersToLoan_thenSuccess() {
        testTransaction(checkingAccount, loanAccount, 10)
    }

    @Test
    fun whenTransactionFailed_thenBalanceRollBack() {
        whenever(accountService.updateAccount(loanAccount)).thenThrow(RuntimeException())
        transactionService.makeTransaction(checkingId, loanId, 11, "Description")
        assertEquals(initialSum, checkingAccount.balance)
        assertEquals(initialSum, loanAccount.balance)
    }

    private fun testTransaction(creditorAcc: Account, debtorAcc: Account, amount: Int) {
        val transaction = transactionService.makeTransaction(
            creditorAcc.accountId, debtorAcc.accountId, amount, "Description")
        verify(accountService).updateAccount(creditorAcc.copy(balance = initialSum - amount))
        verify(accountService).updateAccount(debtorAcc.copy(balance = initialSum + amount))
        verify(transactionRepo).saveAndFlush(transaction)
    }
}
