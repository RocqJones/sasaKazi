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

class WebActivity : AppCompatActivity() {

    // UI elements
    private var webObjDescription : TextView? = null
    private var webInstructorName : TextView? = null
    private var webCertConditions : TextView? = null
    private var webVidPlayerView : YouTubePlayerView? = null
    private var webFloatActionBtn : FloatingActionButton? = null
    private var webFloatBtnView : View? = null
    private var webBaseLayout : ConstraintLayout? = null

    // Float btn elements
    private var webFloatTxtRes : TextView? = null
    private var webFloatTxtObj : TextView? = null
    private var webFloatTxtDis : TextView? = null
    private var webFloatTxtAss : TextView? = null
    private var webFloatResourceBtn : FloatingActionButton? = null
    private var webFloatObjectiveBtn : FloatingActionButton? = null
    private var webFloatAssignmentBtn : FloatingActionButton? = null
    private var webFloatDiscussionBtn : FloatingActionButton? = null

    // layouts View 'Gone'
    private var webResourceVisibility: View? = null
    private var webObjectiveVisibility: View? = null
    private var webAssignmentVisibility: View? = null

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

        // Float btn elements
        webFloatTxtObj = findViewById<View>(R.id.textViewObj) as TextView
        webFloatTxtRes = findViewById<View>(R.id.textViewResources) as TextView
        webFloatTxtDis = findViewById<View>(R.id.textViewDiscussion) as TextView
        webFloatTxtAss = findViewById<View>(R.id.textViewAssignment) as TextView
        webFloatResourceBtn = findViewById<View>(R.id.resources) as FloatingActionButton
        webFloatObjectiveBtn = findViewById<View>(R.id.objective) as FloatingActionButton
        webFloatAssignmentBtn = findViewById<View>(R.id.assignment) as FloatingActionButton
        webFloatDiscussionBtn = findViewById<View>(R.id.discussion) as FloatingActionButton

        // Views - include
        webResourceVisibility = findViewById(R.id.resourcesVisibility)
        webObjectiveVisibility = findViewById(R.id.objVisibility)
        webAssignmentVisibility = findViewById(R.id.assignmentVisibility)

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

                // show resources
                webFloatResourceBtn!!.setOnClickListener {
                    webResourceVisibility!!.visibility = View.VISIBLE
                    webAssignmentVisibility!!.visibility = View.GONE
                    webObjectiveVisibility!!.visibility = View.GONE
                    webFloatBtnView!!.visibility = View.GONE
                }

                webFloatTxtRes!!.setOnClickListener {
                    webResourceVisibility!!.visibility = View.VISIBLE
                    webAssignmentVisibility!!.visibility = View.GONE
                    webObjectiveVisibility!!.visibility = View.GONE
                    webFloatBtnView!!.visibility = View.GONE
                }

                // show assignments
                webFloatAssignmentBtn!!.setOnClickListener {
                    webAssignmentVisibility!!.visibility = View.VISIBLE
                    webObjectiveVisibility!!.visibility = View.GONE
                    webResourceVisibility!!.visibility = View.GONE
                    webFloatBtnView!!.visibility = View.GONE
                }

                webFloatTxtAss!!.setOnClickListener {
                    webAssignmentVisibility!!.visibility = View.VISIBLE
                    webObjectiveVisibility!!.visibility = View.GONE
                    webResourceVisibility!!.visibility = View.GONE
                    webFloatBtnView!!.visibility = View.GONE
                }

                // show course objective
                webFloatObjectiveBtn!!.setOnClickListener {
                    webObjectiveVisibility!!.visibility = View.VISIBLE
                    webAssignmentVisibility!!.visibility = View.GONE
                    webResourceVisibility!!.visibility = View.GONE
                    webFloatBtnView!!.visibility = View.GONE
                }

                webFloatTxtObj!!.setOnClickListener {
                    webObjectiveVisibility!!.visibility = View.VISIBLE
                    webAssignmentVisibility!!.visibility = View.GONE
                    webResourceVisibility!!.visibility = View.GONE
                    webFloatBtnView!!.visibility = View.GONE
                }

                // Intent to Discussion screen
//                webFloatDiscussionBtn!!.setOnClickListener { }
//                webFloatTxtDis!!.setOnClickListener { }
            }

            else {
                webFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}