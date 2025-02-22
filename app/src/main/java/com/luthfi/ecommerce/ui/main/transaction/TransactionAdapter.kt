package com.luthfi.ecommerce.ui.main.transaction

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.R as RAPP
import com.luthfi.ecommerce.core.data.datasource.api.responses.TransactionResponse
import com.luthfi.ecommerce.databinding.ItemTransactionBinding
import com.luthfi.ecommerce.utils.format

class TransactionAdapter(
    private val context: Context,
    val moveToStatus: (TransactionResponse.Data) -> Unit
) : ListAdapter<TransactionResponse.Data, RecyclerView.ViewHolder>(TransactionDiffCallBack()) {
    private class TransactionDiffCallBack : DiffUtil.ItemCallback<TransactionResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: TransactionResponse.Data,
            newItem: TransactionResponse.Data
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: TransactionResponse.Data,
            newItem: TransactionResponse.Data
        ): Boolean =
            oldItem == newItem
    }

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TransactionResponse.Data) {
            with(binding) {
                cvTransaction.startAnimation(AnimationUtils.loadAnimation(itemView.context, RAPP.anim.anim_one))
                tvItemTransactionDate.text = data.date
                Glide.with(context)
                    .load(data.image)
                    .placeholder(R.drawable.thumbnail_load_product)
                    .into(ivItemTransactionImgProduct)
                tvItemTransactionProductName.text = data.name
                tvItemTransactionTotalItem.text =
                    context.resources.getString(R.string.item_s, data.items?.size)
                tvItemTransactionTotalPayValue.text = format(data.total!!)
                btnItemTransactionReview.isVisible = data.review == null || data.rating == null
                btnItemTransactionReview.setOnClickListener {
                    moveToStatus(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as TransactionViewHolder
        viewHolder.bind(getItem(position))
    }
}
