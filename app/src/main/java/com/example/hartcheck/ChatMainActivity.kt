package com.example.hartcheck

import android.net.http.SslError
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi

class ChatMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)
        webViewSetup()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun webViewSetup(){
        val wbWeb: WebView = findViewById(R.id.wb_webView)
        wbWeb.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val patientName = intent.getStringExtra("patientName")

                val userName = patientName
                val jsCode = "document.getElementById(\"userInput\").value ='$userName';"
                wbWeb.evaluateJavascript(jsCode, null)
            }
        }
        wbWeb.apply {
            loadUrl("https://10.0.2.2:7215/Patient/ChatDoc")
            settings.javaScriptEnabled = true
            Log.d("wbWeb","WORKED")
        }
    }
}