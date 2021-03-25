package com.intoverflown.sasakazi.ui.blog

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.intoverflown.sasakazi.R

class BlogFragment : Fragment() {

    private var webView : WebView? = null
    private var blogProgressBar : ProgressBar? = null
    private lateinit var fullUrl : String
    private lateinit var blogViewModel: BlogViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        blogViewModel =
                ViewModelProvider(this).get(BlogViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_blog, container, false)
        webView = root.findViewById(R.id.webView)

        blogViewModel = root.findViewById(R.id.blogProgressBar)

        setUpWebView()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView!!.canGoBack()) {
                    webView!!.goBack()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        // make sure it does not exit the fragment
        webView!!.webViewClient = WebViewClient()

        blogViewModel.webUrl.observe(viewLifecycleOwner, Observer {
            fullUrl = it

            if (blogProgressBar != null) {
                blogProgressBar!!.visibility = View.GONE
            }

            // load web
            webView.apply {
                this!!.loadUrl(fullUrl)
                settings.javaScriptEnabled = true
                settings.safeBrowsingEnabled = true
            }
        })
    }
}