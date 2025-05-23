package com.basaran.casestudy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.basaran.casestudy.data.local.dao.ProductDao
import com.basaran.casestudy.data.local.dao.SupplierDao
import com.basaran.casestudy.data.local.dao.TransactionDao
import com.basaran.casestudy.data.local.dao.UserDao
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.data.model.User

@Database(
    entities = [Product::class, Supplier::class, Transaction::class, User::class],
    version = 1,
    exportSchema = false
)

abstract class StoreDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun supplierDao(): SupplierDao
    abstract fun transactionDao(): TransactionDao
    abstract fun userDao(): UserDao
}