package com.rss_reader.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.data.util.StickyHeaderItemDecoration

class StickyDecoration(
    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
) : StickyHeaderItemDecoration.SectionCallback {
    override fun isHeader(position: Int): Boolean =
        (adapter as? ArticleAdapter)?.isHeader(position) ?: false

    override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? =
        (adapter as? ArticleAdapter)?.getHeaderLayout(list, position)
}