package com.intoverflown.sasakazi.ui.course_objective

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.data.Repository
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModel
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelFactory
import com.intoverflown.sasakazi.ui.discussions.ChatWeb
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class WebActivity : AppCompatActivity() {

    // UI elements
    private var webObjDescription : TextView? = null
    private var webInstructorName : TextView? = null
    private var webCertConditions : TextView? = null

    private var webResourceLinks : TextView? = null
    private var webResourceDescription : TextView? = null
    private var webResourceBooks : TextView? = null
    private var webResourceTranscripts : TextView? = null

    private var mobileAssignmentDescription : TextView? = null
    private var mobileAssignmentExercises : TextView? = null
    private var mobileAssignmentTasks : TextView? = null

    private lateinit var viewModel: ViewModel

    private var webVidPlayerView : YouTubePlayerView? = null
    private var webFloatActionBtn : FloatingActionButton? = null
    private var webFloatBtnView : View? = null
    private var webBaseLayout : ConstraintLayout? = null

    // Float btn Linear Layout elements
    private var webLinearLayoutObj : LinearLayout? = null
    private var webLinearLayoutRes : LinearLayout? = null
    private var webLinearLayoutAss : LinearLayout? = null
    private var webLinearLayoutDis : LinearLayout? = null

    // layouts View 'Gone'
    private var webResourceVisibility: View? = null
    private var webObjectiveVisibility: View? = null
    private var webAssignmentVisibility: View? = null

    private lateinit var fullUrl : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_web)

        webFloatActionBtn = findViewById<View>(R.id.floatingActionBtn) as FloatingActionButton
        webObjDescription = findViewById<View>(R.id.objectiveDescription) as TextView
        webInstructorName = findViewById<View>(R.id.instructorName) as TextView
        webCertConditions = findViewById<View>(R.id.conditionsList) as TextView
        webVidPlayerView = findViewById(R.id.youtubePlayerView)
        webFloatBtnView = findViewById(R.id.floatingBtnView)
        webBaseLayout = findViewById(R.id.rootLayout)

        // Float btn elements
        webLinearLayoutObj = findViewById(R.id.linearLayoutObj)
        webLinearLayoutRes = findViewById(R.id.linearLayoutRes)
        webLinearLayoutAss = findViewById(R.id.linearLayoutAss)
        webLinearLayoutDis = findViewById(R.id.linearLayoutDis)

        // Views - include
        webResourceVisibility = findViewById(R.id.resourcesVisibility)
        webObjectiveVisibility = findViewById(R.id.objVisibility)
        webAssignmentVisibility = findViewById(R.id.assignmentVisibility)

        // Resources
        webResourceLinks = findViewById<View>(R.id.resourceLinks) as TextView
        webResourceDescription = findViewById<View>(R.id.resourceDescription) as TextView
        webResourceBooks = findViewById<View>(R.id.resourceBooks) as TextView
        webResourceTranscripts = findViewById<View>(R.id.resourceTranscripts) as TextView

        // assignment
        mobileAssignmentDescription = findViewById<View>(R.id.assignmentDescription) as TextView
        mobileAssignmentExercises = findViewById<View>(R.id.assignmentExercises) as TextView
        mobileAssignmentTasks = findViewById<View>(R.id.assignmentTasks) as TextView

        // initialize ViewModels
        val repository = Repository()
        val viewModelFactory = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewModel::class.java)

        // set mutable LiveData
        setWebLiveDataHere()
    }

    private fun setWebLiveDataHere() {
        viewModel.fetchMobileData()
        viewModel.myMobileResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {

                // set to UI
                webObjDescription!!.text = response.body()?.course_objective
                webInstructorName!!.text = response.body()?.course_instructor
                webCertConditions!!.text = response.body()?.course_certificate_eligibility

                // get url and extract the link id as follows
                fullUrl = response.body()?.course_video_url!!
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

                // resources ui
                webResourceLinks!!.text = response.body()?.course_resources

                // assignment ui
            } else {
                Log.e("Response API Error: ", response.errorBody().toString())
            }
        })

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
                webLinearLayoutRes!!.setOnClickListener {
                    webResourceVisibility!!.visibility = View.VISIBLE
                    webAssignmentVisibility!!.visibility = View.GONE
                    webObjectiveVisibility!!.visibility = View.GONE
                    webFloatBtnView!!.visibility = View.GONE
                }

                // show assignments
                webLinearLayoutAss!!.setOnClickListener {
                    webAssignmentVisibility!!.visibility = View.VISIBLE
                    webObjectiveVisibility!!.visibility = View.GONE
                    webResourceVisibility!!.visibility = View.GONE
                    webFloatBtnView!!.visibility = View.GONE
                }


                // show course objective
                webLinearLayoutObj!!.setOnClickListener {
                    webObjectiveVisibility!!.visibility = View.VISIBLE
                    webAssignmentVisibility!!.visibility = View.GONE
                    webResourceVisibility!!.visibility = View.GONE
                    webFloatBtnView!!.visibility = View.GONE
                }

                // Intent to Discussion screen
                webLinearLayoutDis!!.setOnClickListener {
                    val intent = Intent(this@WebActivity, ChatWeb::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

            else {
                webFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}