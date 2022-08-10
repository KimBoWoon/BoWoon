package util

import android.content.res.Resources
import kotlin.math.roundToInt

object ScreenUtils {
    val Int.dp: Int get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()
}

fun <A, B, R> ifNotNull(obj1: A?, obj2: B?, action: (A, B) -> R) {
//    params.forEach {
//        if (it == null) {
//            return
//        }
//    }
    if (obj1 != null && obj2 != null) {
        action.invoke(obj1, obj2)
    }
}