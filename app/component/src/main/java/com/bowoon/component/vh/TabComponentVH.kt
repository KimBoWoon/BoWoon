package com.bowoon.component.vh

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.component.adapters.TabContentAdapter
import com.bowoon.component.base.BaseComponentVH
import com.bowoon.component.data.Components
import com.bowoon.component.databinding.VhTabComponentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabComponentVH(
    private val binding: VhTabComponentBinding
) : BaseComponentVH<Components.TabComponent>(binding) {
    private val tabEvent: (Int) -> Unit = { position ->
        binding.tlTabComponent.getTabAt(position)?.let {
            binding.tlTabComponent.selectTab(it)
        }
    }

    override fun bind(component: Components.TabComponent?) {
        component?.let {
            binding.apply {
                this.component = it

                llTabComponentRoot.apply {
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
                }

                tlTabComponent.apply {
                    tabMode = it.mode ?: TabLayout.MODE_FIXED
                }

                vpTabContent.apply {
                    adapter = TabContentAdapter(binding.root.context as FragmentActivity, it.tabs, tabEvent)
                }

                TabLayoutMediator(tlTabComponent, vpTabContent) { tab, index ->
                    tab.text = it.tabs?.get(index)?.name
                }.attach()
            }
        }
    }
}