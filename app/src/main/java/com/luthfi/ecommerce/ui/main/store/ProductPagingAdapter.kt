package com.luthfi.ecommerce.ui.main.store

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.R as RAPP
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductsResponse
import com.luthfi.ecommerce.databinding.ItemProductGridBinding
import com.luthfi.ecommerce.databinding.ItemProductLinearBinding
import com.luthfi.ecommerce.utils.format

class ProductPagingAdapter(private val context: Context) :
    PagingDataAdapter<ProductsResponse.Data.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var isGridLayout: Boolean = false
    fun setLayoutType(isGrid: Boolean) {
        isGridLayout = isGrid
    }

    inner class ViewHolderGrid(private val binding: ItemProductGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductsResponse.Data.Item?) {
            with(binding) {
                cvProductGrid.startAnimation(AnimationUtils.loadAnimation(itemView.context, RAPP.anim.anim_one))
                tvItemGridProductName.text = data?.productName
                tvItemGridPrice.text = format(data?.productPrice ?: 0)
                tvItemGridStoreName.text = data?.store
                tvItemGridRatingSold.text =
                    context.getString(R.string.rating_sold, data?.productRating, data?.sale)
                Glide.with(context)
                    .load(data?.image)
                    .placeholder(R.drawable.thumbnail_load_product)
                    .error(R.drawable.thumbnail_load_product)
                    .into(ivItemGridImg)

            }
        }
    }

    inner class ViewHolderLinear(private val binding: ItemProductLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductsResponse.Data.Item?) {
            with(binding) {
                cvProductLinear.startAnimation(AnimationUtils.loadAnimation(itemView.context, RAPP.anim.anim_one))
                tvItemLinearProductName.text = data?.productName
                tvItemLinearPrice.text = format(data?.productPrice ?: 0)
                tvItemLinearStoreName.text = data?.store
                tvItemLinearRatingSold.text =
                    context.getString(R.string.rating_sold, data?.productRating, data?.sale)
                Glide.with(context)
                    .load(data?.image)
                    .placeholder(R.drawable.thumbnail_load_product)
                    .error(R.drawable.thumbnail_load_product)
                    .into(ivItemLinearImg)
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ProductsResponse.Data.Item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (isGridLayout) {
            false -> {
                val binding = ItemProductLinearBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderLinear(binding)
            }

            true -> {
                val binding = ItemProductGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderGrid(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        when (isGridLayout) {
            false -> {
                val viewHolderLinear = holder as ViewHolderLinear
                viewHolderLinear.itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(
                        data!!
                    )
                }
                viewHolderLinear.bind(data)
            }

            true -> {
                val viewHolderGrid = holder as ViewHolderGrid
                viewHolderGrid.itemView.setOnClickListener { onItemClickCallback.onItemClicked(data!!) }
                viewHolderGrid.bind(data)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductsResponse.Data.Item>() {
            override fun areItemsTheSame(
                oldItem: ProductsResponse.Data.Item,
                newItem: ProductsResponse.Data.Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ProductsResponse.Data.Item,
                newItem: ProductsResponse.Data.Item
            ): Boolean {
                return oldItem.productId == newItem.productId
            }
        }
    }
}
