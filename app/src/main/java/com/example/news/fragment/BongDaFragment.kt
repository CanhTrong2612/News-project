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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class BongDaFragment : Fragment() {
    private var rv:RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bong_da, container, false)
        rv = view.findViewById(R.id.rv_bongda)
        downloadXml("https://vtv.vn/the-thao.rss")
        return view
    }
    fun downloadXml(vararg urls: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO){
                loadXmlFromNetwork(urls[0])
            }

        }
    }
    @Throws(XmlPullParserException::class, IOException::class)
    private suspend fun loadXmlFromNetwork(urlString: String): String {
        val listNews  = downloadUrl(urlString)?.use { stream ->
            HandelXmlPullParser().Pasers(stream)
        } ?: emptyList()
        withContext(Dispatchers.Main) {
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