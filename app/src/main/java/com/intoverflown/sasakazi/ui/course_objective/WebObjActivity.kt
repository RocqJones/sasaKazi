package com.intoverflown.sasakazi.ui.course_objective

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelWeb
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class WebObjActivity : AppCompatActivity() {

    // UI elements
    private var webObjDescription : TextView? = null
    private var webInstructorName : TextView? = null
    private var webCertConditions : TextView? = null
    private var webVidPlayerView : YouTubePlayerView? = null
    private var webFloatActionBtn : FloatingActionButton? = null
    private var webFloatBtnView : View? = null
    private var webBaseLayout : ConstraintLayout? = null

    private lateinit var fullUrl : String
    private lateinit var viewModelWeb : ViewModelWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_web)

        viewModelWeb = ViewModelProvider(this).get(ViewModelWeb::class.java)

        webFloatActionBtn = findViewById<View>(R.id.floatingActionBtn) as FloatingActionButton
        webObjDescription = findViewById<View>(R.id.objectiveDescription) as TextView
        webInstructorName = findViewById<View>(R.id.instructorName) as TextView
        webCertConditions = findViewById<View>(R.id.conditionsList) as TextView
        webVidPlayerView = findViewById(R.id.youtubePlayerView)
        webFloatBtnView = findViewById(R.id.floatingBtnView)
        webBaseLayout = findViewById(R.id.rootLayout)

        // set mutable LiveData
        setWebLiveDataHere()
    }

    private fun setWebLiveDataHere() {
        viewModelWeb.textCourseObj.observe(this) {
            webObjDescription!!.text = it
        }

        viewModelWeb.textInstructorName.observe(this) {
            webInstructorName!!.text = it
        }
        viewModelWeb.textCertRequirements.observe(this) {
            webCertConditions!!.text = it
        }

        // get url and extract the link id in the subsequent function
        viewModelWeb.youtubeLink.observe(this) { it ->
            fullUrl = it

            Log.i("YouTubeURL: ", fullUrl)

            // extract link id from url
            val extractedVidID : String? = fullUrl.substringAfterLast("youtu.be/")

            webVidPlayerView!!.let { lifecycle.addObserver(it) }

            webVidPlayerView!!.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    if (extractedVidID != null) {
                        Log.i("YouTubeID: ", extractedVidID)
                    }

                    extractedVidID.let { it1 ->
                        if (it1 != null) {
                            youTubePlayer.cueVideo(it1, 0F)

                        }
                    }
                }
            })
        }

        webFloatActionBtn!!.setOnClickListener {
            if (webFloatBtnView!!.visibility == View.GONE) {
                webFloatBtnView!!.visibility = View.VISIBLE
                // when you touch outside the view
                webBaseLayout!!.setOnClickListener {
                    if (webFloatBtnView!!.visibility == View.VISIBLE) {
                        webFloatBtnView!!.visibility = View.GONE
                    }
                }
            }

            else {
                webFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}