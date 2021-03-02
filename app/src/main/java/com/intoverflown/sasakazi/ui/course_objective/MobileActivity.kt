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
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelMobile
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class MobileActivity : AppCompatActivity() {

    // UI elements
    private var mobileObjDescription : TextView? = null
    private var mobileInstructorName : TextView? = null
    private var mobileCertConditions : TextView? = null
    private var mobileVidPlayerView : YouTubePlayerView? = null
    private var mobileFloatActionBtn : FloatingActionButton? = null
    private var mobileFloatBtnView : View? = null
    private var mobileBaseLayout : ConstraintLayout? = null

    // Float btn elements
    private var mobileFloatTxtRes : TextView? = null
    private var mobileFloatTxtObj : TextView? = null
    private var mobileFloatTxtDis : TextView? = null
    private var mobileFloatTxtAss : TextView? = null
    private var mobileFloatResourceBtn : FloatingActionButton? = null
    private var mobileFloatObjectiveBtn : FloatingActionButton? = null
    private var mobileFloatAssignmentBtn : FloatingActionButton? = null
    private var mobileFloatDiscussionBtn : FloatingActionButton? = null

    // layouts View 'Gone'
    private var mobileResourceVisibility: View? = null
    private var mobileObjectiveVisibility: View? = null
    private var mobileAssignmentVisibility: View? = null

    private lateinit var fullUrl : String
    private lateinit var viewModelMobile : ViewModelMobile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_mobile)
        viewModelMobile = ViewModelProvider(this).get(ViewModelMobile::class.java)

        mobileObjDescription = findViewById<View>(R.id.objectiveDescription) as TextView
        mobileInstructorName = findViewById<View>(R.id.instructorName) as TextView
        mobileCertConditions = findViewById<View>(R.id.conditionsList) as TextView
        mobileVidPlayerView = findViewById(R.id.youtubePlayerView)
        mobileFloatActionBtn = findViewById<View>(R.id.floatingActionBtn) as FloatingActionButton
        mobileFloatBtnView = findViewById(R.id.floatingBtnView)
        mobileBaseLayout = findViewById(R.id.rootLayout)

        // Float btn elements
        mobileFloatTxtObj = findViewById<View>(R.id.textViewObj) as TextView
        mobileFloatTxtRes = findViewById<View>(R.id.textViewResources) as TextView
        mobileFloatTxtDis = findViewById<View>(R.id.textViewDiscussion) as TextView
        mobileFloatTxtAss = findViewById<View>(R.id.textViewAssignment) as TextView
        mobileFloatResourceBtn = findViewById<View>(R.id.resources) as FloatingActionButton
        mobileFloatObjectiveBtn = findViewById<View>(R.id.objective) as FloatingActionButton
        mobileFloatAssignmentBtn = findViewById<View>(R.id.assignment) as FloatingActionButton
        mobileFloatDiscussionBtn = findViewById<View>(R.id.discussion) as FloatingActionButton

        // Views - include
        mobileResourceVisibility = findViewById(R.id.resourcesVisibility)
        mobileObjectiveVisibility = findViewById(R.id.objVisibility)
        mobileAssignmentVisibility = findViewById(R.id.assignmentVisibility)

        // set mutable LiveData
        setLiveDataHere()
    }

    private fun setLiveDataHere() {
        viewModelMobile.textCourseObj.observe(this) {
            mobileObjDescription!!.text = it
        }

        viewModelMobile.textInstructorName.observe(this) {
            mobileInstructorName!!.text = it
        }
        viewModelMobile.textCertRequirements.observe(this) {
            mobileCertConditions!!.text = it
        }

        // get url and extract the link id in the subsequent function
        viewModelMobile.youtubeLink.observe(this) { it ->
            fullUrl = it

            Log.i("YouTubeURL: ", fullUrl)

            // extract link id from url
            val extractedVidID : String? = fullUrl.substringAfterLast("youtu.be/")

            mobileVidPlayerView!!.let { lifecycle.addObserver(it) }

            mobileVidPlayerView!!.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
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

//            mobileVidPlayerView!!.enterFullScreen()
//            mobileVidPlayerView!!.exitFullScreen()
//            mobileVidPlayerView!!.isFullScreen()
//            mobileVidPlayerView!!.toggleFullScreen()
        }

        mobileFloatActionBtn!!.setOnClickListener {
            if (mobileFloatBtnView!!.visibility == View.GONE) {
                mobileFloatBtnView!!.visibility = View.VISIBLE
                // when you touch outside the view
                mobileBaseLayout!!.setOnClickListener {
                    if (mobileFloatBtnView!!.visibility == View.VISIBLE) {
                        mobileFloatBtnView!!.visibility = View.GONE
                    }
                }

                // show resources
                mobileFloatResourceBtn!!.setOnClickListener {
                    mobileResourceVisibility!!.visibility = View.VISIBLE
                    mobileAssignmentVisibility!!.visibility = View.GONE
                    mobileObjectiveVisibility!!.visibility = View.GONE
                    mobileFloatBtnView!!.visibility = View.GONE
                }

                mobileFloatTxtRes!!.setOnClickListener {
                    mobileResourceVisibility!!.visibility = View.VISIBLE
                    mobileAssignmentVisibility!!.visibility = View.GONE
                    mobileObjectiveVisibility!!.visibility = View.GONE
                    mobileFloatBtnView!!.visibility = View.GONE
                }

                // show assignments
                mobileFloatAssignmentBtn!!.setOnClickListener {
                    mobileAssignmentVisibility!!.visibility = View.VISIBLE
                    mobileObjectiveVisibility!!.visibility = View.GONE
                    mobileResourceVisibility!!.visibility = View.GONE
                    mobileFloatBtnView!!.visibility = View.GONE
                }

                mobileFloatTxtAss!!.setOnClickListener {
                    mobileAssignmentVisibility!!.visibility = View.VISIBLE
                    mobileObjectiveVisibility!!.visibility = View.GONE
                    mobileResourceVisibility!!.visibility = View.GONE
                    mobileFloatBtnView!!.visibility = View.GONE
                }

                // show course objective
                mobileFloatObjectiveBtn!!.setOnClickListener {
                    mobileObjectiveVisibility!!.visibility = View.VISIBLE
                    mobileAssignmentVisibility!!.visibility = View.GONE
                    mobileResourceVisibility!!.visibility = View.GONE
                    mobileFloatBtnView!!.visibility = View.GONE
                }

                mobileFloatTxtObj!!.setOnClickListener {
                    mobileObjectiveVisibility!!.visibility = View.VISIBLE
                    mobileAssignmentVisibility!!.visibility = View.GONE
                    mobileResourceVisibility!!.visibility = View.GONE
                    mobileFloatBtnView!!.visibility = View.GONE
                }

                // Intent to Discussion screen
//                mobileFloatDiscussionBtn!!.setOnClickListener { }
//                mobileFloatTxtDis!!.setOnClickListener {  }
            }

            else {
                mobileFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}