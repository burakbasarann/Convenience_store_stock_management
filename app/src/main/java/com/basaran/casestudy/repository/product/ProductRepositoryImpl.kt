package com.basaran.casestudy.repository.product

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

    override fun getAllProducts(userId: String): Flow<List<Product>> =
        productDao.getAllProducts(userId)

    override suspend fun getProductById(productId: Long, userId: String): Product? =
        withContext(Dispatchers.IO) {
            productDao.getProductById(productId, userId)
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

    override suspend fun getLowStockProducts(userId: String): Flow<List<Product>> =
        withContext(Dispatchers.IO) {
            productDao.getLowStockProducts(userId)
        }

    override suspend fun deleteProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.deleteProduct(product)
        }
    }

    override fun searchProducts(userId: Int, query: String): Flow<List<Product>> =
        productDao.searchProducts(userId, query)

    override suspend fun seedInitialData(userId: String) = withContext(Dispatchers.IO) {
        if (productDao.getAllProducts(userId).first().isEmpty()) {
            val initialProducts = listOf(
                Product(
                    name = "Ülker Çikolatalı Gofret",
                    description = "",
                    price = 15.0,
                    category = "Atıştırmalık",
                    barcode = "123456",
                    supplierId = 1,
                    currentStock = 2,
                    minStock = 10,
                    userId = userId
                ),
                Product(
                    name = "Su",
                    description = "0.5L Doğal Su",
                    price = 5.0,
                    category = "İçecek",
                    barcode = "654321",
                    supplierId = 2,
                    currentStock = 3,
                    minStock = 10,
                    userId = userId
                )
            )
            initialProducts.forEach { productDao.insertProduct(it) }
        }
    }
}
