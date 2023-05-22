package com.rss_reader.activities.vm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.rssReader.usecase.RssDataUseCase
import com.rss_reader.dto.Article
import com.rss_reader.producer.ArticleProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import util.DataStatus
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val rssDataUseCase: RssDataUseCase
) : ViewModel(), LifecycleObserver {
    val rss = MutableStateFlow<DataStatus<List<Article>>>(DataStatus.Loading)

    fun fetchRss(query: String) {
        viewModelScope.launch {
            callbackFlow {
                runCatching {
                    ArticleProducer.feeds.map {
                        async { rssDataUseCase.getRssData(it.url ?: "") }
                    }.awaitAll()
                }.onSuccess {
                    trySend(it)
                }.onFailure { e ->
                    close(e)
                }

                awaitClose {
                    close()
                }
            }.onStart {
                rss.value = DataStatus.Loading
            }.catch {
                rss.value = DataStatus.Failure(it)
            }.map { rss ->
                mutableListOf<Article>().apply {
                    rss.forEachIndexed { index, it ->
                        val filterItem = it.channel?.items?.filter { item ->
                            item.title?.contains(query, true) == true || item.description?.contains(query, true) == true
                        } ?: emptyList()

                        if (!filterItem.none()) {
                            add(Article(true, ArticleProducer.feeds[index].name))
                        }

                        addAll(filterItem.map { item ->
                            Article(
                                false,
                                ArticleProducer.feeds[index].name,
                                item.title,
                                item.description,
                                item.link
                            )
                        })
                    }
                }
            }.collect {
                rss.value = DataStatus.Success(it)
            }
        }
    }
}