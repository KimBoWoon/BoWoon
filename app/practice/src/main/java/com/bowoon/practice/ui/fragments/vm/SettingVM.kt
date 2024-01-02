package com.bowoon.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import com.bowoon.practice.base.BaseVM
import com.bowoon.practice.room.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class SettingVM @Inject constructor(
    private val roomRepository: RoomRepository
) : BaseVM() {
    suspend fun removeAllWish(): String =
        viewModelScope.async {
            if (roomRepository.deleteAll() > 0) {
                "위시 리스트를 모두 삭제했습니다."
            } else {
                "위시 리스트에 저장된 포켓몬이 없습니다."
            }
        }.await()
}