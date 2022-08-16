package com.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import com.domain.practice.usecase.RoomUseCase
import com.practice.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingVM @Inject constructor(
    private val roomUseCase: RoomUseCase
) : BaseVM() {
    suspend fun removeAllWish(): String =
        viewModelScope.async {
            if (roomUseCase.deleteAll() > 0) {
                "위시 리스트를 모두 삭제했습니다."
            } else {
                "위시 리스트에 저장된 포켓몬이 없습니다."
            }
        }.await()
}