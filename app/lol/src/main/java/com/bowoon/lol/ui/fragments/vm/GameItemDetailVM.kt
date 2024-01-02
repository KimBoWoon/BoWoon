package com.bowoon.lol.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import com.bowoon.lol.base.BaseVM
import com.bowoon.lol.data.GameItemInfo
import com.bowoon.commonutils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameItemDetailVM @Inject constructor(

) : BaseVM() {
    val relatedItem = MutableStateFlow<DataStatus<List<GameItemInfo>?>>(DataStatus.Loading)

    init {
        viewModelScope.launch {
            runCatching {

            }
        }
    }
}