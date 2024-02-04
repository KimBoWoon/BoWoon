package com.bowoon.gps_alarm.ui.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.gpsAlarm.databinding.VhAlarmBinding
import com.bowoon.gps_alarm.data.Address

class AlarmAdapter(
    private val handler: AlarmFragment.ClickHandler
) : ListAdapter<Address, AlarmVH>(diff) {
    companion object {
        private val diff = object : DiffUtil.ItemCallback<Address>() {
            override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean = oldItem.longitude == newItem.longitude && oldItem.latitude == newItem.latitude
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmVH =
        AlarmVH(VhAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false), handler)

    override fun onBindViewHolder(holder: AlarmVH, position: Int) {
        currentList[position]?.let {
            holder.bind(it)
        }
    }
}

class AlarmVH(
    private val binding: VhAlarmBinding,
    private val handler: AlarmFragment.ClickHandler
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(address: Address?) {
        address?.let {
            binding.apply {
                item = it
                vh = this@AlarmVH
            }
        }
    }

    fun goToDetail(address: Address?) {
        address?.let {
            handler.goToDetail(address)
        }
    }

    fun removeAlarm(address: Address?) {
        address?.let {
            handler.removeAlarm(it)
        }
    }
}