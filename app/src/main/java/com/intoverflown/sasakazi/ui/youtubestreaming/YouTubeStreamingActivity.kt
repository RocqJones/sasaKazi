package com.intoverflown.sasakazi.ui.youtubestreaming

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.intoverflown.sasakazi.R


class YouTubeStreamingActivity : AppCompatActivity() {

    private var youTubeWebView : WebView? = null
    private var yProgressbar : ProgressBar? =null
    private lateinit var fullUrl : String
    private lateinit var ytsViewModel: YTSViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_tube_streaming)

        ytsViewModel = ViewModelProvider(this).get(YTSViewModel::class.java)

        youTubeWebView = findViewById(R.id.youTubeWebView)

        yProgressbar = findViewById(R.id.progressBar3)

        setUpWebView()

//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (youTubeWebView!!.canGoBack()) {
//                    youTubeWebView!!.goBack()
//                } else {
//                    isEnabled = false
//
////                    requireActivity().onBackPressed()
//                }
//            }
//        }
////        requireActivity()
//        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpWebView() {
        // make sure it does not exit the fragment
        youTubeWebView!!.webViewClient = WebViewClient()

        ytsViewModel.ytUrl.observe(this, Observer {
            fullUrl = it

            //hide progress bar
            yProgressbar!!.visibility = View.GONE

            // load web
            youTubeWebView.apply {
                this!!.loadUrl(fullUrl)
                settings.javaScriptEnabled = true
                settings.safeBrowsingEnabled = true
            }
        })
    }
}