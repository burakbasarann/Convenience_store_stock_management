package com.basaran.casestudy.repository

import com.basaran.casestudy.data.model.Product
import javax.inject.Inject

class ProductRepository @Inject constructor() {   //TODO Tum mock datalar tasinabilir bir incelenecek, roomdb aktif kullanilacak

    private val mockProducts = listOf(
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

    fun getAllProducts(): List<Product> {
        return mockProducts
    }
}