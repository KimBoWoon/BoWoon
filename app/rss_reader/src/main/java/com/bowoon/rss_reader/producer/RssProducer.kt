package com.bowoon.rss_reader.producer

import com.bowoon.commonutils.Log
import com.bowoon.rss_reader.apis.Apis
import com.bowoon.rss_reader.data.Feed
import com.bowoon.rss_reader.data.Rss
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import javax.inject.Inject

class RssProducer @Inject constructor(
    private val apis: Apis
) {
    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("#RssProducer", "id > [${Thread.currentThread().id}], message > [${throwable.message}]")
        Log.printStackTrace(throwable)
    }
    private val feeds = listOf(
        Feed("JTBC", "https://fs.jtbc.co.kr/RSS/newsflash.xml"),
        Feed("SBS", "https://news.sbs.co.kr/news/ReplayRssFeed.do?prog_cd=R1&plink=RSSREADER"),
        Feed("비디오머그", "https://news.sbs.co.kr/news/VideoMug_RssFeed.do?plink=RSSREADER"),
        Feed("연합", "http://www.yonhapnewstv.co.kr/browse/feed/"),
        Feed("NPR", "https://feeds.npr.org/1001/rss.xml"),
        Feed("CNN", "http://rss.cnn.com/rss/cnn_topstories.rss"),
        Feed("NASA", "https://www.nasa.gov/rss/dyn/breaking_news.rss"),
        Feed("FOX", "https://moxie.foxnews.com/google-publisher/politics.xml?format=xml"),
//        Feed("inv", "afasldkfj")
    )
    private val notifications = Channel<Action>(Channel.CONFLATED)
    val rssChannel = Channel<Rss>()

    enum class Action {
        INCREASE, RESET
    }

    suspend fun increment() {
        notifications.send(Action.INCREASE)
    }

    suspend fun reset() {
        notifications.send(Action.RESET)
    }

    fun getNotificationChannel(): ReceiveChannel<Action> = notifications

    fun fetch() {
        feeds.forEach { feed ->
            CoroutineScope(Dispatchers.IO + handler).launch {
                Log.d("#RssProducer", "name > [${feed.name}], url > [${feed.url}]")
                rssChannel.send(apis.rssApi.getRss(feed.url ?: ""))
            }
        }
    }

    fun getFeeds(): List<Feed> = feeds

    fun getFeedSize(): Int = feeds.size

    fun channelClose() {
        rssChannel.close()
    }
}