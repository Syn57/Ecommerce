package com.luthfi.ecommerce.ui.main.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luthfi.ecommerce.R
import com.ecommerce.uiassets.R as RUI
import com.luthfi.ecommerce.databinding.ItemContainerOnboardingBinding

class ProductImageAdapter(private val context: Context, private val listImg: List<String>) :
    RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemContainerOnboardingBinding.bind(itemView)
        fun bind(data: String) {
            with(binding) {
                Glide.with(context)
                    .load(data)
                    .placeholder(RUI.drawable.thumbnail_load_product)
                    .error(RUI.drawable.thumbnail_load_product)
                    .into(ivItemOnboard)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_container_onboarding, parent, false)
        )

    override fun getItemCount(): Int = listImg.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listImg[position]
        holder.bind(data)
    }
}
