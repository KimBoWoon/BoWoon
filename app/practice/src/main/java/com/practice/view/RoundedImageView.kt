package com.practice.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import com.bowoon.practice.R

@SuppressLint("AppCompatCustomView")
class RoundedImageView @JvmOverloads constructor(
    private val context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
    private val defStyleRes: Int = 0
) : ImageView(context, attrs, defStyleAttr, defStyleRes) {
    private var radius = 0f
    private var topLeft = 0f
    private var topRight = 0f
    private var bottomLeft = 0f
    private var bottomRight = 0f
    private val roundRect = RectF()
    private val path = Path()
    private val radii = floatArrayOf(
        0f, 0f,
        0f, 0f,
        0f, 0f,
        0f, 0f,
    )

    enum class Corner {
        ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT
    }

    companion object {
        private const val DEFAULT_VALUE = -1
    }

    init {
        initView()
    }

    private fun initView() {
        val customStyleAttrs = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyleAttr, defStyleRes)
        radius = customStyleAttrs.getDimensionPixelSize(R.styleable.RoundedImageView_radius, DEFAULT_VALUE).toFloat()
        topLeft = customStyleAttrs.getDimensionPixelSize(R.styleable.RoundedImageView_top_left, DEFAULT_VALUE).toFloat()
        topRight = customStyleAttrs.getDimensionPixelSize(R.styleable.RoundedImageView_top_right, DEFAULT_VALUE).toFloat()
        bottomLeft = customStyleAttrs.getDimensionPixelSize(R.styleable.RoundedImageView_bottom_left, DEFAULT_VALUE).toFloat()
        bottomRight = customStyleAttrs.getDimensionPixelSize(R.styleable.RoundedImageView_bottom_right, DEFAULT_VALUE).toFloat()
        customStyleAttrs.recycle()

        if (radius != -1f) {
            setRadius(radius, Corner.ALL)
        } else {
            if (topLeft != -1f) {
                setRadius(topLeft, Corner.TOP_LEFT)
            }
            if (topRight != -1f) {
                setRadius(topRight, Corner.TOP_RIGHT)
            }
            if (bottomLeft != -1f) {
                setRadius(bottomLeft, Corner.BOTTOM_LEFT)
            }
            if (bottomRight != -1f) {
                setRadius(bottomRight, Corner.BOTTOM_RIGHT)
            }
        }
    }

    fun setRadius(radius: Float, vararg corners: Corner) {
        corners.forEach { corner ->
            when (corner) {
                Corner.ALL -> {
                    radii.fill(radius)
                }
                Corner.TOP_LEFT -> {
                    radii[0] = radius
                    radii[1] = radius
                }
                Corner.TOP_RIGHT -> {
                    radii[2] = radius
                    radii[3] = radius
                }
                Corner.BOTTOM_RIGHT -> {
                    radii[4] = radius
                    radii[5] = radius
                }
                Corner.BOTTOM_LEFT -> {
                    radii[6] = radius
                    radii[7] = radius
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        roundRect.set(0f, 0f, width.toFloat(), height.toFloat())
        path.addRoundRect(roundRect, radii, Path.Direction.CW)
    }

    override fun draw(canvas: Canvas) {
        canvas.clipPath(path)
        super.draw(canvas)
    }
}