package com.basaran.casestudy.ui.suppliers.addeditsupplier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.databinding.FragmentAddOrEditSupplierBinding
import com.basaran.casestudy.ui.base.BaseFragment
import com.basaran.casestudy.utils.UiState
import com.basaran.casestudy.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddOrEditSupplierFragment : BaseFragment<FragmentAddOrEditSupplierBinding>() {

    private val viewModel: AddOrEditSupplierViewModel by viewModels()
    private lateinit var editSupplier: Supplier

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Supplier>("supplier")?.let {
            editSupplier = it
            fillSupplierDetails(editSupplier)
        }?: run {
            editSupplier = Supplier()
        }
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.apply {
            saveButton.setOnClickListener {
                saveSuppliert()
            }
            cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        showLoading(false)
                        handleSuccess()
                    }
                    is UiState.Loading -> {
                        showLoading(true)
                    }
                    is UiState.Error ->  {
                        showLoading(false)
                        showError(state.message)
                    }
                    is UiState.Idle -> {}
                }
            }

        }
        lifecycleScope.launch {
            viewModel.validationErrors.collect { errors ->
                showValidationErrors(errors)
            }
        }
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.saveButton.isEnabled = !isLoading
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAddOrEditSupplierBinding {
        return FragmentAddOrEditSupplierBinding.inflate(inflater, container, false)
    }

    private fun handleSuccess() {
        findNavController().navigateUp()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showValidationErrors(errors: Set<AddOrEditSupplierViewModel.ValidationError>) {
        binding.apply {
            supplierNameInput.error = errors.find { it.field == AddOrEditSupplierViewModel.Field.NAME }?.message
            contactPersonInput.error = errors.find { it.field == AddOrEditSupplierViewModel.Field.CONTACT_PERSON }?.message
            phoneInput.error = errors.find { it.field == AddOrEditSupplierViewModel.Field.PHONE }?.message
            emailInput.error = errors.find { it.field == AddOrEditSupplierViewModel.Field.EMAIL }?.message
            addressInput.error = errors.find { it.field == AddOrEditSupplierViewModel.Field.ADDRESS }?.message
        }
    }

    private fun fillSupplierDetails(supplier: Supplier) {
        binding.supplierNameEditText.setText(supplier.name)
        binding.contactPersonEditText.setText(supplier.contactPerson)
        binding.phoneEditText.setText(supplier.phone)
        binding.emailEditText.setText(supplier.email)
        binding.addressEditText.setText(supplier.address)
    }

    private fun saveSuppliert() {
        binding.apply {
            val name = supplierNameEditText.text.toString()
            val contact = contactPersonEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val email = emailEditText.text.toString()
            val address = addressEditText.text.toString()

            val supplier = Supplier(
                id = editSupplier.id,
                userId = UserManager.getUserId(),
                name = name,
                contactPerson = contact,
                phone = phone,
                email = email,
                address = address
            )
            viewModel.saveSupplier(supplier)
        }
    }
}