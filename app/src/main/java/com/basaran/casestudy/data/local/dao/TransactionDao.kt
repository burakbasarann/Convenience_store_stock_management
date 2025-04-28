package com.basaran.casestudy.data.local.dao

import androidx.room.*
import com.basaran.casestudy.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    fun getAllTransactions(userId: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id AND userId = :userId")
    suspend fun getTransactionById(id: Long, userId: String): Transaction?

    @Query("SELECT * FROM transactions WHERE productId = :productId AND userId = :userId ORDER BY date DESC")
    fun getTransactionsByProduct(productId: Long, userId: String): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
}
