package com.rss_reader.producer

import com.rss_reader.dto.Article
import com.rss_reader.dto.Feed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

object ArticleProducer {
    private const val CHANNEL = "channel"
    val feeds = listOf(
        Feed("npr", "https://feeds.npr.org/1001/rss.xml"),
        Feed("cnn", "http://rss.cnn.com/rss/cnn_topstories.rss"),
        Feed("fox", "https://moxie.foxnews.com/google-publisher/politics.xml?format=xml"),
//        Feed("inv", "afasldkfj")
    )
    val factory = DocumentBuilderFactory.newInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    val producer = CoroutineScope(Dispatchers.IO).produce(Dispatchers.IO) {
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
            .map {
                val title = it.getElementsByTagName("title").item(0).textContent
                val summary = it.getElementsByTagName("description").item(0).textContent
                Article(feed.name, title, summary)
            }
    }
}