package com.basaran.casestudy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.databinding.ItemProductBinding

class ProductsAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onEditClick: (Product) -> Unit
) : ListAdapter<Product, ProductsAdapter.ProductViewHolder>(DiffCallback()) {

    private var isEditMode: Boolean = false

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productNameTextView.text = product.name
            binding.productStockTextView.text = "Stock: ${product.currentStock}"
            binding.editImageView.visibility = if (isEditMode) View.VISIBLE else View.GONE

            binding.root.setOnClickListener {
                onItemClick(product)
            }
            binding.editImageView.setOnClickListener {
                onEditClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
    }

    fun setEditMode(enabled: Boolean) {
        isEditMode = enabled
        notifyDataSetChanged()
    }
}
