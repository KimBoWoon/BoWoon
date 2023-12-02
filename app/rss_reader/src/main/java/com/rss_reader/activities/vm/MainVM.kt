package com.rss_reader.activities.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.domain.rssReader.usecase.RssDataUseCase
import com.rss_reader.base.BaseVM
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
import com.data.util.DataStatus
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val rssDataUseCase: RssDataUseCase
) : BaseVM() {
    val rss = MutableStateFlow<DataStatus<List<Article>>>(DataStatus.Loading)

    init {
        viewModelScope.launch {
//            fetchRss()
            if (!ArticleProducer.produce.isClosedForReceive) {
                val feed = ArticleProducer.produce.receive()

                fetchRss(feed.url ?: "")
            }
        }
    }

    fun fetchRss() {
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
                        add(Article(true, ArticleProducer.feeds[index].name))
                        addAll(it.channel?.items?.map { item ->
                            Article(
                                false,
                                ArticleProducer.feeds[index].name,
                                item.title,
                                item.description,
                                item.link
                            )
                        } ?: emptyList())
                    }
                }
            }.collect {
                rss.value = DataStatus.Success(it)
            }
        }
    }

    fun fetchRss(url: String) {
        viewModelScope.launch {
            callbackFlow {
                runCatching {
                    async { rssDataUseCase.getRssData(url) }.await()
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
                    add(Article(true, ArticleProducer.feeds.find { it.url == url }?.name))
                    addAll(rss.channel?.items?.map { item ->
                        Article(
                            false,
                            ArticleProducer.feeds.find { it.url == url }?.name,
                            item.title,
                            item.description,
                            item.link
                        )
                    } ?: emptyList())
                }
            }.collect {
                rss.value = DataStatus.Success(it)
            }
        }
    }
}