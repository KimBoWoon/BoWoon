package com.practice.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.practice.R
import kotlin.math.min
import kotlin.math.pow

class CircleImageView @JvmOverloads constructor(
    private val context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
    private val defStyleRes: Int = 0
) : ImageView(context, attrs, defStyleAttr, defStyleRes) {
    private val mDrawableRect = RectF()
    private val mBorderRect = RectF()
    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val mBorderPaint = Paint()
    private val mCircleBackgroundPaint = Paint()
    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH
    private var mCircleBackgroundColor = DEFAULT_CIRCLE_BACKGROUND_COLOR
    private var mImageAlpha = DEFAULT_IMAGE_ALPHA
    private var mBitmap: Bitmap? = null
    private var mBitmapCanvas: Canvas? = null
    private var mDrawableRadius = 0f
    private var mBorderRadius = 0f
    private var mColorFilter: ColorFilter? = null
    private var mInitialized = false
    private var mRebuildShader = false
    private var mDrawableDirty = false
    private var mBorderOverlay = false
    private var mDisableCircularTransformation = false
    var borderColor: Int
        get() = mBorderColor
        set(borderColor) {
            if (borderColor == mBorderColor) {
                return
            }
            mBorderColor = borderColor
            mBorderPaint.color = borderColor
            invalidate()
        }
    var circleBackgroundColor: Int
        get() = mCircleBackgroundColor
        set(circleBackgroundColor) {
            if (circleBackgroundColor == mCircleBackgroundColor) {
                return
            }
            mCircleBackgroundColor = circleBackgroundColor
            mCircleBackgroundPaint.color = circleBackgroundColor
            invalidate()
        }
    var borderWidth: Int
        get() = mBorderWidth
        set(borderWidth) {
            if (borderWidth == mBorderWidth) {
                return
            }
            mBorderWidth = borderWidth
            mBorderPaint.strokeWidth = borderWidth.toFloat()
            updateDimensions()
            invalidate()
        }
    var isBorderOverlay: Boolean
        get() = mBorderOverlay
        set(borderOverlay) {
            if (borderOverlay == mBorderOverlay) {
                return
            }
            mBorderOverlay = borderOverlay
            updateDimensions()
            invalidate()
        }
    var isDisableCircularTransformation: Boolean
        get() = mDisableCircularTransformation
        set(disableCircularTransformation) {
            if (disableCircularTransformation == mDisableCircularTransformation) {
                return
            }
            mDisableCircularTransformation = disableCircularTransformation
            if (disableCircularTransformation) {
                mBitmap = null
                mBitmapCanvas = null
                mBitmapPaint.shader = null
            } else {
                initializeBitmap()
            }
            invalidate()
        }

    init {
        init()
    }

    private fun init() {
        val customStyleAttrs = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
        mBorderWidth = customStyleAttrs.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH)
        mBorderColor = customStyleAttrs.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR)
        mBorderOverlay = customStyleAttrs.getBoolean(R.styleable.CircleImageView_border_overlay, DEFAULT_BORDER_OVERLAY)
        mCircleBackgroundColor = customStyleAttrs.getColor(R.styleable.CircleImageView_circle_background_color, DEFAULT_CIRCLE_BACKGROUND_COLOR)
        customStyleAttrs.recycle()

        mInitialized = true
        super.setScaleType(SCALE_TYPE)
        mBitmapPaint.apply {
            isAntiAlias = true
            isDither = true
            isFilterBitmap = true
            alpha = mImageAlpha
            colorFilter = mColorFilter
        }
        mBorderPaint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = mBorderColor
            strokeWidth = mBorderWidth.toFloat()
        }
        mCircleBackgroundPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = mCircleBackgroundColor
        }
        outlineProvider = OutlineProvider()
    }

    override fun setScaleType(scaleType: ScaleType) {
        require(scaleType == SCALE_TYPE) { String.format("ScaleType %s not supported.", scaleType) }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        require(!adjustViewBounds) { "adjustViewBounds not supported." }
    }

    override fun onDraw(canvas: Canvas) {
        if (mDisableCircularTransformation) {
            super.onDraw(canvas)
            return
        }
        if (mCircleBackgroundColor != Color.TRANSPARENT) {
            canvas.drawCircle(
                mDrawableRect.centerX(),
                mDrawableRect.centerY(),
                mDrawableRadius,
                mCircleBackgroundPaint
            )
        }
        if (mBitmap != null) {
            mBitmapCanvas?.let {
                if (mDrawableDirty) {
                    mDrawableDirty = false
                    drawable.apply {
                        setBounds(0, 0, it.width, it.height)
                        draw(it)
                    }
                }
            }
            if (mRebuildShader) {
                mRebuildShader = false
                val bitmapShader =
                    BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                bitmapShader.setLocalMatrix(mShaderMatrix)
                mBitmapPaint.shader = bitmapShader
            }
            canvas.drawCircle(
                mDrawableRect.centerX(),
                mDrawableRect.centerY(),
                mDrawableRadius,
                mBitmapPaint
            )
        }
        if (mBorderWidth > 0) {
            canvas.drawCircle(
                mBorderRect.centerX(),
                mBorderRect.centerY(),
                mBorderRadius,
                mBorderPaint
            )
        }
    }

    override fun invalidateDrawable(dr: Drawable) {
        mDrawableDirty = true
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateDimensions()
        invalidate()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateDimensions()
        invalidate()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        updateDimensions()
        invalidate()
    }

    @Deprecated("Use {@link #setCircleBackgroundColor(int)} instead")
    fun setCircleBackgroundColorResource(@ColorRes circleBackgroundRes: Int) {
        circleBackgroundColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(circleBackgroundRes, null)
        } else {
            context.resources.getColor(circleBackgroundRes)
        }
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap()
        invalidate()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
        invalidate()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
        invalidate()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
        invalidate()
    }

    override fun setImageAlpha(alpha: Int) {
        var alpha = alpha
        alpha = alpha and 0xFF
        if (alpha == mImageAlpha) {
            return
        }
        mImageAlpha = alpha

        // This might be called during ImageView construction before
        // member initialization has finished on API level >= 16.
        if (mInitialized) {
            mBitmapPaint.alpha = alpha
            invalidate()
        }
    }

    override fun getImageAlpha(): Int = mImageAlpha

    override fun setColorFilter(cf: ColorFilter) {
        if (cf === mColorFilter) {
            return
        }
        mColorFilter = cf

        // This might be called during ImageView construction before
        // member initialization has finished on API level <= 19.
        if (mInitialized) {
            mBitmapPaint.colorFilter = cf
            invalidate()
        }
    }

    override fun getColorFilter(): ColorFilter? = mColorFilter

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        drawable ?: return null

        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            try {
                val bitmap = if (drawable is ColorDrawable) {
                    Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG)
                } else {
                    Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
                }
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun initializeBitmap() {
        mBitmap = getBitmapFromDrawable(drawable)
        mBitmapCanvas = mBitmap?.let {
            if (it.isMutable) {
                Canvas(it)
            } else {
                null
            }
        }
        if (!mInitialized) {
            return
        }
        if (mBitmap != null) {
            updateShaderMatrix()
        } else {
            mBitmapPaint.shader = null
        }
    }

    private fun updateDimensions() {
        mBorderRect.set(calculateBounds())
        mBorderRadius = min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f)
        mDrawableRect.set(mBorderRect)
        if (!mBorderOverlay && mBorderWidth > 0) {
            mDrawableRect.inset(mBorderWidth - 1.0f, mBorderWidth - 1.0f)
        }
        mDrawableRadius = min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f)
        updateShaderMatrix()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom
        val sideLength = Math.min(availableWidth, availableHeight)
        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f
        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {
        mBitmap ?: return

        val scale: Float
        var dx = 0f
        var dy = 0f
        mShaderMatrix.set(null)
        val bitmapHeight = mBitmap!!.height
        val bitmapWidth = mBitmap!!.width
        if (bitmapWidth * mDrawableRect.height() > mDrawableRect.width() * bitmapHeight) {
            scale = mDrawableRect.height() / bitmapHeight.toFloat()
            dx = (mDrawableRect.width() - bitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / bitmapWidth.toFloat()
            dy = (mDrawableRect.height() - bitmapHeight * scale) * 0.5f
        }
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(
            (dx + 0.5f).toInt() + mDrawableRect.left,
            (dy + 0.5f).toInt() + mDrawableRect.top
        )
        mRebuildShader = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean =
        if (mDisableCircularTransformation) {
            super.onTouchEvent(event)
        } else {
            inTouchableArea(event.x, event.y) && super.onTouchEvent(event)
        }

    private fun inTouchableArea(x: Float, y: Float): Boolean =
        if (mBorderRect.isEmpty) {
            true
        } else {
            (x - mBorderRect.centerX()).pow(2.0f) + (y - mBorderRect.centerY()).pow(2.0f) <= mBorderRadius.pow(2.0f)
        }

    private inner class OutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            if (mDisableCircularTransformation) {
                BACKGROUND.getOutline(view, outline)
            } else {
                val bounds = Rect()
                mBorderRect.roundOut(bounds)
                outline.setRoundRect(bounds, bounds.width() / 2.0f)
            }
        }
    }

    companion object {
        private val SCALE_TYPE = ScaleType.CENTER_CROP
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMENSION = 2
        private const val DEFAULT_BORDER_WIDTH = 0
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.TRANSPARENT
        private const val DEFAULT_IMAGE_ALPHA = 255
        private const val DEFAULT_BORDER_OVERLAY = false
    }
}

