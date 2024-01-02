package com.bowoon.lol.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bowoon.lol.databinding.SpinnerItemBinding

class VersionAdapter(
    context: Context,
    private val resource: Int,
    private val items: List<String>
) : ArrayAdapter<String>(context, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
        SpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            tvSettingVersion.text = items[position]
        }.root

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
        SpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            tvSettingVersion.text = items[position]
        }.root

    override fun getItem(position: Int): String? = items[position]
    override fun getCount(): Int = items.size
}