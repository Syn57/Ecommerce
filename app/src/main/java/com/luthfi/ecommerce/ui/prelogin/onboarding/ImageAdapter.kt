package com.luthfi.ecommerce.ui.prelogin.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.databinding.ItemContainerOnboardingBinding

class ImageAdapter(private val listImg: List<Int>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemContainerOnboardingBinding.bind(itemView)
        fun bind(data: Int) {
            with(binding) {
                ivItemOnboard.setImageResource(data)
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
