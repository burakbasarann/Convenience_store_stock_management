package com.basaran.casestudy.di

import android.content.Context
import androidx.room.Room
import com.basaran.casestudy.data.local.StoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): StoreDatabase {
        return Room.databaseBuilder(
            context,
            StoreDatabase::class.java,
            "store_database"
        ).build()
    }

    @Provides
    fun provideProductDao(database: StoreDatabase) = database.productDao()

    @Provides
    fun provideSupplierDao(database: StoreDatabase) = database.supplierDao()

    @Provides
    fun provideTransactionDao(database: StoreDatabase) = database.transactionDao()

    @Provides
    fun provideUserDao(database: StoreDatabase) = database.userDao()
} 