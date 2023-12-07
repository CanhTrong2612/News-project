package com.example.news.XmlPullParser

import android.preference.PreferenceManager
import android.webkit.WebView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.Resource
import com.example.news.R
import com.example.news.adapter.NewsAdapter
import com.example.news.model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
class NetworkActivity {
    companion object {
        const val WIFI = "Wi-Fi"
        const val ANY = "Any"
        const val SO_URL = ""
        // Whether there is a Wi-Fi connection.
        private var wifiConnected = false
        // Whether there is a mobile connection.
        private var mobileConnected = false

        // Whether the display should be refreshed.
        var refreshDisplay = true
        // The user's current network preference setting.
        var sPref: String? = null
    }

    // Asynchronously downloads the XML feed from stackoverflow.com.
    fun loadPage() {

        if (sPref.equals(ANY) && (wifiConnected || mobileConnected)) {
            downloadXml(SO_URL)
        } else if (sPref.equals(WIFI) && wifiConnected) {
            downloadXml(SO_URL)
        } else {
            // Show error.
        }
    }
    // Implementation of Kotlin coroutines used to download XML feed from stackoverflow.com.
    fun downloadXml(vararg urls: String) {
        GlobalScope.launch(Dispatchers.IO) {
                loadXmlFromNetwork(urls[0])
        }
    }
    // Uploads XML from stackoverflow.com, parses it, and combines it with
// HTML markup. Returns HTML string.
    @Throws(XmlPullParserException::class, IOException::class)
    private fun loadXmlFromNetwork(urlString: String): String {
        val listNews = downloadUrl(urlString)?.use { stream ->
            HandelXmlPullParser().Pasers(stream)
        } ?: emptyList()

        return  listNews.toString()
    }
    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): InputStream? {
        val url = URL(urlString)
        return (url.openConnection() as? HttpURLConnection)?.run {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "GET"
            doInput = true
            // Starts the query.
            connect()
            inputStream
        }
    }
}