package com.luthfi.ecommerce.ui.main.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luthfi.ecommerce.core.domain.model.Payment
import com.luthfi.ecommerce.databinding.ItemPaymentMethodBinding

class PaymentAdapter(private val context: Context, val selectPayment: (Payment.Item) -> Unit) :
    ListAdapter<Payment, RecyclerView.ViewHolder>(PaymentDiffCallBack()) {

    private class PaymentDiffCallBack : DiffUtil.ItemCallback<Payment>() {
        override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean =
            oldItem == newItem
    }

    inner class PaymentViewHolder(private val binding: ItemPaymentMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Payment) {
            with(binding) {
                tvItemPaymentMethodTitle.text = data.title
                val adapter = PaymentDetailAdapter(context) { data ->
                    selectPayment(data)
                }
                rvItemPaymentMethod.layoutManager = LinearLayoutManager(context)
                rvItemPaymentMethod.adapter = adapter
                adapter.submitList(data.item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as PaymentViewHolder
        viewHolder.bind(getItem(position))
    }
}
