package util

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollEventListener(
    private val context: Context,
    private val spanCount: Int = 1,
    private val orientation: Int = RecyclerView.VERTICAL,
    private val reverseLayout: Boolean = false
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {

}

class RecyclerViewScrollEventListener(
    private val load: LoadMore? = null
) : RecyclerView.OnScrollListener() {
    companion object {
        var loading = false
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

//        when (newState) {
//            RecyclerView.SCROLL_STATE_IDLE -> {
//                Log.d("RecyclerView.SCROLL_STATE_IDLE")
//            }
//            RecyclerView.SCROLL_STATE_DRAGGING -> {
//                Log.d("RecyclerView.SCROLL_STATE_DRAGGING")
//            }
//            RecyclerView.SCROLL_STATE_SETTLING -> {
//                Log.d("RecyclerView.SCROLL_STATE_SETTLING")
//            }
//        }
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