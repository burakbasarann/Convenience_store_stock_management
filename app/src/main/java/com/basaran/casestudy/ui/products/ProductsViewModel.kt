package com.basaran.casestudy.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.repository.product.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val products: LiveData<List<Product>> = productRepository.getAllProducts()
        .asLiveData(viewModelScope.coroutineContext)

    private val _filteredProducts = MutableLiveData<List<Product>>()
    val filteredProducts: LiveData<List<Product>> get() = _filteredProducts

    init {
        viewModelScope.launch {
            productRepository.seedInitialData()
            productRepository.getAllProducts().collect { allProducts ->
                _filteredProducts.value = allProducts
            }
        }
    }

    fun filterProducts(query: String) {
        viewModelScope.launch {
            productRepository.getAllProducts().collect { allProducts ->
                _filteredProducts.value = if (query.isEmpty()) {
                    allProducts
                } else {
                    allProducts.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }
            }
        }
    }
}
