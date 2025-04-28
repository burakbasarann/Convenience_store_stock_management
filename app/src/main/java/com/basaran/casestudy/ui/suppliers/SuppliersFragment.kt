package com.basaran.casestudy.ui.suppliers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basaran.casestudy.R
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.databinding.FragmentSuppliersBinding
import com.basaran.casestudy.ui.adapter.SupplierAdapter
import com.basaran.casestudy.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SuppliersFragment : BaseFragment<FragmentSuppliersBinding>() {

    private lateinit var supplierAdapter: SupplierAdapter
    private val viewModel: SuppliersViewModel by viewModels()
    override fun showLoading(isLoading: Boolean) {}

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSuppliersBinding {
        return FragmentSuppliersBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupViews()
    }

    private fun setupRecyclerView() {
        supplierAdapter = SupplierAdapter(onItemClick = { supplier ->
            navigateToItemSupplier(supplier)

        })
        binding.recyclerViewSuppliers.apply {
            adapter = supplierAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        viewModel.suppliers.observe(viewLifecycleOwner) { suppliers ->
            supplierAdapter.submitList(suppliers)
        }

        viewModel.filteredSuppliers.observe(viewLifecycleOwner) { suppliers ->
            supplierAdapter.submitList(suppliers)
        }
    }

    private fun setupViews() {
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.filter_options, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterSpinner.adapter = adapter

        binding.filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (selectedItemView == null) return
                when (position) {
                    0 -> viewModel.sortSuppliersAscending()
                    1 -> viewModel.sortSuppliersDescending()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        binding.fabAddSupplier.setOnClickListener {
            findNavController().navigate(R.id.action_suppliersFragment_to_addOrEditSupplierFragment)
        }
    }
    private fun navigateToItemSupplier(supplier: Supplier) {
        val bundle = Bundle()
        bundle.putParcelable("supplier", supplier)
        findNavController().navigate(R.id.action_suppliersFragment_to_addOrEditSupplierFragment, bundle)
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { //TODO Buraya Bak
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { //TODO Buraya Bak
            }

            override fun afterTextChanged(editable: Editable?) {
                viewModel.filterSuppliers(editable.toString())
            }
        })
    }
}
