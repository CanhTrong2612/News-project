package com.example.news

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Toast
import com.example.news.databinding.ActivityWebViewBinding
import com.example.news.firebase.FirestoresClass
import com.example.news.model.News

class WebViewActivity : AppCompatActivity() {
    private var binding: ActivityWebViewBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        val webView: WebView = findViewById(R.id.webview)
        val link = intent.getStringExtra("link")!!
        var news = intent.getParcelableExtra<News>("news")
        webView.loadUrl(link)

    }

    fun actionBar() {
        setSupportActionBar(binding?.tb)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title =""



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
//        val menuIcon: Drawable? = menu.findItem(R.menu.option_menu).icon
//        menuIcon?.mutate()?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {
            R.id.read_later -> {
                save()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun save() {
        var news = intent.getParcelableExtra<News>("news")
        news?.let { FirestoresClass().addReadlater(this, it) }
        Toast.makeText(this,"Add to the list of additions later",Toast.LENGTH_SHORT).show()
    }
}