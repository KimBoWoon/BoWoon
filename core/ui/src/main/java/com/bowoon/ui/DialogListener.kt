package com.bowoon.ui

import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import kotlinx.parcelize.Parcelize

@Parcelize
class YesOrNoDialogListener : Parcelable {
    var leftBtnText: String? = null
    var leftBtnLambda: (() -> Unit)? = null
    var rightBtnText: String? = null
    var rightBtnLambda: (() -> Unit)? = null
}

@Parcelize
class DialogListener<V> : Parcelable {
    var viewSettings: (DialogFragment.(V) -> Unit)? = null
}