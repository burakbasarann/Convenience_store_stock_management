package com.basaran.casestudy.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.databinding.FragmentDashboardBinding
import com.basaran.casestudy.ui.adapter.LowStockAdapter
import com.basaran.casestudy.ui.adapter.RecentTransactionsAdapter
import com.basaran.casestudy.ui.base.BaseFragment
import com.basaran.casestudy.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var lowStockAdapter: LowStockAdapter
    private lateinit var recentTransactionsAdapter: RecentTransactionsAdapter
    private var filterProducts: List<Product> = emptyList()
    private var allProducts: List<Product> = emptyList()
    private var allTransactions: List<Transaction> = emptyList()

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.lowStockRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recentTransactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModel() {
        viewModel.lowStockProducts.observe(viewLifecycleOwner) { products ->
            filterProducts = products
            updateAdapter()
        }

        viewModel.recentTransactions.observe(viewLifecycleOwner) { transactions ->
            allTransactions = transactions
            updateAdapter()
        }

        viewModel.allProducts.observe(viewLifecycleOwner) { allProductsDb ->
            allProducts = allProductsDb
            updateAdapter()
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    setLoadingState(true)
                }

                is UiState.Success -> {
                    setLoadingState(false)
                }

                is UiState.Error -> {
                    setLoadingState(false)
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.Idle -> {}
            }
        }
    }

    private fun updateAdapter() {
        if (allTransactions.isNotEmpty()) {
            recentTransactionsAdapter = RecentTransactionsAdapter(allTransactions, allProducts)
            binding.recentTransactionsRecyclerView.adapter = recentTransactionsAdapter
        }
        if (allProducts.isNotEmpty()) {
            lowStockAdapter = LowStockAdapter(filterProducts)
            binding.lowStockRecyclerView.adapter = lowStockAdapter
        }
    }
} 