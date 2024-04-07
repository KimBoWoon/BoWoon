package com.bowoon.component.vh

import android.content.Intent
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.commonutils.ViewAdapter.onDebounceClickListener
import com.bowoon.component.base.BaseComponentVH
import com.bowoon.component.data.ComponentEvent
import com.bowoon.component.data.Components
import com.bowoon.component.databinding.VhImageComponentBinding
import com.bowoon.component.ui.WebView


class ImageComponentVH(
    private val binding: VhImageComponentBinding,
    private val tabEvent: ((Int) -> Unit)? = null
) : BaseComponentVH<Components.ImageComponent>(binding) {
    companion object {
        private const val TAG = "component_image_component_vh"
    }

    override fun bind(component: Components.ImageComponent?) {
        component?.let {
            binding.apply {
                this.component = it

                ivImageComponent.apply {
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
                                clickEvent.position?.let {
                                    tabEvent?.invoke(it)
                                }
                            }
                        }
                    }
                }

//                ImageLoader.load(
//                    binding.root.context,
//                    ivImageComponent,
//                    it.url ?: "",
//                    ImageOptions(
//                        placeholderDrawable = ColorDrawable(Color.TRANSPARENT),
//                        radius = it.radius,
//                        topLeftRadius = it.topLeftRadius ?: 0f,
//                        topRightRadius = it.topRightRadius ?: 0f,
//                        bottomLeftRadius = it.bottomLeftRadius ?: 0f,
//                        bottomRightRadius = it.bottomRightRadius ?: 0f,
//                    ),
//                    object : ImageLoadListener {
//                        override fun onStart() {
//                            Log.d(TAG, "load start!")
//                        }
//
//                        override fun onFailed(e: GlideException?, model: Any?) {
//                            Log.d(TAG, "load failed!")
//                        }
//
//                        override fun onSuccess(
//                            resource: Drawable?,
//                            model: Any?,
//                            dataSource: DataSource?
//                        ) {
//                            Log.d(TAG, "load success!")
//                        }
//                    }
//                )
            }
        }
    }
}