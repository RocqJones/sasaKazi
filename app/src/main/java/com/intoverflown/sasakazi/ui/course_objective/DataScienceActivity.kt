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
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelDataScience
import com.intoverflown.sasakazi.ui.discussion_migrations.ChatDataScience
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class DataScienceActivity : AppCompatActivity() {

    // UI elements
    private var dataSciObjDescription : TextView? = null
    private var dataSciInstructorName : TextView? = null
    private var dataSciCertConditions : TextView? = null
    private var dataSciVidPlayerView : YouTubePlayerView? = null
    private var dataSciFloatActionBtn : FloatingActionButton? = null
    private var dataSciFloatBtnView : View? = null
    private var dataSciBaseLayout : ConstraintLayout? = null

    // Float btn Linear Layout elements
    private var dataSciLinearLayoutObj : LinearLayout? = null
    private var dataSciLinearLayoutRes : LinearLayout? = null
    private var dataSciLinearLayoutAss : LinearLayout? = null
    private var dataSciLinearLayoutDis : LinearLayout? = null

    // layouts View 'Gone'
    private var dataSciResourceVisibility: View? = null
    private var dataSciObjectiveVisibility: View? = null
    private var dataSciAssignmentVisibility: View? = null

    private lateinit var fullUrl : String
    private lateinit var viewModeldataSci : ViewModelDataScience

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_data_science)

        viewModeldataSci = ViewModelProvider(this).get(ViewModelDataScience::class.java)

        dataSciFloatActionBtn = findViewById<View>(R.id.floatingActionBtn) as FloatingActionButton
        dataSciObjDescription = findViewById<View>(R.id.objectiveDescription) as TextView
        dataSciInstructorName = findViewById<View>(R.id.instructorName) as TextView
        dataSciCertConditions = findViewById<View>(R.id.conditionsList) as TextView
        dataSciVidPlayerView = findViewById(R.id.youtubePlayerView)
        dataSciFloatBtnView = findViewById(R.id.floatingBtnView)
        dataSciBaseLayout = findViewById(R.id.rootLayout)

        // Float btn elements
        dataSciLinearLayoutObj = findViewById(R.id.linearLayoutObj)
        dataSciLinearLayoutRes = findViewById(R.id.linearLayoutRes)
        dataSciLinearLayoutAss = findViewById(R.id.linearLayoutAss)
        dataSciLinearLayoutDis = findViewById(R.id.linearLayoutDis)

        // Views - include
        dataSciResourceVisibility = findViewById(R.id.resourcesVisibility)
        dataSciObjectiveVisibility = findViewById(R.id.objVisibility)
        dataSciAssignmentVisibility = findViewById(R.id.assignmentVisibility)

        // set mutable LiveData
        setDigitalMrktLiveDataHere()
    }

    private fun setDigitalMrktLiveDataHere() {
        viewModeldataSci.textCourseObj.observe(this) {
            dataSciObjDescription!!.text = it
        }

        viewModeldataSci.textInstructorName.observe(this) {
            dataSciInstructorName!!.text = it
        }
        viewModeldataSci.textCertRequirements.observe(this) {
            dataSciCertConditions!!.text = it
        }

        // get url and extract the link id in the subsequent function
        viewModeldataSci.youtubeLink.observe(this) { it ->
            fullUrl = it

            Log.i("YouTubeURL: ", fullUrl)

            // extract link id from url
            val extractedVidID : String? = fullUrl.substringAfterLast("youtu.be/")

            dataSciVidPlayerView!!.let { lifecycle.addObserver(it) }

            dataSciVidPlayerView!!.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
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

        dataSciFloatActionBtn!!.setOnClickListener {
            if (dataSciFloatBtnView!!.visibility == View.GONE) {
                dataSciFloatBtnView!!.visibility = View.VISIBLE
                // when you touch outside the view
                dataSciBaseLayout!!.setOnClickListener {
                    if (dataSciFloatBtnView!!.visibility == View.VISIBLE) {
                        dataSciFloatBtnView!!.visibility = View.GONE
                    }
                }

                // show resources
                dataSciLinearLayoutRes!!.setOnClickListener {
                    dataSciResourceVisibility!!.visibility = View.VISIBLE
                    dataSciAssignmentVisibility!!.visibility = View.GONE
                    dataSciObjectiveVisibility!!.visibility = View.GONE
                    dataSciFloatBtnView!!.visibility = View.GONE
                }

                // show assignments
                dataSciLinearLayoutAss!!.setOnClickListener {
                    dataSciAssignmentVisibility!!.visibility = View.VISIBLE
                    dataSciObjectiveVisibility!!.visibility = View.GONE
                    dataSciResourceVisibility!!.visibility = View.GONE
                    dataSciFloatBtnView!!.visibility = View.GONE
                }

                // show course objective
                dataSciLinearLayoutObj!!.setOnClickListener {
                    dataSciObjectiveVisibility!!.visibility = View.VISIBLE
                    dataSciAssignmentVisibility!!.visibility = View.GONE
                    dataSciResourceVisibility!!.visibility = View.GONE
                    dataSciFloatBtnView!!.visibility = View.GONE
                }

                // Intent to Discussion screen
                dataSciLinearLayoutDis!!.setOnClickListener {
                    val intent = Intent(this@DataScienceActivity, ChatDataScience::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

            else {
                dataSciFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}