package com.bowoon.commonutils

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderItemDecoration(
    private val sectionCallback: SectionCallback
) : RecyclerView.ItemDecoration() {
    private lateinit var header: View

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topChild = parent.getChildAt(0) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)

        if (topChildPosition == RecyclerView.NO_POSITION) return

        /* 헤더 */
        val currentHeader = sectionCallback.getHeaderLayoutView(parent, topChildPosition) ?: return
//        val currentHeader = when (sectionCallback.isHeader(topChildPosition)) {
//            true -> {
//                header = sectionCallback.getHeaderLayoutView(parent, topChildPosition) ?: return
//                header
//            }
//            else -> header
//        }

        /* View의 레이아웃 설정 */
        fixLayoutSize(parent, currentHeader, topChild.measuredHeight)

        val contactPoint = currentHeader.bottom
        val childInContact = getChildInContact(parent, contactPoint) ?: return
        val childAdapterPosition = parent.getChildAdapterPosition(childInContact)

        if (childAdapterPosition == RecyclerView.NO_POSITION) return

        when (sectionCallback.isHeader(childAdapterPosition)) {
            true -> moveHeader(c, currentHeader, childInContact)
            else -> drawHeader(c, currentHeader)
        }
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? =
        parent.children.find { child -> child.bottom > contactPoint && child.top <= contactPoint }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, nextHeader.top - currentHeader.height.toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    private fun fixLayoutSize(parent: ViewGroup, view: View, height: Int) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.EXACTLY
        )
        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    interface SectionCallback {
        fun isHeader(position: Int): Boolean
        fun getHeaderLayoutView(list: RecyclerView, position: Int): View?
    }
}