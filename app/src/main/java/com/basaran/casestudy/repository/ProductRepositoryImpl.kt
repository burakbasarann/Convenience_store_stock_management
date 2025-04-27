package com.basaran.casestudy.repository

import com.basaran.casestudy.data.local.dao.ProductDao
import com.basaran.casestudy.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()

    override suspend fun getProductById(productId: Long): Product? =
        withContext(Dispatchers.IO) {
            productDao.getProductById(productId)
        }

    override suspend fun insertProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.insertProduct(product)
        }
    }

    override suspend fun updateProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.updateProduct(product)
        }
    }

    override suspend fun deleteProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.deleteProduct(product)
        }
    }

    override fun searchProducts(query: String): Flow<List<Product>> =
        productDao.searchProducts(query)

    override suspend fun seedInitialData() = withContext(Dispatchers.IO) {
        if (productDao.getAllProducts().first().isEmpty()) {
            val initialProducts = listOf(
                Product(name = "Ülker Çikolatalı Gofret", description = "", price = 15.0,
                    category = "Atıştırmalık", barcode = "123456", supplierId = 1,
                    currentStock = 2, minStock = 10),
                Product(name = "Su", description = "0.5L Doğal Su", price = 5.0,
                    category = "İçecek", barcode = "654321", supplierId = 2,
                    currentStock = 3, minStock = 10)
            )
            initialProducts.forEach { productDao.insertProduct(it) }
        }
    }
}