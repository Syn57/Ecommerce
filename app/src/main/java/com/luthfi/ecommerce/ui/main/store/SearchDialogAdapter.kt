package com.luthfi.ecommerce.ui.main.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.databinding.ItemSearchDialogBinding

class SearchDialogAdapter(private val listRecommendation: List<String>) :
    RecyclerView.Adapter<SearchDialogAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSearchDialogBinding.bind(itemView)
        fun bind(data: String) {
            with(binding) {
                tvItemRecommendationSearchDialog.text = data
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_dialog, parent, false)
        )

    override fun getItemCount(): Int = listRecommendation.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listRecommendation[position]
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listRecommendation[position]) }
        holder.bind(data)
    }
}
