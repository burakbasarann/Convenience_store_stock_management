package com.basaran.casestudy.repository.transaction

import com.basaran.casestudy.data.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun getAllTransactions() : Flow<List<Transaction>>
    suspend fun getTransactionById(transactionId: Long) : Transaction?
    suspend fun getTransactionsByProduct(productId : Long) : Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
}