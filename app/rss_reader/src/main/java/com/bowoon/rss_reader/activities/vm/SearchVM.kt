package com.bowoon.rss_reader.activities.vm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bowoon.commonutils.DataStatus
import com.bowoon.rss_reader.data.Article
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
class SearchVM @Inject constructor(
    private val rssProducer: RssProducer
) : ViewModel(), LifecycleObserver {
    val rss = MutableStateFlow<DataStatus<List<Article>>>(DataStatus.Loading)

    override fun onCleared() {
        rssProducer.channelClose()
        super.onCleared()
    }

    fun fetchRss(query: String) {
        flow {
            emit(
                mutableListOf<Rss>().apply {
                    repeat(rssProducer.getFeedSize()) {
                        rssProducer.rssChannel.receive().also { rss ->
                            rss?.let { add(it) }
                        }
                    }
                }
            )
        }.onStart {
            rss.value = DataStatus.Loading
            rssProducer.fetch()
        }.catch {
            rss.value = DataStatus.Failure(it)
        }.onEach { rssList ->
            this@SearchVM.rss.value = DataStatus.Success(
                mutableListOf<Article>().apply {
                    rssList.forEachIndexed { index, it ->
                        val filterItem = it.channel?.items?.filter { item ->
                            item.title?.contains(query, true) == true || item.description?.contains(query, true) == true
                        } ?: emptyList()

                        if (!filterItem.none()) {
                            add(Article(true, rssProducer.getFeeds()[index].name))
                        }

                        addAll(filterItem.map { item ->
                            Article(
                                false,
                                rssProducer.getFeeds()[index].name,
                                item.title,
                                item.description,
                                item.link
                            )
                        })
                    }
                }
            )
        }.launchIn(viewModelScope)
    }
}