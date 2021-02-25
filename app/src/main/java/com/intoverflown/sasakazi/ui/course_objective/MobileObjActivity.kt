package com.intoverflown.sasakazi.ui.course_objective

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.ui.course_objective.models.MobileViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class MobileObjActivity : AppCompatActivity() {

    // UI elements
    private var course_txtView : TextView? = null
    private var obj_description : TextView? = null
    private var instructor_name : TextView? = null
    private var cert_conditions : TextView? = null
    private var vidPlayerView : YouTubePlayerView? = null

    private lateinit var fullUrl : String
    private lateinit var mobileViewModel : MobileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_mobile)
        mobileViewModel = ViewModelProvider(this).get(MobileViewModel::class.java)

//        course_txtView = findViewById<View>(R.id.courseTxtView) as TextView
        obj_description = findViewById<View>(R.id.objectiveDescription) as TextView
        instructor_name = findViewById<View>(R.id.instructorName) as TextView
        cert_conditions = findViewById<View>(R.id.conditionsList) as TextView
        vidPlayerView = findViewById(R.id.youtubePlayerView)

        // set mutable LiveData
        setLiveDataHere()
    }

    private fun setLiveDataHere() {
//        mobileViewModel.course.observe(this) {
//            course_txtView!!.text = it
//        }

        mobileViewModel.textCourseObj.observe(this) {
            obj_description!!.text = it
        }

        mobileViewModel.textInstructorName.observe(this) {
            instructor_name!!.text = it
        }
        mobileViewModel.textCertRequirements.observe(this) {
            cert_conditions!!.text = it
        }

        // get url and extract the link id in the subsequent function
        fullUrl = mobileViewModel.youtubeLink.observe(this) {
            it
        }.toString()

        Log.i("YouTubeURL: ", fullUrl)

        playVideo()
    }


    private fun playVideo() {
        // extract link id first
        val extractedVidID : String? = fullUrl.substringAfterLast("youtu.be/")

        vidPlayerView!!.let { lifecycle.addObserver(it) }

        vidPlayerView!!.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = extractedVidID
                if (videoId != null) {
                    Log.i("YouTubeID: ", videoId)
                }

                videoId.let {
                    if (it != null) {
                        youTubePlayer.loadVideo(it, 0F)
                    }
                }
            }
        })
    }
}