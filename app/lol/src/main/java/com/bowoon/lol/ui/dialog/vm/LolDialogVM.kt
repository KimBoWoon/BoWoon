package com.bowoon.lol.ui.dialog.vm

import com.bowoon.lol.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LolDialogVM @Inject constructor(

) : BaseVM() {
    val message = MutableStateFlow<String>("")
    val confirmText = MutableStateFlow<String>("")
    val dismissText = MutableStateFlow<String?>("")
    val choose = MutableStateFlow<Boolean?>(null)

    fun init(message: String, confirmText: String, dismissText: String? = null) {
        this.message.value = message
        this.confirmText.value = confirmText
        this.dismissText.value = dismissText
    }

    fun onConfirmClick() {
        choose.value = true
    }

    fun dismissClick() {
        choose.value = false
    }
}