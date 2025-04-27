package com.basaran.casestudy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long,
    val type: TransactionType,
    val productId: Long,
    val quantity: Int,
    val notes: String?
)

enum class TransactionType {
    SALE,
    RESTOCK
}