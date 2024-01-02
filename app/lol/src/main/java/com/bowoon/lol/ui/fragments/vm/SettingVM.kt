package com.bowoon.lol.ui.fragments.vm

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.bowoon.datamanager.DataStoreRepository
import com.bowoon.lol.base.BaseVM
import com.bowoon.lol.data.LolDataConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingVM @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : BaseVM() {
    suspend fun setVersion(version: String) {
        viewModelScope.launch {
            dataStoreRepository.setData(LolDataConstant.LOL_DATA_STORE_NAME, stringPreferencesKey("LOL_VERSION"), version)
        }
    }

    suspend fun getVersion(): String? =
        withContext(viewModelScope.coroutineContext) {
            dataStoreRepository.getData(LolDataConstant.LOL_DATA_STORE_NAME, stringPreferencesKey("LOL_VERSION"), null)
        }
}