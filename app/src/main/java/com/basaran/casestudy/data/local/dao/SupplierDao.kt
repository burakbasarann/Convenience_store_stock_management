package com.basaran.casestudy.data.local.dao

import androidx.room.*
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {

    @Query("SELECT * FROM suppliers WHERE userId = :userId")
    fun getAllSuppliers(userId: String): Flow<List<Supplier>>

    @Query("SELECT * FROM suppliers WHERE id = :id AND userId = :userId")
    suspend fun getSupplierById(id: Long, userId: String): Supplier?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier: Supplier): Long

    @Update
    suspend fun updateSupplier(supplier: Supplier)

    @Delete
    suspend fun deleteSupplier(supplier: Supplier)

    @Query("SELECT * FROM suppliers WHERE userId = :userId AND name LIKE '%' || :query || '%'")
    fun searchSuppliers(userId: String, query: String): Flow<List<Supplier>>
}
