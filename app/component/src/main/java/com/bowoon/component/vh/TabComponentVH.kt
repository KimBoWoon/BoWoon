package com.bowoon.component.vh

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.component.data.Components
import com.bowoon.component.data.Tab
import com.bowoon.component.databinding.VhTabComponentBinding
import com.bowoon.component.ui.ContentFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabComponentVH(
    private val binding: VhTabComponentBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(content: Components.TabComponent?) {
        content?.let {
            binding.apply {
                this.content = it

                tlTabComponent.apply {
                    layoutParams.apply {
                        (this as? ViewGroup.MarginLayoutParams)?.apply {
                            setMargins((it.startMargin ?: 0).dp, (it.topMargin ?: 0).dp, (it.endMargin ?: 0).dp, (it.bottomMargin ?: 0).dp)
                        }
                        width = if (it.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                            ViewGroup.LayoutParams.MATCH_PARENT
                        } else if (it.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        } else {
                            it.width?.dp
                        } ?: 0
                        height = if (it.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                            ViewGroup.LayoutParams.MATCH_PARENT
                        } else if (it.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        } else {
                            it.height?.dp
                        } ?: 0
                    }
                    tabMode = it.mode ?: TabLayout.MODE_FIXED
                }

                vpTabContent.apply {
                    adapter = ViewPagerAdapter(binding.root.context as FragmentActivity, it.tabs)
                }

                TabLayoutMediator(tlTabComponent, vpTabContent) { tab, index ->
                    tab.text = it.tabs?.get(index)?.name
                }.attach()
            }
        }
    }
}

class ViewPagerAdapter(
    private val fa: FragmentActivity,
    private val items: List<Tab>? = null
) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = items?.size ?: 0

    override fun createFragment(position: Int): Fragment =
        ContentFragment(items?.get(position))
}