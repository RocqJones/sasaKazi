package com.intoverflown.sasakazi.ui.course_objective

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelDigitalMrkt
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class DigitalMarketingActivity : AppCompatActivity() {

    // UI elements
    private var digitalObjDescription : TextView? = null
    private var digitalInstructorName : TextView? = null
    private var digitalCertConditions : TextView? = null
    private var digitalVidPlayerView : YouTubePlayerView? = null
    private var digitalFloatActionBtn : FloatingActionButton? = null
    private var digitalFloatBtnView : View? = null
    private var digitalBaseLayout : ConstraintLayout? = null

    private lateinit var fullUrl : String
    private lateinit var viewModelDigitalMrkt : ViewModelDigitalMrkt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_digital_marketing)

        viewModelDigitalMrkt = ViewModelProvider(this).get(ViewModelDigitalMrkt::class.java)

        digitalFloatActionBtn = findViewById<View>(R.id.floatingActionBtn) as FloatingActionButton
        digitalObjDescription = findViewById<View>(R.id.objectiveDescription) as TextView
        digitalInstructorName = findViewById<View>(R.id.instructorName) as TextView
        digitalCertConditions = findViewById<View>(R.id.conditionsList) as TextView
        digitalVidPlayerView = findViewById(R.id.youtubePlayerView)
        digitalFloatBtnView = findViewById(R.id.floatingBtnView)
        digitalBaseLayout = findViewById(R.id.rootLayout)

        // set mutable LiveData
        setDigitalMrktLiveDataHere()
    }

    private fun setDigitalMrktLiveDataHere() {
        viewModelDigitalMrkt.textCourseObj.observe(this) {
            digitalObjDescription!!.text = it
        }

        viewModelDigitalMrkt.textInstructorName.observe(this) {
            digitalInstructorName!!.text = it
        }
        viewModelDigitalMrkt.textCertRequirements.observe(this) {
            digitalCertConditions!!.text = it
        }

        // get url and extract the link id in the subsequent function
        viewModelDigitalMrkt.youtubeLink.observe(this) { it ->
            fullUrl = it

            Log.i("YouTubeURL: ", fullUrl)

            // extract link id from url
            val extractedVidID : String? = fullUrl.substringAfterLast("youtu.be/")

            digitalVidPlayerView!!.let { lifecycle.addObserver(it) }

            digitalVidPlayerView!!.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
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

        digitalFloatActionBtn!!.setOnClickListener {
            if (digitalFloatBtnView!!.visibility == View.GONE) {
                digitalFloatBtnView!!.visibility = View.VISIBLE
                // when you touch outside the view
                digitalBaseLayout!!.setOnClickListener {
                    if (digitalFloatBtnView!!.visibility == View.VISIBLE) {
                        digitalFloatBtnView!!.visibility = View.GONE
                    }
                }
            }

            else {
                digitalFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}