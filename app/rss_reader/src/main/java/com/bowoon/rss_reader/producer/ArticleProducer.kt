package com.bowoon.rss_reader.producer

import com.bowoon.rss_reader.data.Feed
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

object ArticleProducer {
    private const val CHANNEL = "channel"
    val feeds = listOf(
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
    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error captured in $coroutineContext")
        println("Message: ${throwable.message}")
    }
    private val notifications = Channel<Action>(Channel.CONFLATED)
    val produce = CoroutineScope(Dispatchers.IO + handler).produce {
        feeds.forEach { send(it) }
    }

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
}