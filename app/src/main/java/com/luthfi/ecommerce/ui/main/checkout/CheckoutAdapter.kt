package com.luthfi.ecommerce.ui.main.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.databinding.ItemChartBinding
import com.luthfi.ecommerce.utils.format

class CheckoutAdapter(
    private val context: Context,
    val increaseCount: (Int) -> Unit,
    val decreaseCount: (Int) -> Unit
) : ListAdapter<Product, RecyclerView.ViewHolder>(ProductDiffCallBack()) {

    private class ProductDiffCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.productId == newItem.productId

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

    inner class ChartViewHolder(private val binding: ItemChartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Product, position: Int) {
            with(binding) {
                cbItemChartCheck.isChecked = data.isChecked
                Glide.with(context)
                    .load(data.image)
                    .placeholder(R.drawable.thumbnail_load_product)
                    .error(R.drawable.thumbnail_load_product)
                    .into(ivItemChartImgProduct)
                tvItemChartProductName.text = data.productName
                tvItemChartVariant.text = data.variantName
                tvItemChartStock.text = if (data.stock < 10) {
                    context.getString(
                        R.string.left_stock,
                        data.stock
                    )
                } else {
                    context.getString(R.string.stock, data.stock)
                }
                if (data.stock < 10) {
                    tvItemChartStock.setTextColor(
                        context.resources.getColor(
                            R.color.e_50,
                            context.theme
                        )
                    )
                }
                tvItemChartPrice.text = format(data.productPrice.plus(data.variantPrice))
                tvItemChartTotalItems.text = data.count.toString()
                cbItemChartCheck.visibility = View.GONE
                ivItemChartDelete.visibility = View.GONE

                btnItemChartDelete.setOnClickListener {
                    decreaseCount(position)
                    tvItemChartTotalItems.text = data.count.toString()
                }
                btnItemChartAdd.setOnClickListener {
                    increaseCount(position)
                    tvItemChartTotalItems.text = data.count.toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemChartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ChartViewHolder
        viewHolder.bind(getItem(position), position)
    }
}
