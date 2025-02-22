package com.luthfi.ecommerce.ui.main.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecommerce.uiassets.R
import com.luthfi.ecommerce.core.domain.model.Notification
import com.luthfi.ecommerce.databinding.ItemNotificationBinding

class NotificationAdapter(private val context: Context, val updateIsCheck: (Int, Boolean) -> Unit) :
    ListAdapter<Notification, RecyclerView.ViewHolder>(NotificationDiffCallBack()) {

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Notification) {
            with(binding) {
                itemView.setBackgroundColor(
                    when (data.isChecked) {
                        true -> {
                            context.resources.getColor(R.color.white, context.theme)
                        }

                        false -> {
                            context.resources.getColor(R.color.p_90, context.theme)
                        }
                    }
                )
                Glide.with(context)
                    .load(data.image)
                    .placeholder(R.drawable.thumbnail_load_product)
                    .error(R.drawable.thumbnail_load_product)
                    .into(ivItemNotificationImgThumbnail)
                tvItemNotificationInfo.text = data.type
                tvItemNotificationSuccess.text = data.title
                tvItemNotificationDescription.text = data.body
                tvItemNotificationDateTime.text =
                    context.getString(R.string.date_time_notification, data.date, data.time)
                itemView.setOnClickListener {
                    updateIsCheck(data.id, true)
                }
            }
        }
    }

    private class NotificationDiffCallBack : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as NotificationViewHolder
        viewHolder.bind(getItem(position))
    }
}
