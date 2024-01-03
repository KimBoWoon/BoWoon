package com.bowoon.rss_reader.activities.vm

import androidx.lifecycle.viewModelScope
import com.bowoon.commonutils.DataStatus
import com.bowoon.rss_reader.base.BaseVM
import com.bowoon.rss_reader.data.Rss
import com.bowoon.rss_reader.producer.RssProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val rssProducer: RssProducer
) : BaseVM() {
    val rss = MutableStateFlow<DataStatus<List<Rss>>>(DataStatus.Loading)

    init {
        fetchRss()
    }

    override fun onCleared() {
        rssProducer.channelClose()
        super.onCleared()
    }

    fun fetchRss() {
        flow {
            emit(
                mutableListOf<Rss>().apply {
                    repeat(rssProducer.getFeedSize()) {
                        add(rssProducer.rssChannel.receive())
                    }
                }
            )
        }.onStart {
            rss.value = DataStatus.Loading
            rssProducer.fetch()
        }.catch {
            rss.value = DataStatus.Failure(it)
        }.onEach { rssList ->
            this@MainVM.rss.value = DataStatus.Success(rssList)
        }.launchIn(viewModelScope)
    }
}