package com.rss_reader.producer

import com.rss_reader.dto.Article
import com.rss_reader.dto.Feed
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

object ArticleProducer {
    private const val CHANNEL = "channel"
    val feeds = listOf(
        Feed("npr", "https://feeds.npr.org/1001/rss.xml"),
//        Feed("cnn", "http://rss.cnn.com/rss/cnn_topstories.rss"),
        Feed("nasa", "https://www.nasa.gov/rss/dyn/breaking_news.rss"),
        Feed("fox", "https://moxie.foxnews.com/google-publisher/politics.xml?format=xml"),
//        Feed("inv", "afasldkfj")
    )
    private val factory = DocumentBuilderFactory.newInstance()
    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error captured in $coroutineContext")
        println("Message: ${throwable.message}")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val producer = CoroutineScope(Dispatchers.IO).produce(handler) {
        feeds.forEach {
            send(fetchArticles(it))
        }
    }

    private fun fetchArticles(feed: Feed): List<Article> {
        val factory = factory.newDocumentBuilder()
        val xml = factory.parse(feed.url)
        val news = xml.getElementsByTagName(CHANNEL).item(0)

        return (0 until news.childNodes.length)
            .map { news.childNodes.item(it) }
            .filter { Node.ELEMENT_NODE == it.nodeType }
            .map { it as Element }
            .filter { "item" == it.tagName }
            .mapIndexed { index, element ->
                val title = element.getElementsByTagName("title").item(0).textContent
                val summary = element.getElementsByTagName("description").item(0).textContent
                Article(index == 0, feed.name, title, summary)
            }
    }

    fun search(query: String): ReceiveChannel<List<Article>> {
        val channel = Channel<List<Article>>(150)

        feeds.forEach { feed ->
            CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                search(feed, channel, query)
            }
        }

        return channel
    }

    private suspend fun search(
        feed: Feed,
        channel: SendChannel<List<Article>>,
        query: String
    ) {
        val factory = factory.newDocumentBuilder()
        val xml = factory.parse(feed.url)
        val news = xml.getElementsByTagName(CHANNEL).item(0)

        val articles = mutableListOf<Article>()

        (0 until news.childNodes.length)
            .map { news.childNodes.item(it) }
            .filter { Node.ELEMENT_NODE == it.nodeType }
            .map { it as Element }
            .filter { "item" == it.tagName }
            .forEach {
                val title = it.getElementsByTagName("title").item(0).textContent
                val summary = it.getElementsByTagName("description").item(0).textContent

                if (title.contains(query) || summary.contains(query)) {
//                    if (summary.contains("<div")) {
//                        summary = summary.substring(0, summary.indexOf("<div"))
//                    }

                    articles.add(Article(false, feed.name, title, summary))
                }
            }

        channel.send(articles)
    }
}