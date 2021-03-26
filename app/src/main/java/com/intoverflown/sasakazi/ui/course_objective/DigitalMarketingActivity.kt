package com.intoverflown.sasakazi.ui.course_objective

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelDigitalMrkt
import com.intoverflown.sasakazi.ui.discussions.ChatDigitalMrkt
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

    // Float btn Linear Layout elements
    private var digitalLinearLayoutObj : LinearLayout? = null
    private var digitalLinearLayoutRes : LinearLayout? = null
    private var digitalLinearLayoutAss : LinearLayout? = null
    private var digitalLinearLayoutDis : LinearLayout? = null

    // layouts View 'Gone'
    private var digitalResourceVisibility: View? = null
    private var digitalObjectiveVisibility: View? = null
    private var digitalAssignmentVisibility: View? = null

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

        // Float btn elements
        digitalLinearLayoutObj = findViewById(R.id.linearLayoutObj)
        digitalLinearLayoutRes = findViewById(R.id.linearLayoutRes)
        digitalLinearLayoutAss = findViewById(R.id.linearLayoutAss)
        digitalLinearLayoutDis = findViewById(R.id.linearLayoutDis)

        // Views - include
        digitalResourceVisibility = findViewById(R.id.resourcesVisibility)
        digitalObjectiveVisibility = findViewById(R.id.objVisibility)
        digitalAssignmentVisibility = findViewById(R.id.assignmentVisibility)

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

                // show resources
                digitalLinearLayoutRes!!.setOnClickListener {
                    digitalResourceVisibility!!.visibility = View.VISIBLE
                    digitalAssignmentVisibility!!.visibility = View.GONE
                    digitalObjectiveVisibility!!.visibility = View.GONE
                    digitalFloatBtnView!!.visibility = View.GONE
                }

                // show assignments
                digitalLinearLayoutAss!!.setOnClickListener {
                    digitalAssignmentVisibility!!.visibility = View.VISIBLE
                    digitalObjectiveVisibility!!.visibility = View.GONE
                    digitalResourceVisibility!!.visibility = View.GONE
                    digitalFloatBtnView!!.visibility = View.GONE
                }

                // show course objective
                digitalLinearLayoutObj!!.setOnClickListener {
                    digitalObjectiveVisibility!!.visibility = View.VISIBLE
                    digitalAssignmentVisibility!!.visibility = View.GONE
                    digitalResourceVisibility!!.visibility = View.GONE
                    digitalFloatBtnView!!.visibility = View.GONE
                }

                // Intent to Discussion screen
                digitalLinearLayoutDis!!.setOnClickListener {
                    val intent = Intent(this@DigitalMarketingActivity, ChatDigitalMrkt::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

            else {
                digitalFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}