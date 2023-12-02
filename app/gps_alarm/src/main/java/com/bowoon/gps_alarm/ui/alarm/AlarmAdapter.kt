package com.bowoon.gps_alarm.ui.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.gpsAlarm.databinding.VhAlarmBinding
import com.bowoon.gps_alarm.data.Address

class AlarmAdapter : ListAdapter<Address, AlarmVH>(diff) {
    companion object {
        private val diff = object : DiffUtil.ItemCallback<Address>() {
            override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean = oldItem.name == newItem.name && oldItem.roadAddress == newItem.roadAddress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmVH =
        AlarmVH(VhAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: AlarmVH, position: Int) {
        currentList[position]?.let {
            holder.bind(it)
        }
    }
}

class AlarmVH(
    private val binding: VhAlarmBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(address: Address?) {
        address?.let {
            binding.apply {
                item = it
            }
        }
    }
}