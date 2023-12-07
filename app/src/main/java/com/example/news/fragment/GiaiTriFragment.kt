package com.example.news.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.XmlPullParser.HandelXmlPullParser
import com.example.news.adapter.NewsAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class GiaiTriFragment : Fragment() {
    private var rv: RecyclerView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_giai_tri, container, false)
        rv = view.findViewById(R.id.rv_giaitri)
        downloadXml("https://vtv.vn/van-hoa-giai-tri.rss")
        return view
    }
    fun downloadXml(vararg urls: String) {
        GlobalScope.launch {
            loadXmlFromNetwork(urls[0])
        }
    }
    @Throws(XmlPullParserException::class, IOException::class)
    private fun loadXmlFromNetwork(urlString: String): String {
        val listNews  = downloadUrl(urlString)?.use { stream ->
            HandelXmlPullParser().Pasers(stream)
        } ?: emptyList()
        lifecycleScope.launch {
            rv!!.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            val adapter = NewsAdapter(requireContext(), listNews)
            rv!!.adapter = adapter
        }



        return StringBuilder().apply {
            listNews.forEach {
                    entry ->

                append("<p><a href='")
                append(entry!!.link)
                append("'>" + entry.title + "</a></p>")
            }
        }.toString()
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