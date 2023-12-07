package com.example.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val webView :WebView = findViewById(R.id.webview)
        val link = intent.getStringExtra("link")!!
        webView.loadUrl(link)

    }
}