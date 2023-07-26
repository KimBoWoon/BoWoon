package com.lol.ui.fragments.vm

import com.domain.lol.usecase.DataDragonApiUseCase
import com.lol.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameItemVM @Inject constructor(
    private val dataDragonApiUseCase: DataDragonApiUseCase
) : BaseVM() {
}