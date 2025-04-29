package com.basaran.casestudy.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.repository.product.ProductRepository
import com.basaran.casestudy.utils.UiState
import com.basaran.casestudy.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val products: LiveData<List<Product>> = productRepository.getAllProducts(UserManager.getUserId())
        .asLiveData(viewModelScope.coroutineContext)

    private val _filteredProducts = MutableLiveData<List<Product>>()
    val filteredProducts: LiveData<List<Product>> get() = _filteredProducts

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    init {
        viewModelScope.launch {
            try {
                delay(300)
                productRepository.seedInitialData(UserManager.getUserId())
                productRepository.getAllProducts(UserManager.getUserId()).collect { allProducts ->
                    _uiState.postValue(UiState.Success)
                    _allProducts.value = allProducts
                    _filteredProducts.value = allProducts
                }
            } catch (e:Exception) {
                _uiState.postValue(UiState.Error(e.localizedMessage ?: "Bir hata oluştu"))
            }

        }
    }

    fun filterProducts(query: String) {
        val products = _allProducts.value
        _filteredProducts.value = if (query.isEmpty()) {
            products
        } else {
            products.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }

    fun sortProductsAscending() {
        _filteredProducts.value = _filteredProducts.value?.sortedBy { it.name } ?: emptyList()
    }

    fun sortProductsDescending() {
        _filteredProducts.value = _filteredProducts.value?.sortedByDescending { it.name } ?: emptyList()
    }

    fun increasingPriceProduct() {
        _filteredProducts.value = _filteredProducts.value?.sortedBy { it.price } ?: emptyList()
    }

    fun descreasingPriceProduct() {
        _filteredProducts.value = _filteredProducts.value?.sortedByDescending { it.price } ?: emptyList()
    }
}
