package com.luthfi.ecommerce.ui.main.checkout

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.R
import com.google.android.material.color.MaterialColors
import com.luthfi.ecommerce.core.domain.model.Payment
import com.luthfi.ecommerce.databinding.ItemPaymentMethodDetailBinding


class PaymentDetailAdapter(
    private val context: Context,
    val selectPayment: (Payment.Item) -> Unit
) : ListAdapter<Payment.Item, RecyclerView.ViewHolder>(PaymentDiffCallBack()) {

    private class PaymentDiffCallBack : DiffUtil.ItemCallback<Payment.Item>() {
        override fun areItemsTheSame(oldItem: Payment.Item, newItem: Payment.Item): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Payment.Item, newItem: Payment.Item): Boolean =
            oldItem == newItem
    }

    inner class PaymentDetailViewHolder(private val binding: ItemPaymentMethodDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Payment.Item) {
            with(binding) {
                Glide.with(context)
                    .load(data.image)
                    .error(com.luthfi.ecommerce.R.drawable.thumbnail_load_product)
                    .into(ivItemPaymentDetailPartyLogo)
                itemView.alpha = if (data.status != false) 1f else 0.5f
                val color = MaterialColors.getColor(
                    context,
                    R.attr.colorSurfaceContainerHighest,
                    Color.GRAY
                )
                if (data.status == false) {
                    itemView.setBackgroundColor(color)
                }
                itemView.isEnabled = data.status!!
                vgItemPaymentMethodDetail.isEnabled = data.status!!
                itemView.setOnClickListener {
                    selectPayment(data)
                }
                tvItemPaymentDetailPartyName.text = data.label
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPaymentMethodDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as PaymentDetailViewHolder
        viewHolder.bind(getItem(position))
    }
}
