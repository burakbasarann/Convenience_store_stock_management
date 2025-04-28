package com.basaran.casestudy.repository.transaction

import com.basaran.casestudy.data.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun getAllTransactions(userId: String): Flow<List<Transaction>>
    suspend fun getTransactionById(transactionId: Long, userId: String): Transaction?
    suspend fun getTransactionsByProduct(productId: Long, userId: String): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
}
