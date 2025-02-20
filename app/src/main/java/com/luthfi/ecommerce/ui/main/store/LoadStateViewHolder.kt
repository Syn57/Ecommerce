package com.luthfi.ecommerce.ui.main.store

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.luthfi.ecommerce.R
import com.luthfi.ecommerce.databinding.LoadStateItemBinding

class LoadStateViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.load_state_item, parent, false)
) {
    private val binding = LoadStateItemBinding.bind(itemView)
    private val progressBar: ProgressBar = binding.pbLoadStateItem

    fun bind(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
    }
}
