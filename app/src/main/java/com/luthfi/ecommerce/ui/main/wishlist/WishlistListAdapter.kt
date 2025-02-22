package com.luthfi.ecommerce.ui.main.wishlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.R as RAPP
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.databinding.ItemWishlistGridBinding
import com.luthfi.ecommerce.databinding.ItemWishlistLinearBinding
import com.luthfi.ecommerce.utils.format

class WishlistListAdapter(
    private val context: Context,
    val deleteItem: (String) -> Unit,
    val addChart: (Product) -> Unit
) : ListAdapter<Product, RecyclerView.ViewHolder>(ProductDiffCallBack()) {

    private var isGridLayout: Boolean = false
    fun setLayoutType(isGrid: Boolean) {
        isGridLayout = isGrid
    }

    private class ProductDiffCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.productId == newItem.productId

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

    inner class ViewHolderGrid(private val binding: ItemWishlistGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Product) {
            with(binding) {
                cvWishlistGrid.startAnimation(AnimationUtils.loadAnimation(itemView.context, RAPP.anim.anim_one))
                tvItemWishlistGridProductName.text = data.productName
                tvItemWishlistGridPrice.text = format(data.productPrice + data.variantPrice)
                tvItemWishlistGridStoreName.text = data.store
                tvItemWishlistGridRatingSold.text =
                    context.getString(R.string.rating_sold, data.productRating, data.sale)
                Glide.with(context)
                    .load(data.image)
                    .placeholder(R.drawable.thumbnail_load_product)
                    .error(R.drawable.thumbnail_load_product)
                    .into(ivItemWishlistGridImg)
                btnItemWishlistGridDelete.setOnClickListener {
                    deleteItem(data.productId)
                }
                btnItemWishlistGridAddChart.setOnClickListener {
                    addChart(data)
                }
            }
        }
    }

    inner class ViewHolderLinear(private val binding: ItemWishlistLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Product) {
            with(binding) {
                cvWishlistLinear.startAnimation(AnimationUtils.loadAnimation(itemView.context, RAPP.anim.anim_one))
                tvItemWishlistLinearProductName.text = data.productName
                tvItemWishlistLinearPrice.text = format(data.productPrice + data.variantPrice)
                tvItemWishlistLinearStoreName.text = data.store
                tvItemWishlistLinearRatingSold.text =
                    context.getString(R.string.rating_sold, data.productRating, data.sale)
                Glide.with(context)
                    .load(data.image)
                    .placeholder(R.drawable.thumbnail_load_product)
                    .error(R.drawable.thumbnail_load_product)
                    .into(ivItemWishlistLinearImg)
                btnItemWishlistLinearDelete.setOnClickListener {
                    deleteItem(data.productId)
                }
                btnItemWishlistLinearAddChart.setOnClickListener {
                    addChart(data)
                }
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (isGridLayout) {
            false -> {
                val binding = ItemWishlistLinearBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderLinear(binding)
            }

            true -> {
                val binding = ItemWishlistGridBinding.inflate(
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
                        data
                    )
                }
                viewHolderLinear.bind(data)
            }

            true -> {
                val viewHolderGrid = holder as ViewHolderGrid
                viewHolderGrid.itemView.setOnClickListener { onItemClickCallback.onItemClicked(data) }
                viewHolderGrid.bind(data)
            }
        }
    }

}