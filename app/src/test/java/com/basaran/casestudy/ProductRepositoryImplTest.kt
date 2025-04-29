package com.basaran.casestudy

import com.basaran.casestudy.data.local.dao.ProductDao
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.repository.product.ProductRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var productDao: ProductDao
    private lateinit var repository: ProductRepositoryImpl
    private val userId = "test_user_id"

    @Before
    fun setup() {
        productDao = mockk()
        repository = ProductRepositoryImpl(productDao)
    }

    @Test
    fun `getAllProducts returns flow of products`() = runBlocking {
        val products = listOf(
            Product(
                id = 1,
                userId = userId,
                name = "Test Product",
                description = "Test Description",
                price = 10.0,
                category = "Test Category",
                barcode = "123456",
                currentStock = 10,
                minStock = 5
            )
        )
        coEvery { productDao.getAllProducts(userId) } returns flowOf(products)
        val result = repository.getAllProducts(userId).first()
        assertEquals(products, result)
        coVerify { productDao.getAllProducts(userId) }
    }

    @Test
    fun `getProductById returns product when exists`() = runBlocking {
        val product = Product(
            id = 1,
            userId = userId,
            name = "Test Product",
            description = "Test Description",
            price = 10.0,
            category = "Test Category",
            barcode = "123456",
            currentStock = 10,
            minStock = 5
        )
        coEvery { productDao.getProductById(1, userId) } returns product
        val result = repository.getProductById(1, userId)
        assertEquals(product, result)
        coVerify { productDao.getProductById(1, userId) }
    }

    @Test
    fun `getLowStockProducts returns flow of low stock products`() = runBlocking {
        val products = listOf(
            Product(
                id = 1,
                userId = userId,
                name = "Test Product",
                description = "Test Description",
                price = 10.0,
                category = "Test Category",
                barcode = "123456",
                currentStock = 2,
                minStock = 5
            )
        )
        coEvery { productDao.getLowStockProducts(userId) } returns flowOf(products)
        val result = repository.getLowStockProducts(userId).first()
        assertEquals(products, result)
        coVerify { productDao.getLowStockProducts(userId) }
    }

    @Test
    fun `insertProduct calls dao insertProduct`() = runBlocking {
        val product = Product(
            id = 1,
            userId = userId,
            name = "Test Product",
            description = "Test Description",
            price = 10.0,
            category = "Test Category",
            barcode = "123456",
            currentStock = 10,
            minStock = 5
        )
        coEvery { productDao.insertProduct(product) } returns 1L
        repository.insertProduct(product)
        coVerify { productDao.insertProduct(product) }
    }

    @Test
    fun `updateProduct calls dao updateProduct`() = runBlocking {
        val product = Product(
            id = 1,
            userId = userId,
            name = "Test Product",
            description = "Test Description",
            price = 10.0,
            category = "Test Category",
            barcode = "123456",
            currentStock = 10,
            minStock = 5
        )
        coEvery { productDao.updateProduct(product) } returns Unit
        repository.updateProduct(product)
        coVerify { productDao.updateProduct(product) }
    }

    @Test
    fun `deleteProduct calls dao deleteProduct`() = runBlocking {
        val product = Product(
            id = 1,
            userId = userId,
            name = "Test Product",
            description = "Test Description",
            price = 10.0,
            category = "Test Category",
            barcode = "123456",
            currentStock = 10,
            minStock = 5
        )
        coEvery { productDao.deleteProduct(product) } returns Unit
        repository.deleteProduct(product)
        coVerify { productDao.deleteProduct(product) }
    }
} 