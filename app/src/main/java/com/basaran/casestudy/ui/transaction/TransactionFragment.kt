package com.basaran.casestudy.ui.transaction

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.basaran.casestudy.R
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.data.model.TransactionType
import com.basaran.casestudy.databinding.FragmentTransactionBinding
import com.basaran.casestudy.ui.adapter.RecentTransactionsAdapter
import com.basaran.casestudy.ui.base.BaseFragment
import com.basaran.casestudy.ui.transactions.TransactionViewModel
import com.basaran.casestudy.utils.ExportUtils
import com.basaran.casestudy.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : BaseFragment<FragmentTransactionBinding>() {

    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var transactionAdapter: RecentTransactionsAdapter
    private var transactionsList: List<Transaction> = emptyList()
    private var productsList: List<Product> = emptyList()

    private val createPdfFile = registerForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        uri?.let {
            try {
                ExportUtils.exportToPdf(requireContext(), transactionsList, productsList, it)
                Toast.makeText(requireContext(), getString(R.string.pdf_export_success), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), getString(R.string.error_exporting_pdf, e.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val createCsvFile = registerForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri ->
        uri?.let {
            try {
                ExportUtils.exportToCsv(requireContext(), transactionsList, productsList, it)
                Toast.makeText(requireContext(), getString(R.string.csv_export_success), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), getString(R.string.error_exporting_csv, e.message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun showLoading(isLoading: Boolean) {}

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTransactionBinding {
        return FragmentTransactionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeTransactionsAndProducts()
        setupAddTransactionButton()
        setupExportButtons()
        viewModel.getAllTransactions()
        viewModel.getAllProducts()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTransactions.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observeTransactionsAndProducts() {
        viewModel.transactions.observe(viewLifecycleOwner) { transactionList ->
            transactionsList = transactionList
            updateAdapter()
        }

        viewModel.products.observe(viewLifecycleOwner) { productList ->
            productsList = productList
            updateAdapter()
        }
    }

    private fun updateAdapter() {
        if (transactionsList.isNotEmpty() && productsList.isNotEmpty()) {
            transactionAdapter = RecentTransactionsAdapter(transactionsList, productsList)
            binding.recyclerViewTransactions.adapter = transactionAdapter
        }
    }

    private fun setupAddTransactionButton() {
        binding.buttonAddTransaction.setOnClickListener {
            showAddTransactionDialog()
        }
    }

    private fun setupExportButtons() {
        binding.buttonExportPdf.setOnClickListener {
            if (transactionsList.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.no_transactions), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createPdfFile.launch("transactions_${System.currentTimeMillis()}.pdf")
        }

        binding.buttonExportCsv.setOnClickListener {
            if (transactionsList.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.no_transactions), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createCsvFile.launch("transactions_${System.currentTimeMillis()}.csv")
        }
    }

    private fun showAddTransactionDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_transaction, null)

        val spinnerTransactionType = dialogView.findViewById<Spinner>(R.id.spinnerTransactionType)
        val editTextProductId = dialogView.findViewById<EditText>(R.id.editTextProductId)
        val editTextQuantity = dialogView.findViewById<EditText>(R.id.editTextQuantity)
        val editTextNotes = dialogView.findViewById<EditText>(R.id.editTextNotes)

        val types = TransactionType.entries.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTransactionType.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.new_transaction))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save)) { dialog, _ ->
                val selectedType = TransactionType.valueOf(spinnerTransactionType.selectedItem as String)
                val productId = editTextProductId.text.toString().toLongOrNull() ?: 0L
                val quantity = editTextQuantity.text.toString().toIntOrNull() ?: 0
                val notes = editTextNotes.text.toString()

                val newTransaction = Transaction(
                    date = System.currentTimeMillis(),
                    userId = UserManager.getUserId(),
                    type = selectedType,
                    productId = productId,
                    quantity = quantity,
                    notes = notes
                )
                viewModel.addTransaction(newTransaction)

                val quantityChange = if (newTransaction.type == TransactionType.SALE) {
                    -newTransaction.quantity
                } else {
                    newTransaction.quantity
                }

                viewModel.updateProductStock(newTransaction.productId, quantityChange)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
