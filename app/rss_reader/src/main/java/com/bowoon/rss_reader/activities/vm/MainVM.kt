package com.bowoon.rss_reader.activities.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.Log
import com.bowoon.rss_reader.apis.Apis
import com.bowoon.rss_reader.base.BaseVM
import com.bowoon.rss_reader.data.Article
import com.bowoon.rss_reader.data.Rss
import com.bowoon.rss_reader.producer.ArticleProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val api: Apis
) : BaseVM() {
    val rss = MutableStateFlow<DataStatus<List<Article>>>(DataStatus.Loading)
    val rssList = MutableStateFlow<DataStatus<List<Rss>>>(DataStatus.Loading)
    val loadingCount = MutableStateFlow<Int>(1)

    init {
        fetchRss()
    }

    fun fetchRss() {
        callbackFlow {
            runCatching {
                ArticleProducer.feeds.foldIndexed(mutableListOf<Rss>()) { index, acc, feed ->
                    Log.d("#fetchRss", "$index 데이터 로딩중...")
                    loadingCount.value = index + 1
                    acc.add(async { api.rssApi.getRss(feed.url ?: "") }.await())
                    acc
                }
            }.onSuccess { rssList ->
                trySend(rssList)
            }.onFailure { e ->
                close(e)
            }

            awaitClose {
                close()
            }
        }.onStart {
            rssList.value = DataStatus.Loading
        }.catch {
            rssList.value = DataStatus.Failure(it)
        }.onEach { rssList ->
            this@MainVM.rssList.value = DataStatus.Success(rssList)
        }.launchIn(viewModelScope)
    }

//    init {
//        viewModelScope.launch {
////            fetchRss()
//            if (!ArticleProducer.produce.isClosedForReceive) {
//                val feed = ArticleProducer.produce.receive()
//
//                fetchRss(feed.url ?: "")
//            }
//        }
//    }

//    fun fetchRss() {
//        callbackFlow {
//            runCatching {
//                ArticleProducer.feeds.map {
//                    async { api.rssApi.getRss(it.url ?: "") }
//                }.awaitAll()
//            }.onSuccess {
//                trySend(it)
//            }.onFailure { e ->
//                close(e)
//            }
//
//            awaitClose {
//                close()
//            }
//        }.onStart {
//            rss.value = DataStatus.Loading
//        }.catch {
//            rss.value = DataStatus.Failure(it)
//        }.onEach { rss ->
//            this@MainVM.rss.value = DataStatus.Success(
//                mutableListOf<Article>().apply {
//                    rss.forEachIndexed { index, it ->
//                        add(Article(true, ArticleProducer.feeds[index].name))
//                        addAll(it.channel?.items?.map { item ->
//                            Article(
//                                false,
//                                ArticleProducer.feeds[index].name,
//                                item.title,
//                                item.description,
//                                item.link
//                            )
//                        } ?: emptyList())
//                    }
//                }
//            )
//        }.launchIn(viewModelScope)
//    }
//
//    fun fetchRss(url: String) {
//        callbackFlow {
//            runCatching {
//                async { api.rssApi.getRss(url) }.await()
//            }.onSuccess {
//                trySend(it)
//            }.onFailure { e ->
//                close(e)
//            }
//
//            awaitClose {
//                close()
//            }
//        }.onStart {
//            rss.value = DataStatus.Loading
//        }.catch {
//            rss.value = DataStatus.Failure(it)
//        }.onEach { rss ->
//            this@MainVM.rss.value = DataStatus.Success(
//                mutableListOf<Article>().apply {
//                    add(Article(true, ArticleProducer.feeds.find { it.url == url }?.name))
//                    addAll(rss.channel?.items?.map { item ->
//                        Article(
//                            false,
//                            ArticleProducer.feeds.find { it.url == url }?.name,
//                            item.title,
//                            item.description,
//                            item.link
//                        )
//                    } ?: emptyList())
//                }
//            )
//        }.launchIn(viewModelScope)
//    }
}