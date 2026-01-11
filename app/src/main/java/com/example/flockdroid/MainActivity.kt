package com.flockdroid.app

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create WebView programmatically
        webView = WebView(this)
        setContentView(webView)

        // Configure WebView settings
        configureWebView()

        // Load the website
        webView.loadUrl("https://flockmod.com/draw/")
    }

    private fun configureWebView() {
        val webSettings: WebSettings = webView.settings

        // Enable JavaScript
        webSettings.javaScriptEnabled = true

        // Enable DOM storage
        webSettings.domStorageEnabled = true

        // Enable database storage
        webSettings.databaseEnabled = true

        // Enable zoom controls
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        // Enable responsive layout
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        // Enable mixed content
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // Handle page navigation
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // JavaScript injection will go here later
            }
        }

        // Handle JavaScript dialogs
        webView.webChromeClient = WebChromeClient()

        // Enable remote debugging
        WebView.setWebContentsDebuggingEnabled(true)
    }
}