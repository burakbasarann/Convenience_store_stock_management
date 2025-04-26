package com.basaran.casestudy.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.data.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    private val _lowStockProducts = MutableLiveData<List<Product>>()
    val lowStockProducts: LiveData<List<Product>> get() = _lowStockProducts

    private val _recentTransactions = MutableLiveData<List<Transaction>>()
    val recentTransactions: LiveData<List<Transaction>> get() = _recentTransactions

    init {
        loadMockData()
    }

    private fun loadMockData() {
        _lowStockProducts.value = listOf(
            Product(1, "Ülker Çikolatalı Gofret", "", 15.0, "Atıştırmalık", "123456", 1, 2, 10),
            Product(2, "Su", "0.5L Doğal Su", 5.0, "İçecek", "654321", 2, 3, 10),
            Product(3, "Pepsi", "0.33L Pepsi", 8.0, "İçecek", "111213", 2, 3, 10),
            Product(4, "Coca Cola", "0.33L Coca Cola", 8.0, "İçecek", "141516", 2, 3, 10),
            Product(5, "Bisküvi", "Yulaflı Bisküvi", 7.5, "Atıştırmalık", "789012", 1, 2, 10),
            Product(6, "Çikolata", "Sütlü Çikolata", 12.0, "Tatlı", "192021", 1, 2, 10),
            Product(7, "Elma", "Taze Elma", 4.0, "Meyve", "222324", 3, 4, 10),
            Product(8, "Armut", "Taze Armut", 4.5, "Meyve", "252627", 3, 4, 10),
            Product(9, "Kola", "0.5L Kola", 6.0, "İçecek", "282930", 2, 3, 10),
            Product(10, "Ayran", "200ml Ayran", 3.0, "İçecek", "313233", 2, 3, 10)
        )

        _recentTransactions.value = listOf(
            Transaction(1, System.currentTimeMillis() - 3600000, TransactionType.RESTOCK, 1, 2, "Satış Yapıldı"),
            Transaction(2, System.currentTimeMillis() - 7200000, TransactionType.SALE, 2, 10, "Stok Güncellendi"),
            Transaction(3, System.currentTimeMillis() - 10800000, TransactionType.SALE, 3, 5, "Yeni Satış"),
            Transaction(4, System.currentTimeMillis() - 14400000, TransactionType.RESTOCK, 4, 7, "Yeniden Stok Eklendi"),
            Transaction(5, System.currentTimeMillis() - 18000000, TransactionType.SALE, 5, 3, "Ürün Satışı Yapıldı"),
            Transaction(6, System.currentTimeMillis() - 21600000, TransactionType.RESTOCK, 6, 2, "Stok Yenilendi"),
            Transaction(7, System.currentTimeMillis() - 25200000, TransactionType.SALE, 7, 10, "Satış Gerçekleşti"),
            Transaction(8, System.currentTimeMillis() - 28800000, TransactionType.RESTOCK, 8, 6, "Yeni Ürün Stokta"),
            Transaction(9, System.currentTimeMillis() - 32400000, TransactionType.SALE, 9, 8, "Satış Yapıldı"),
            Transaction(10, System.currentTimeMillis() - 36000000, TransactionType.RESTOCK, 10, 4, "Stok Güncellendi")
        )
    }
}