package com.basaran.casestudy

import com.basaran.casestudy.data.local.dao.TransactionDao
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.data.model.TransactionType
import com.basaran.casestudy.repository.transaction.TransactionRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TransactionRepositoryImplTest {

    private lateinit var transactionDao: TransactionDao
    private lateinit var repository: TransactionRepositoryImpl
    private val userId = "test_user_id"

    @Before
    fun setup() {
        transactionDao = mockk()
        repository = TransactionRepositoryImpl(transactionDao)
    }

    @Test
    fun `getAllTransactions returns flow of transactions`() = runBlocking {
        val transactions = listOf(
            Transaction(
                id = 1,
                userId = userId,
                date = System.currentTimeMillis(),
                type = TransactionType.SALE,
                productId = 1,
                quantity = 5,
                notes = "Test note"
            )
        )
        coEvery { transactionDao.getAllTransactions(userId) } returns flowOf(transactions)

        val result = repository.getAllTransactions(userId).first()
        assertEquals(transactions, result)
        coVerify { transactionDao.getAllTransactions(userId) }
    }

    @Test
    fun `getTransactionById returns transaction when exists`() = runBlocking {
        val transaction = Transaction(
            id = 1,
            userId = userId,
            date = System.currentTimeMillis(),
            type = TransactionType.SALE,
            productId = 1,
            quantity = 5,
            notes = "Test note"
        )
        coEvery { transactionDao.getTransactionById(1, userId) } returns transaction
        val result = repository.getTransactionById(1, userId)
        assertEquals(transaction, result)
        coVerify { transactionDao.getTransactionById(1, userId) }
    }

    @Test
    fun `getTransactionsByProduct returns flow of transactions`() = runBlocking {
        val transactions = listOf(
            Transaction(
                id = 1,
                userId = userId,
                date = System.currentTimeMillis(),
                type = TransactionType.SALE,
                productId = 1,
                quantity = 5,
                notes = "Test note"
            )
        )
        coEvery { transactionDao.getTransactionsByProduct(1, userId) } returns flowOf(transactions)
        val result = repository.getTransactionsByProduct(1, userId).first()
        assertEquals(transactions, result)
        coVerify { transactionDao.getTransactionsByProduct(1, userId) }
    }

    @Test
    fun `insertTransaction calls dao insertTransaction`() = runBlocking {
        val transaction = Transaction(
            id = 1,
            userId = userId,
            date = System.currentTimeMillis(),
            type = TransactionType.SALE,
            productId = 1,
            quantity = 5,
            notes = "Test note"
        )
        coEvery { transactionDao.insertTransaction(transaction) } returns Unit
        repository.insertTransaction(transaction)
        coVerify { transactionDao.insertTransaction(transaction) }
    }

    @Test
    fun `updateTransaction calls dao updateTransaction`() = runBlocking {
        val transaction = Transaction(
            id = 1,
            userId = userId,
            date = System.currentTimeMillis(),
            type = TransactionType.SALE,
            productId = 1,
            quantity = 5,
            notes = "Test note"
        )
        coEvery { transactionDao.updateTransaction(transaction) } returns Unit
        repository.updateTransaction(transaction)
        coVerify { transactionDao.updateTransaction(transaction) }
    }

    @Test
    fun `deleteTransaction calls dao deleteTransaction`() = runBlocking {
        val transaction = Transaction(
            id = 1,
            userId = userId,
            date = System.currentTimeMillis(),
            type = TransactionType.SALE,
            productId = 1,
            quantity = 5,
            notes = "Test note"
        )
        coEvery { transactionDao.deleteTransaction(transaction) } returns Unit
        repository.deleteTransaction(transaction)
        coVerify { transactionDao.deleteTransaction(transaction) }
    }
} 