package com.bowoon.commonutils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollEventListener(
    private val load: LoadMore? = null
) : RecyclerView.OnScrollListener() {
    companion object {
        var loading = false
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

//        (recyclerView.layoutManager as? GridLayoutManager)?.let {
        (recyclerView.layoutManager as? LinearLayoutManager)?.let {
            val size = it.itemCount
            val lastPosition = it.findLastVisibleItemPosition()

            if (!loading && size - lastPosition <= 2) {
                loading = true
                load?.loadMore()
            }
        }
    }
}

interface LoadMore {
    fun loadMore()
}