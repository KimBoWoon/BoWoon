package com.practice.ui.fragments.vm

import androidx.lifecycle.MutableLiveData
import com.practice.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingVM @Inject constructor() : BaseVM(

) {
    val removeAllWish = MutableLiveData<Unit>()

    fun removeAllWish() {
        removeAllWish.value = Unit
    }
}