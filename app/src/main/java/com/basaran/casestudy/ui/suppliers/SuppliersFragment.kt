package com.basaran.casestudy.ui.suppliers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basaran.casestudy.R
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.databinding.FragmentProductsBinding
import com.basaran.casestudy.databinding.FragmentSuppliersBinding
import com.basaran.casestudy.ui.adapter.ProductsAdapter
import com.basaran.casestudy.ui.adapter.SupplierAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SuppliersFragment : Fragment() {

    private var _binding: FragmentSuppliersBinding? = null
    private val binding get() = _binding!!

    private lateinit var supplierAdapter: SupplierAdapter
    private val viewModel: SuppliersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuppliersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
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
    }

    private fun setupViews() {
        binding.fabAddSupplier.setOnClickListener {
            findNavController().navigate(R.id.action_suppliersFragment_to_addOrEditSupplierFragment)
        }
    }
    private fun navigateToItemSupplier(supplier: Supplier) {
        val bundle = Bundle()
        bundle.putParcelable("supplier", supplier)
        findNavController().navigate(R.id.action_suppliersFragment_to_addOrEditSupplierFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
