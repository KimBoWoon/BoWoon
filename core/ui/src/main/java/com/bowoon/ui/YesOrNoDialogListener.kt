package com.bowoon.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class YesOrNoDialogListener : Parcelable {
    var leftBtnText: String? = null
    var leftBtnLambda: (() -> Unit)? = null
    var rightBtnText: String? = null
    var rightBtnLambda: (() -> Unit)? = null
}