package com.bowoon.lol.ui.fragments.vm

import com.bowoon.lol.apis.Apis
import com.bowoon.lol.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameItemVM @Inject constructor(
    private val apis: Apis
) : BaseVM() {
}