//import android.annotation.TargetApi
//import android.content.Context
//import android.graphics.*
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.os.Build
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewOutlineProvider
//import android.widget.ImageView
//import androidx.annotation.ColorInt
//import androidx.annotation.Dimension
//import androidx.annotation.DrawableRes
//import kotlin.math.min
//import kotlin.math.pow
//import kotlin.math.sqrt
//
//open class CircleImageView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null
//) : ImageView(context, attrs) {
//
//    private var mBitmapShader: Shader? = null
//    private val mShaderMatrix: Matrix
//    private val mBitmapDrawBounds: RectF
//    private val mStrokeBounds: RectF
//    private var mBitmap: Bitmap? = null
//    private val mBitmapPaint: Paint
//    private val mStrokePaint: Paint
//    private val mPressedPaint: Paint
//    private val mInitialized: Boolean
//    private var mPressed = false
//    private var mHighlightEnable: Boolean
//
//    var isHighlightEnable: Boolean
//        get() = mHighlightEnable
//        set(enable) {
//            mHighlightEnable = enable
//            invalidate()
//        }
//
//    @get:ColorInt
//    var highlightColor: Int
//        get() = mPressedPaint.color
//        set(color) {
//            mPressedPaint.color = color
//            invalidate()
//        }
//
//    @get:ColorInt
//    var strokeColor: Int
//        get() = mStrokePaint.color
//        set(color) {
//            mStrokePaint.color = color
//            invalidate()
//        }
//
//    @get:Dimension
//    var strokeWidth: Float
//        get() = mStrokePaint.strokeWidth
//        set(width) {
//            mStrokePaint.strokeWidth = width
//            invalidate()
//        }
//
//    override fun setImageResource(@DrawableRes resId: Int) {
//        super.setImageResource(resId)
//        setupBitmap()
//    }
//
//    override fun setImageDrawable(drawable: Drawable?) {
//        super.setImageDrawable(drawable)
//        setupBitmap()
//    }
//
//    override fun setImageBitmap(bm: Bitmap?) {
//        super.setImageBitmap(bm)
//        setupBitmap()
//    }
//
//    override fun setImageURI(uri: Uri?) {
//        super.setImageURI(uri)
//        setupBitmap()
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        val halfStrokeWidth = mStrokePaint.strokeWidth / 2f
//        updateCircleDrawBounds(mBitmapDrawBounds)
//        mStrokeBounds.set(mBitmapDrawBounds)
//        mStrokeBounds.inset(halfStrokeWidth, halfStrokeWidth)
//        updateBitmapSize()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            outlineProvider = CircleImageViewOutlineProvider(mStrokeBounds)
//        }
//    }
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        var processed = false
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                if (!isInCircle(event.x, event.y)) {
//                    return false
//                }
//                processed = true
//                mPressed = true
//                invalidate()
//            }
//
//            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
//                processed = true
//                mPressed = false
//                invalidate()
//                if (!isInCircle(event.x, event.y)) {
//                    return false
//                }
//            }
//        }
//        return super.onTouchEvent(event) || processed
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        drawBitmap(canvas)
//        drawStroke(canvas)
//        drawHighlight(canvas)
//    }
//
//    protected fun drawHighlight(canvas: Canvas) {
//        if (mHighlightEnable && mPressed) {
//            canvas.drawOval(mBitmapDrawBounds, mPressedPaint)
//        }
//    }
//
//    protected fun drawStroke(canvas: Canvas) {
//        if (mStrokePaint.strokeWidth > 0f) {
//            canvas.drawOval(mStrokeBounds, mStrokePaint)
//        }
//    }
//
//    private fun drawBitmap(canvas: Canvas) {
//        canvas.drawOval(mBitmapDrawBounds, mBitmapPaint)
//    }
//
//    protected fun updateCircleDrawBounds(bounds: RectF) {
//        val contentWidth = width - paddingLeft - paddingRight.toFloat()
//        val contentHeight =
//            height - paddingTop - paddingBottom.toFloat()
//        var left = paddingLeft.toFloat()
//        var top = paddingTop.toFloat()
//        if (contentWidth > contentHeight) {
//            left += (contentWidth - contentHeight) / 2f
//        } else {
//            top += (contentHeight - contentWidth) / 2f
//        }
//        val diameter = min(contentWidth, contentHeight)
//        bounds[left, top, left + diameter] = top + diameter
//    }
//
//    private fun setupBitmap() {
//        if (!mInitialized) {
//            return
//        }
//        mBitmap = getBitmapFromDrawable(drawable)
//        if (mBitmap == null) {
//            return
//        }
//        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//        mBitmapPaint.shader = mBitmapShader
//        updateBitmapSize()
//    }
//
//    private fun updateBitmapSize() {
//        if (mBitmap == null) return
//        val dx: Float
//        val dy: Float
//        val scale: Float
//
//        // scale up/down with respect to this view size and maintain aspect ratio
//        // translate bitmap position with dx/dy to the center of the image
//        if (mBitmap!!.width < mBitmap!!.height) {
//            scale = mBitmapDrawBounds.width() / mBitmap!!.width.toFloat()
//            dx = mBitmapDrawBounds.left
//            dy =
//                mBitmapDrawBounds.top - mBitmap!!.height * scale / 2f + mBitmapDrawBounds.width() / 2f
//        } else {
//            scale = mBitmapDrawBounds.height() / mBitmap!!.height.toFloat()
//            dx =
//                mBitmapDrawBounds.left - mBitmap!!.width * scale / 2f + mBitmapDrawBounds.width() / 2f
//            dy = mBitmapDrawBounds.top
//        }
//        mShaderMatrix.setScale(scale, scale)
//        mShaderMatrix.postTranslate(dx, dy)
//        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
//    }
//
//    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
//        if (drawable == null) {
//            return null
//        }
//        if (drawable is BitmapDrawable) {
//            return drawable.bitmap
//        }
//        val bitmap = Bitmap.createBitmap(
//            drawable.intrinsicWidth,
//            drawable.intrinsicHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
//        return bitmap
//    }
//
//    private fun isInCircle(x: Float, y: Float): Boolean {
//        // find the distance between center of the view and x,y point
//        val distance = sqrt(
//            (mBitmapDrawBounds.centerX() - x.toDouble()).pow(2.0) + (mBitmapDrawBounds.centerY() - y.toDouble()).pow(
//                2.0
//            )
//        )
//        return distance <= mBitmapDrawBounds.width() / 2
//    }
//
//    class CircleImageViewOutlineProvider internal constructor(rect: RectF) : ViewOutlineProvider() {
//        private val mRect = Rect(
//            rect.left.toInt(),
//            rect.top.toInt(),
//            rect.right.toInt(),
//            rect.bottom.toInt()
//        )
//
//        override fun getOutline(view: View, outline: Outline) {
//            outline.setOval(mRect)
//        }
//    }
//
//    companion object {
//        private const val DEF_PRESS_HIGHLIGHT_COLOR = 0x32000000
//    }
//
//    init {
//        var strokeColor = Color.TRANSPARENT
//        var strokeWidth = 0f
//        var highlightEnable = true
//        var highlightColor =
//            DEF_PRESS_HIGHLIGHT_COLOR
//        attrs?.let {
////            val a =
////                context.obtainStyledAttributes(it, R.styleable.CircleImageView, 0, 0)
////            strokeColor = a.getColor(
////                R.styleable.CircleImageView_strokeColor,
////                Color.TRANSPARENT
////            )
////            strokeWidth =
////                a.getDimensionPixelSize(R.styleable.CircleImageView_strokeWidth, 0).toFloat()
////            highlightEnable = a.getBoolean(R.styleable.CircleImageView_highlightEnable, true)
////            highlightColor = a.getColor(
////                R.styleable.CircleImageView_highlightColor,
////                DEF_PRESS_HIGHLIGHT_COLOR
////            )
////            a.recycle()
//        }
//        mShaderMatrix = Matrix()
//        mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//        mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
//        mStrokeBounds = RectF()
//        mBitmapDrawBounds = RectF()
//        mStrokePaint.color = strokeColor
//        mStrokePaint.style = Paint.Style.STROKE
//        mStrokePaint.strokeWidth = strokeWidth
//        mPressedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//        mPressedPaint.color = highlightColor
//        mPressedPaint.style = Paint.Style.FILL
//        mHighlightEnable = highlightEnable
//        mInitialized = true
//        setupBitmap()
//    }
//}