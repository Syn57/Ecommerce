package com.luthfi.ecommerce.ui.main.review

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luthfi.ecommerce.R
import com.ecommerce.uiassets.R as RUI
import com.luthfi.ecommerce.core.data.datasource.api.responses.ReviewResponse
import com.luthfi.ecommerce.databinding.ItemReviewBinding

class ReviewAdapter(
    private val context: Context,
    private val listReview: List<ReviewResponse.Data?>?
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemReviewBinding.bind(itemView)
        fun bind(data: ReviewResponse.Data?) {
            with(binding) {
                tvItemReviewProfileLetter.text = data?.userName?.get(0).toString()
                tvItemReviewProfileLetter.isVisible = false
                tvItemReviewName.text = data?.userName
                rbItemReviewRating.rating = data?.userRating?.toFloat()!!
                tvItemReviewContent.text = data.userReview
                Glide.with(context)
                    .load(data.userImage)
                    .placeholder(RUI.drawable.thumbnail_load_product)
                    .error(RUI.drawable.thumbnail_load_product)
                    .circleCrop()
                    .centerCrop()
                    .into(ivItemReviewProfile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_review, parent, false)
        )

    override fun getItemCount(): Int = listReview?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listReview!![position]
        holder.bind(data)
    }
}
