package util

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import util.ViewAdapter.DebounceClickValues.MIN_CLICK_INTERVAL

object ViewAdapter {
    @JvmStatic
    @BindingAdapter("loadImage")
    fun ImageView?.loadImage(url: String?) {
        this ?: run {
            Log.e("ImageView is null")
            return
        }
        url ?: run {
            Log.e("url is null")
            return
        }

        Glide.with(this)
            .load(url)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(this)
    }

    @JvmStatic
    @BindingAdapter("onDebounceClickListener")
    fun View?.onDebounceClickListener(listener: View.OnClickListener? = null) {
        this ?: run {
            Log.e("View is null")
            return
        }

        listener?.let {
            val minClickInterval = MIN_CLICK_INTERVAL
            this.setOnClickListener { view ->
                val currentClickTime = SystemClock.elapsedRealtime()
                val elapsedTime = currentClickTime - DebounceClickValues.globalSingleLastClickTime

                if (elapsedTime <= minClickInterval) {
                    return@setOnClickListener
                }

                DebounceClickValues.globalSingleLastClickTime = currentClickTime

                listener.onClick(view)
            }
        }
    }

    private object DebounceClickValues {
        var globalSingleLastClickTime: Long = 0
        const val MIN_CLICK_INTERVAL: Long = 300
    }
}