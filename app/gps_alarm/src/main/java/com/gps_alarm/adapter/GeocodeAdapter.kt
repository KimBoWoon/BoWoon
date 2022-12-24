package com.gps_alarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import com.bowoon.android.gps_alarm.databinding.VhGeocodeBinding
import com.domain.gpsAlarm.dto.Addresses
import com.gps_alarm.base.BaseVH
import util.Log

class GeocodeAdapter : ListAdapter<Addresses, GeocodeVH>(diffutil) {
    companion object {
        private val diffutil = object : ItemCallback<Addresses>() {
            override fun areItemsTheSame(oldItem: Addresses, newItem: Addresses): Boolean =
                oldItem.roadAddress == newItem.roadAddress && oldItem.jibunAddress == newItem.jibunAddress

            override fun areContentsTheSame(oldItem: Addresses, newItem: Addresses): Boolean =
                oldItem.x == newItem.x && oldItem.y == newItem.y
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeocodeVH {
        return GeocodeVH(VhGeocodeBinding.inflate(LayoutInflater.from(parent.context), null, false))
    }

    override fun onBindViewHolder(holder: GeocodeVH, position: Int) {
        currentList[position]?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = currentList.size
}

class GeocodeVH(
    binding: VhGeocodeBinding
) : BaseVH<VhGeocodeBinding, Addresses>(binding) {
    override fun bind(geocode: Addresses?) {
        runCatching {
            requireNotNull(geocode)
        }.onSuccess {
            binding.apply {
                item = it
            }
        }.onFailure {
            Log.e("geocode is null!")
        }
    }
}