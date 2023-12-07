package com.example.news

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.news.XmlPullParser.HandelXmlPullParser
import com.example.news.XmlPullParser.NetworkActivity
import com.example.news.adapter.FragmentAdapter
import com.example.news.adapter.NewsAdapter
import com.example.news.adapter.PhotoAdapter
import com.example.news.databinding.ActivityMainBinding
import com.example.news.firebase.FirestoresClass
import com.example.news.fragment.MoiFragment
import com.example.news.model.News
import com.example.news.model.Photo
import com.example.news.model.User
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.relex.circleindicator.CircleIndicator
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {
    private var binding :ActivityMainBinding? = null
    private var toolbar : Toolbar?= null
    private var drawerLayout :DrawerLayout?= null
    private var viewPager: ViewPager?= null
    private var circleIndication: CircleIndicator?= null
    private var rv:RecyclerView?= null
    private  lateinit var tablayout :TabLayout
    private var viewPager2: ViewPager2? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        tablayout()
      //  displayViewPager()
        FirestoresClass().getUserDetail(this)
        binding?.navView?.setNavigationItemSelectedListener(this)
       // downloadXml("https://vtv.vn/trong-nuoc.rss")

    }
    fun actionBar(){
        toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title= ""
        toolbar!!.setNavigationIcon(R.drawable.baseline_menu_24)
        toolbar?.setNavigationOnClickListener {
            toggleDrawer()
        }

    }
    fun toggleDrawer() {
         drawerLayout = findViewById(R.id.layout_drawer);
        if ( drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout!!.openDrawer(GravityCompat.START);
        }
    }
    fun tablayout(){
        tablayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.viewpager2)
        val adapter = FragmentAdapter(supportFragmentManager,lifecycle)
        viewPager2!!.adapter = adapter
        TabLayoutMediator(tablayout, viewPager2!!){ tab, position->
            when(position){
                0->{
                    tab.text = "Trang chủ"
                }
                1->{
                    tab.text = "Công nghệ"
                }
                2->{
                    tab.text = "Độc & lạ"
                }

                3->{
                    tab.text = "Giải trí"
                }

                4->{
                    tab.text = "Pháp luật"
                }
                5->{
                    tab.text = "Sức khỏe"
                }
                6->{
                    tab.text = "Giáo dục"
                }
                7->{
                    tab.text = "Bóng đá"
                }
            }
        }.attach()
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
        viewPager = findViewById(R.id.viewpager)
        circleIndication = findViewById(R.id.circle_indication)
        val photoAdapter = PhotoAdapter(this, dataImage())
        viewPager!!.adapter = photoAdapter
        circleIndication!!.setViewPager(viewPager)

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile ->{
                startActivity(Intent(this,SettingActivity::class.java))
            }
            R.id.logout->{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            R.id.home->{
                startActivity(Intent(this,MainActivity::class.java))
            }


        }
        return true
    }

//    fun downloadXml(vararg urls: String) {
//        GlobalScope.launch {
//            loadXmlFromNetwork(urls[0])
//        }
//    }
//    @Throws(XmlPullParserException::class, IOException::class)
//    private fun loadXmlFromNetwork(urlString: String): String {
//        val listNews  = downloadUrl(urlString)?.use { stream ->
//            HandelXmlPullParser().Pasers(stream)
//        } ?: emptyList()
//        runOnUiThread(Runnable {
//            rv = findViewById(R.id.rv_main)
//            rv!!.layoutManager = LinearLayoutManager(this)
//            val adapter = NewsAdapter(this, listNews)
//
//            rv!!.adapter = adapter
//        })
//
//
//        return StringBuilder().apply {
//            listNews.forEach {
//                    entry ->
//
//                append("<p><a href='")
//                append(entry!!.link)
//                append("'>" + entry.title + "</a></p>")
//            }
//        }.toString()
//    }
//    @Throws(IOException::class)
//    private fun downloadUrl(urlString: String): InputStream? {
//        val url = URL(urlString)
//        return (url.openConnection() as? HttpURLConnection)?.run {
//            readTimeout = 10000
//            connectTimeout = 15000
//            requestMethod = "GET"
//            doInput = true
//            // Starts the query.
//            connect()
//            inputStream
//        }
//    }


    fun  navigationUserDetail(user: User){
        val image : CircleImageView = findViewById(R.id.nav_user_image)
        val email :TextView = findViewById(R.id.tv_email_nav)
        val name : TextView = findViewById(R.id.tv_username_nav)
        name.text =user.username
        email.text = user.email

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .into(image)
    }


}