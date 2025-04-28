package com.basaran.casestudy.repository.transaction

import com.basaran.casestudy.data.local.dao.TransactionDao
import com.basaran.casestudy.data.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override suspend fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllTransactions()

    override suspend fun getTransactionById(transactionId: Long): Transaction? =
        withContext(Dispatchers.IO) {
            transactionDao.getTransactionById(transactionId)
        }

    override suspend fun getTransactionsByProduct(productId: Long): Flow<List<Transaction>> =
        withContext(Dispatchers.IO) {
            transactionDao.getTransactionsByProduct(productId)
        }

    override suspend fun insertTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            transactionDao.insertTransaction(transaction)
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            transactionDao.updateTransaction(transaction)
        }
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            transactionDao.deleteTransaction(transaction)
        }
    }
}