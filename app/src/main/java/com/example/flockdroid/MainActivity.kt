package com.flockdroid.app

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.ConsoleMessage
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

        // Enable file access for localStorage (needed for your mod script)
        webSettings.allowFileAccess = true

        // Handle page navigation
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Inject your JavaScript when page finishes loading
                injectJavaScript()
            }
        }

        // Handle JavaScript dialogs and console messages
        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                // Log console.log() messages from your JavaScript
                Log.d("FlockMod-JS", "${message.message()} -- From line ${message.lineNumber()} of ${message.sourceId()}")
                return true
            }
        }

        // Enable remote debugging
        WebView.setWebContentsDebuggingEnabled(true)
    }

    private fun injectJavaScript() {
        try {
            // Read your injection.js file from assets folder
            val inputStream = assets.open("injected.js")
            val jsCode = inputStream.bufferedReader().use { it.readText() }

            // Inject it into the page
            webView.evaluateJavascript(jsCode) { result ->
                Log.d("FlockMod", "JavaScript injection completed")
            }

            Log.d("FlockMod", "JavaScript file injected successfully")
        } catch (e: Exception) {
            Log.e("FlockMod", "Failed to inject JavaScript", e)
        }
    }
}