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
    override suspend fun getAllSuppliers(): Flow<List<Supplier>> =
        withContext(Dispatchers.IO) {
            supplierDao.getAllSuppliers()
        }

    override suspend fun getSupplierById(supplierId: Long): Supplier? =
        withContext(Dispatchers.IO) {
            supplierDao.getSupplierById(supplierId)
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

    override fun searchSuppliers(query: String): Flow<List<Supplier>> =
        supplierDao.searchSuppliers(query)

    override suspend fun seedInitialData() {
        if (supplierDao.getAllSuppliers().first().isEmpty()) {
            val initialSupplier = listOf(
                Supplier(
                    name = "A Firması",
                    contactPerson = "Ahmet",
                    phone = "55555555",
                    email = "aaaa@gmail.com",
                    address = ""
                ),
                Supplier(
                    name = "B Firması",
                    contactPerson = "Alp",
                    phone = "55555555",
                    email = "aaaa@gmail.com",
                    address = ""
                ),

                )
            initialSupplier.forEach { supplierDao.insertSupplier(it) }
        }
    }
}