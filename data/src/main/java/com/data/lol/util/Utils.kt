package com.data.lol.util

import android.content.res.Resources
import kotlin.math.roundToInt

object ScreenUtils {
    val Int.dp: Int get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()
}