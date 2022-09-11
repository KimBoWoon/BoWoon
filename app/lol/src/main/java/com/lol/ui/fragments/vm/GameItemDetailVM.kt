package com.lol.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import com.domain.lol.dto.GameItemInfo
import com.lol.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import util.DataStatus
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