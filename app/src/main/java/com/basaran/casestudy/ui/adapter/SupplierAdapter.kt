package com.basaran.casestudy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Supplier
import com.basaran.casestudy.databinding.ItemSupplierBinding

class SupplierAdapter(
    private val onItemClick: (Supplier) -> Unit,
) : RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder>() {
    private var suppliers: List<Supplier> = emptyList()

    inner class SupplierViewHolder(private val binding: ItemSupplierBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(supplier: Supplier) {
            binding.textViewSupplierName.text = supplier.name
            binding.textViewContactPerson.text = "Yetkili: ${supplier.contactPerson}"
            binding.textViewPhone.text = "Telefon: ${supplier.phone}"

            binding.root.setOnClickListener {
                onItemClick(supplier)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        val binding = ItemSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupplierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        holder.bind(suppliers[position])
    }

    override fun getItemCount(): Int = suppliers.size

    fun submitList(list: List<Supplier>) {
        suppliers = list
        notifyDataSetChanged()
    }
}

