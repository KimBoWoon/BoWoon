package util

import android.os.SystemClock
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

        ImageLoader.load(this.context, url, imageView = this)
    }

    @JvmStatic
    @BindingAdapter("htmlText")
    fun TextView?.htmlText(text: String?) {
        this ?: run {
            Log.e("textview is null!")
            return
        }
        text ?: run {
            Log.e("htmlText text is null!")
            return
        }

        this.text?.let {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                this.text = Html.fromHtml(text)
            } else {
                this.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
            }
        } ?: run {
            Log.e("textview.text is null!")
            return
        }
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