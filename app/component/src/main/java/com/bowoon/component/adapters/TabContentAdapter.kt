package com.bowoon.component.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bowoon.component.data.Tab
import com.bowoon.component.ui.ContentFragment

class TabContentAdapter(
    private val fa: FragmentActivity,
    private val items: List<Tab>? = null,
    private val tabEvent: ((Int) -> Unit)? = null
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = items?.size ?: 0

    override fun createFragment(position: Int): Fragment =
        ContentFragment(items?.get(position), tabEvent)
}