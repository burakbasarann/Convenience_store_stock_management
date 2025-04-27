package com.basaran.casestudy.ui.products.addeditproduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.repository.ProductRepository
import com.basaran.casestudy.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrEditProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _validationErrors = MutableStateFlow<Set<ValidationError>>(emptySet())
    val validationErrors: StateFlow<Set<ValidationError>> = _validationErrors.asStateFlow()

    fun saveProduct(product: Product) {
        when (validateProduct(product)) {
            true -> {
                _state.value = UiState.Loading
                viewModelScope.launch {
                    try {
                        productRepository.insertProduct(product)
                        _state.value = UiState.Success
                    } catch (e: Exception) {
                        _state.value = UiState.Error(e.localizedMessage ?: "Error saving product")
                    }
                }
            }
            false -> Unit
        }
    }

    private fun validateProduct(product: Product): Boolean {
        val errors = mutableSetOf<ValidationError>()

        if (product.name.isBlank()) {
            errors.add(ValidationError(Field.NAME, "Product name cannot be empty"))
        }

        if (product.price <= 0) {
            errors.add(ValidationError(Field.PRICE, "Price must be greater than 0"))
        }

        if (product.category.isBlank()) {
            errors.add(ValidationError(Field.CATEGORY, "Category cannot be empty"))
        }

        if (product.barcode.isBlank()) {
            errors.add(ValidationError(Field.BARCODE, "Barcode cannot be empty"))
        }

         if (product.minStock <= 0) {
            errors.add(ValidationError(Field.MIN_STOCK, "Minimum Stock must be greater than 0"))
        }

        _validationErrors.value = errors
        return errors.isEmpty()
    }


    data class ValidationError(val field: Field, val message: String)

    enum class Field { NAME, PRICE, CATEGORY, BARCODE, MIN_STOCK }
}