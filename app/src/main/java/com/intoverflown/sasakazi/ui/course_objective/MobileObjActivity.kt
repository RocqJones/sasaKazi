package com.intoverflown.sasakazi.ui.course_objective

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.ui.course_objective.models.MobileViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener

class MobileObjActivity : AppCompatActivity() {

    // UI elements
    private var course_txtView : TextView? = null
    private var intro_video : VideoView? = null
    private var obj_description : TextView? = null
    private var instructor_name : TextView? = null
    private var cert_conditions : TextView? = null
    private var vidPlayerView : YouTubePlayerView? = null

    private var fullUrl : String? = null

    private lateinit var mobileViewModel : MobileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_mobile)
        mobileViewModel = ViewModelProvider(this).get(MobileViewModel::class.java)

        course_txtView = findViewById<View>(R.id.courseTxtView) as TextView
        intro_video = findViewById<View>(R.id.courseIntroVideoView) as VideoView
        obj_description = findViewById<View>(R.id.objectiveDescription) as TextView
        instructor_name = findViewById<View>(R.id.instructorName) as TextView
        cert_conditions = findViewById<View>(R.id.conditionsList) as TextView
        vidPlayerView = findViewById<View>(R.id.youtubePlayerView) as YouTubePlayerView

        // set mutable LiveData
        setLiveDataHere()
    }

    private fun setLiveDataHere() {
        mobileViewModel.course.observe(this) {
            course_txtView!!.text = it
        }

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
        fullUrl = mobileViewModel.youtubeLink.observe(this) { it }.toString()
        val extractedVidID : String? = fullUrl!!.substringAfterLast("youtu.be/")

        playVideo()
    }


    private fun playVideo() {
        // extract link id first
        val extractedVidID : String? = fullUrl!!.substringAfterLast("youtu.be/")
//        vidPlayerView?.let { getLifecycle().addObserver(it) }


//        val mediacontroller = MediaController(this)
//        mediacontroller.setAnchorView(intro_video)
//        val vidUrl : Uri = Uri.parse(
//                mobileViewModel.youtubeLink.observe(this) {
//                    it
//                }.toString()
//        )
//        intro_video!!.setMediaController(mediacontroller)
//        intro_video!!.setVideoURI(vidUrl)
//        intro_video!!.requestFocus()
//        intro_video!!.start()
    }
}