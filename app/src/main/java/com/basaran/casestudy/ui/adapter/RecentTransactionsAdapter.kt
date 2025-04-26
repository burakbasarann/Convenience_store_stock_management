package com.basaran.casestudy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.basaran.casestudy.databinding.ItemRecentTransactionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecentTransactionsAdapter(
    private val transactions: List<Transaction>,
    private val products: List<Product>
) : RecyclerView.Adapter<RecentTransactionsAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: ItemRecentTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            val product = products.find { it.id == transaction.productId }
            val productName = product?.name ?: "Bilinmeyen Ürün"

            binding.transactionTypeTextView.text = transaction.type.name
            binding.productIdTextView.text = "Ürün Adı: $productName  (TransactionID: ${transaction.productId})"
            binding.dateTextView.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(transaction.date))
            binding.quantityTextView.text = "Adet: ${transaction.quantity}"
            binding.notesTextView.text = "Not: ${transaction.notes ?: "-"}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemRecentTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size
}