package com.basaran.casestudy.repository

import com.basaran.casestudy.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(productId: Long): Product?
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun seedInitialData()
}