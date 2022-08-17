package util

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt

object ScreenUtils {
    val Int.dp: Int get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()
}

object ContextUtils {
    fun Context?.showToast(message: String, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }

    fun Context?.showSnackBar(view: View, message: String, duration: Int) {
        this?.let { context ->
            Snackbar.make(context, view, message, duration).show()
        }
    }
}

object ViewUtils {
    fun View?.showSnackBar(message: String, duration: Int) {
        this?.let { view ->
            Snackbar.make(view, message, duration).show()
        }
    }

    fun View?.showSnackBar(@StringRes resId: Int, duration: Int) {
        this?.let { view ->
            Snackbar.make(view, resId, duration).show()
        }
    }
}

fun <A, B, R> ifNotNull(obj1: A?, obj2: B?, action: (A, B) -> R) {
    if (obj1 != null && obj2 != null) {
        action.invoke(obj1, obj2)
    }
}