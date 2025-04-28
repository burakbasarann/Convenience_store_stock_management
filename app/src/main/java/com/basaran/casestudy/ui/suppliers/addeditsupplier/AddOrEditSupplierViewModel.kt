package com.basaran.casestudy.ui.suppliers.addeditsupplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.repository.supplier.SupplierRepository
import com.basaran.casestudy.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrEditSupplierViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _validationErrors = MutableStateFlow<Set<ValidationError>>(emptySet())
    val validationErrors: StateFlow<Set<ValidationError>> = _validationErrors.asStateFlow()

    fun saveSupplier(supplier: Supplier) {
        if (validateSupplier(supplier)) {
            _state.value = UiState.Loading
            viewModelScope.launch {
                try {
                    if (supplier.id == 0L) {
                        supplierRepository.insertSupplier(supplier)
                    } else {
                        supplierRepository.updateSupplier(supplier)
                    }
                    _state.value = UiState.Success
                } catch (e: Exception) {
                    _state.value = UiState.Error(e.localizedMessage ?: "Error saving supplier")
                }
            }
        }
    }

    private fun validateSupplier(supplier: Supplier): Boolean {
        val errors = mutableSetOf<ValidationError>()

        if (supplier.name.isBlank()) {
            errors.add(ValidationError(Field.NAME, "Supplier name cannot be empty"))
        }
        if (supplier.contactPerson.isBlank()) {
            errors.add(ValidationError(Field.CONTACT_PERSON, "Contact person cannot be empty"))
        }
        if (supplier.phone.isBlank()) {
            errors.add(ValidationError(Field.PHONE, "Phone number cannot be empty"))
        }
        if (supplier.email.isBlank()) {
            errors.add(ValidationError(Field.EMAIL, "Email cannot be empty"))
        }
        if (supplier.address.isBlank()) {
            errors.add(ValidationError(Field.ADDRESS, "Address cannot be empty"))
        }

        _validationErrors.value = errors
        return errors.isEmpty()
    }

    data class ValidationError(val field: Field, val message: String)

    enum class Field { NAME, CONTACT_PERSON, PHONE, EMAIL, ADDRESS }
}
