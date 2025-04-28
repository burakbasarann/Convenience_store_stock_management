package com.basaran.casestudy.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.basaran.casestudy.databinding.FragmentTransactionBinding
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.data.model.TransactionType
import com.basaran.casestudy.ui.adapter.RecentTransactionsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModels()

    private lateinit var transactionAdapter: RecentTransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeTransactions()
        setupAddTransactionButton()

        viewModel.getAllTransactions()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTransactions.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observeTransactions() {
        viewModel.transactions.observe(viewLifecycleOwner) { transactionList ->
            transactionAdapter = RecentTransactionsAdapter(transactionList, null)
            binding.recyclerViewTransactions.adapter = transactionAdapter
        }
    }

    private fun setupAddTransactionButton() {
        binding.buttonAddTransaction.setOnClickListener {
            val newTransaction = Transaction(
                date = System.currentTimeMillis(),
                type = TransactionType.RESTOCK,
                productId = 1L,
                quantity = 5,
                notes = "Yeni stok eklendi"
            )
            viewModel.addTransaction(newTransaction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
