package com.flockdroid.app

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.ConsoleMessage
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        configureWebView()

        webView.loadUrl("https://flockmod.com/draw/")
    }

    private fun configureWebView() {
        val webSettings: WebSettings = webView.settings

        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webSettings.allowFileAccess = true

        webView.webViewClient = object : WebViewClient() {

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                val url = request?.url?.toString() ?: return null

                // Intercept flockmod.js and replace it with your modified version
                if (url.contains("flockmod.js")) {
                    Log.d("FlockMod", "Intercepting flockmod.js from: $url")
                    return try {
                        val modifiedJs = assets.open("flockmod.js")
                        WebResourceResponse(
                            "text/javascript",
                            "UTF-8",
                            modifiedJs
                        )
                    } catch (e: Exception) {
                        Log.e("FlockMod", "Failed to load modified flockmod.js", e)
                        null
                    }
                }

                return super.shouldInterceptRequest(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                Log.d("FlockMod-JS", "${message.message()} -- From line ${message.lineNumber()} of ${message.sourceId()}")
                return true
            }
        }

        WebView.setWebContentsDebuggingEnabled(true)
    }

}