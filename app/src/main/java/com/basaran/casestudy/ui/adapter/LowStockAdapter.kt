package com.basaran.casestudy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.databinding.ItemLowStockBinding

class LowStockAdapter(
    private val products: List<Product>
) : RecyclerView.Adapter<LowStockAdapter.LowStockViewHolder>() {

    inner class LowStockViewHolder(private val binding: ItemLowStockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productNameText.text = product.name
            binding.productDescriptionText.text = product.description
            binding.productCategoryText.text = "Kategori: ${product.category}"
            binding.supplierIdText.text = "Tedarikçi ID: ${product.supplierId}"
            binding.priceText.text = "Fiyat: ₺${product.price}"
            binding.barcodeText.text = "Barkod: ${product.barcode}"
            binding.stockInfoText.text = "Stok: ${product.currentStock} / Minimum Stok: ${product.minStock}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LowStockViewHolder {
        val binding = ItemLowStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LowStockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LowStockViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}