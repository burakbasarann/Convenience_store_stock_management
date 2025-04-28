package com.basaran.casestudy.data.local.dao

import androidx.room.*
import com.basaran.casestudy.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products WHERE userId = :userId")
    fun getAllProducts(userId: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id AND userId = :userId")
    suspend fun getProductById(id: Long, userId: String): Product?

    @Query("SELECT * FROM products WHERE userId = :userId AND currentStock <= minStock")
    fun getLowStockProducts(userId: String): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM products WHERE userId = :userId AND (name LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%')")
    fun searchProducts(userId: Int, query: String): Flow<List<Product>>
}
