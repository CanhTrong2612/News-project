package com.example.news.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.news.MainActivity
import com.example.news.R
import com.example.news.WebViewActivity
import com.example.news.XmlPullParser.HandelXmlPullParser
import com.example.news.XmlPullParser.NetworkActivity
import com.example.news.adapter.NewsAdapter
import com.example.news.adapter.PhotoAdapter
import com.example.news.model.News
import com.example.news.model.Photo
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.relex.circleindicator.CircleIndicator
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MoiFragment : Fragment() {
    private  lateinit var tablayout : TabLayout
    private var viewPager2: ViewPager2? =null
    private var viewPager: ViewPager? =null
    private var circleIndication:CircleIndicator?= null
    private var rv: RecyclerView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moi, container, false)
        viewPager= view.findViewById(R.id.viewpager)
        circleIndication= view.findViewById(R.id.circle_indication)
        rv = view.findViewById(R.id.rv_main)
        displayViewPager()
        downloadXml("https://vtv.vn/trong-nuoc.rss")
       // downloadXml("https://vtc.vn/rss/kinh-te.rss")

        return view
    }
    fun dataImage():ArrayList<Photo>{
        val listImage = ArrayList<Photo>()
        listImage.add(Photo(R.drawable.my))
        listImage.add(Photo(R.drawable.gd))
        listImage.add(Photo(R.drawable.bd1))
        listImage.add(Photo(R.drawable.putin))

        return  listImage
    }
    fun displayViewPager() {
        val photoAdapter = PhotoAdapter(requireContext(), dataImage())
        viewPager!!.adapter = photoAdapter
        circleIndication!!.setViewPager(viewPager)
    }
    fun downloadXml(vararg urls: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            loadXmlFromNetwork(urls[0])
        }
    }
    @Throws(XmlPullParserException::class, IOException::class)
    private suspend fun loadXmlFromNetwork(urlString: String): String {
        val listNews  = downloadUrl(urlString)?.use { stream ->
            HandelXmlPullParser().Pasers(stream)
        } ?: emptyList()
        lifecycleScope.launch  {
            rv!!.layoutManager = LinearLayoutManager(requireContext())
            val adapter = NewsAdapter(requireContext(), listNews)
            rv!!.adapter = adapter
//            adapter.setOnItemClickListener(object : NewsAdapter.OnItemClickListener{
//                override fun onItemClick(position: Int) {
//                    val intent = Intent(context, WebViewActivity::class.java)
//                    intent.putExtra("link",listNews[position]!!.link)
//                    startActivity(intent)
//                }
//
//            })
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