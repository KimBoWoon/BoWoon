package com.rss_reader.producer

import com.rss_reader.dto.Feed
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
        Feed("npr", "https://feeds.npr.org/1001/rss.xml"),
        Feed("cnn", "http://rss.cnn.com/rss/cnn_topstories.rss"),
        Feed("nasa", "https://www.nasa.gov/rss/dyn/breaking_news.rss"),
        Feed("fox", "https://moxie.foxnews.com/google-publisher/politics.xml?format=xml"),
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