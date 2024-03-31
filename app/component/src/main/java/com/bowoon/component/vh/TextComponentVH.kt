package com.bowoon.component.vh

import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.commonutils.textStyle
import com.bowoon.component.data.Components
import com.bowoon.component.databinding.VhTextComponentBinding

class TextComponentVH(
    private val binding: VhTextComponentBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(content: Components.TextComponent?) {
        content?.let {
            binding.apply {
                this.content = it

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
                }
            }
        }
    }
}