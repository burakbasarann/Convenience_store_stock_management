package com.basaran.casestudy.repository.supplier

import com.basaran.casestudy.data.local.dao.SupplierDao
import com.basaran.casestudy.data.model.Supplier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupplierRepositoryImpl @Inject constructor(
    private val supplierDao: SupplierDao,
) : SupplierRepository {

    override fun getAllSuppliers(userId: String): Flow<List<Supplier>> =
        supplierDao.getAllSuppliers(userId)

    override suspend fun getSupplierById(supplierId: Long, userId: String): Supplier? =
        withContext(Dispatchers.IO) {
            supplierDao.getSupplierById(supplierId, userId)
        }

    override suspend fun insertSupplier(supplier: Supplier) {
        withContext(Dispatchers.IO) {
            supplierDao.insertSupplier(supplier)
        }
    }

    override suspend fun updateSupplier(supplier: Supplier) {
        withContext(Dispatchers.IO) {
            supplierDao.updateSupplier(supplier)
        }
    }

    override suspend fun deleteSupplier(supplier: Supplier) {
        withContext(Dispatchers.IO) {
            supplierDao.deleteSupplier(supplier)
        }
    }

    override fun searchSuppliers(userId: String, query: String): Flow<List<Supplier>> =
        supplierDao.searchSuppliers(userId, query)

    override suspend fun seedInitialData(userId: String) = withContext(Dispatchers.IO) {
        if (supplierDao.getAllSuppliers(userId).first().isEmpty()) {
            val initialSuppliers = listOf(
                Supplier(
                    name = "A Firması",
                    contactPerson = "Ahmet",
                    phone = "55555555",
                    email = "aaaa@gmail.com",
                    address = "",
                    userId = userId
                ),
                Supplier(
                    name = "B Firması",
                    contactPerson = "Alp",
                    phone = "55555555",
                    email = "aaaa@gmail.com",
                    address = "",
                    userId = userId
                )
            )
            initialSuppliers.forEach { supplierDao.insertSupplier(it) }
        }
    }
}