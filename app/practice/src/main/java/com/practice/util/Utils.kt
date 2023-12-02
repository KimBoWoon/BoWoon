package com.practice.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.data.util.ScreenUtils.dp

class PokemonDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)
        val size = parent.adapter?.itemCount ?: 0
        if (position in 0 .. size) {
            when {
                position % 2 == 0 -> {
                    outRect.left = 10.dp
                    outRect.right = 5.dp
                }
                position % 2 == 1 -> {
                    outRect.left = 5.dp
                    outRect.right = 10.dp
                }
            }
            outRect.top = 10.dp
        }
    }
}