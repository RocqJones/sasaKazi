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
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelDataScience
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelDigitalMrkt
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
            }

            else {
                dataSciFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}