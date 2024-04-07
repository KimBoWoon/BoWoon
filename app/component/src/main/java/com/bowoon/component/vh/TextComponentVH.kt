package com.bowoon.component.vh

import android.content.Intent
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.commonutils.ViewAdapter.onDebounceClickListener
import com.bowoon.commonutils.textStyle
import com.bowoon.component.base.BaseComponentVH
import com.bowoon.component.data.ComponentEvent
import com.bowoon.component.data.Components
import com.bowoon.component.databinding.VhTextComponentBinding
import com.bowoon.component.ui.WebView

class TextComponentVH(
    private val binding: VhTextComponentBinding,
    private val tabEvent: ((Int) -> Unit)? = null
) : BaseComponentVH<Components.TextComponent>(binding) {
    companion object {
        private const val TAG = "component_text_component_vh"
    }

    override fun bind(component: Components.TextComponent?) {
        component?.let {
            binding.apply {
                this.component = it

                tvTextComponent.apply {
                    layoutParams.apply {
                        (this as? MarginLayoutParams)?.apply {
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
                    text = it.style?.textStyle(it.text ?: "")
                    it.clickEvent?.let { clickEvent ->
                        when (ComponentEvent.valueOf(clickEvent.type ?: "")) {
                            ComponentEvent.WEB -> {
                                onDebounceClickListener {
                                    binding.root.context.startActivity(
                                        Intent(binding.root.context, WebView::class.java).apply {
                                            putExtra("url", clickEvent.url)
                                        }
                                    )
                                }
                            }
                            ComponentEvent.MOVE_TAB -> {
                                onDebounceClickListener {
                                    clickEvent.position?.let {
                                        tabEvent?.invoke(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}