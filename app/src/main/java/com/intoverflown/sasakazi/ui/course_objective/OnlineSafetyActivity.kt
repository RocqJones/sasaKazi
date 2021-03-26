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
import com.intoverflown.sasakazi.ui.discussions.ChatOnlineSafety
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class OnlineSafetyActivity : AppCompatActivity() {

    // UI elements
    private var onlineSafetyObjDescription : TextView? = null
    private var onlineSafetyInstructorName : TextView? = null
    private var onlineSafetyCertConditions : TextView? = null

    private var onlineSafetyResourceLinks : TextView? = null
    private var onlineSafetyResourceDescription : TextView? = null
    private var onlineSafetyResourceBooks : TextView? = null
    private var onlineSafetyResourceTranscripts : TextView? = null

    private var onlineSafetyAssignmentDescription : TextView? = null
    private var onlineSafetyAssignmentExercises : TextView? = null
    private var onlineSafetyAssignmentTasks : TextView? = null

    private lateinit var viewModel: ViewModel

    private var onlineSafetyVidPlayerView : YouTubePlayerView? = null
    private var onlineSafetyFloatActionBtn : FloatingActionButton? = null
    private var onlineSafetyFloatBtnView : View? = null
    private var onlineSafetyBaseLayout : ConstraintLayout? = null

    // Float btn Linear Layout elements
    private var onlineSafetyLinearLayoutObj : LinearLayout? = null
    private var onlineSafetyLinearLayoutRes : LinearLayout? = null
    private var onlineSafetyLinearLayoutAss : LinearLayout? = null
    private var onlineSafetyLinearLayoutDis : LinearLayout? = null

    // layouts View 'Gone'
    private var onlineSafetyResourceVisibility: View? = null
    private var onlineSafetyObjectiveVisibility: View? = null
    private var onlineSafetyAssignmentVisibility: View? = null

    private lateinit var fullUrl : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_online_safety)

        onlineSafetyFloatActionBtn = findViewById<View>(R.id.floatingActionBtn) as FloatingActionButton
        onlineSafetyObjDescription = findViewById<View>(R.id.objectiveDescription) as TextView
        onlineSafetyInstructorName = findViewById<View>(R.id.instructorName) as TextView
        onlineSafetyCertConditions = findViewById<View>(R.id.conditionsList) as TextView
        onlineSafetyVidPlayerView = findViewById(R.id.youtubePlayerView)
        onlineSafetyFloatBtnView = findViewById(R.id.floatingBtnView)
        onlineSafetyBaseLayout = findViewById(R.id.rootLayout)

        // Float btn elements
        onlineSafetyLinearLayoutObj = findViewById(R.id.linearLayoutObj)
        onlineSafetyLinearLayoutRes = findViewById(R.id.linearLayoutRes)
        onlineSafetyLinearLayoutAss = findViewById(R.id.linearLayoutAss)
        onlineSafetyLinearLayoutDis = findViewById(R.id.linearLayoutDis)

        // Views - include
        onlineSafetyResourceVisibility = findViewById(R.id.resourcesVisibility)
        onlineSafetyObjectiveVisibility = findViewById(R.id.objVisibility)
        onlineSafetyAssignmentVisibility = findViewById(R.id.assignmentVisibility)

        // Resources
        onlineSafetyResourceLinks = findViewById<View>(R.id.resourceLinks) as TextView
        onlineSafetyResourceDescription = findViewById<View>(R.id.resourceDescription) as TextView
        onlineSafetyResourceBooks = findViewById<View>(R.id.resourceBooks) as TextView
        onlineSafetyResourceTranscripts = findViewById<View>(R.id.resourceTranscripts) as TextView

        // assignment
        onlineSafetyAssignmentDescription = findViewById<View>(R.id.assignmentDescription) as TextView
        onlineSafetyAssignmentExercises = findViewById<View>(R.id.assignmentExercises) as TextView
        onlineSafetyAssignmentTasks = findViewById<View>(R.id.assignmentTasks) as TextView

        // initialize ViewModels
        val repository = Repository()
        val viewModelFactory = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewModel::class.java)

        // set mutable LiveData
        setGameLiveDataHere()
    }

    private fun setGameLiveDataHere() {
        viewModel.fetchOnlineSafetyData()
        viewModel.myMobileResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {

                // set to UI
                onlineSafetyObjDescription!!.text = response.body()?.course_objective
                onlineSafetyInstructorName!!.text = response.body()?.course_instructor
                onlineSafetyCertConditions!!.text = response.body()?.course_certificate_eligibility

                // get url and extract the link id as follows
                fullUrl = response.body()?.course_video_url!!
                Log.i("YouTubeURL: ", fullUrl)

                // extract link id from url
                val extractedVidID : String? = fullUrl.substringAfterLast("youtu.be/")

                onlineSafetyVidPlayerView!!.let { lifecycle.addObserver(it) }

                onlineSafetyVidPlayerView!!.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
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
                onlineSafetyResourceLinks!!.text = response.body()?.course_resources

                // assignment ui
            } else {
                Log.e("Response API Error: ", response.errorBody().toString())
            }
        })

        onlineSafetyFloatActionBtn!!.setOnClickListener {
            if (onlineSafetyFloatBtnView!!.visibility == View.GONE) {
                onlineSafetyFloatBtnView!!.visibility = View.VISIBLE
                // when you touch outside the view
                onlineSafetyBaseLayout!!.setOnClickListener {
                    if (onlineSafetyFloatBtnView!!.visibility == View.VISIBLE) {
                        onlineSafetyFloatBtnView!!.visibility = View.GONE
                    }
                }

                // show resources
                onlineSafetyLinearLayoutRes!!.setOnClickListener {
                    onlineSafetyResourceVisibility!!.visibility = View.VISIBLE
                    onlineSafetyAssignmentVisibility!!.visibility = View.GONE
                    onlineSafetyObjectiveVisibility!!.visibility = View.GONE
                    onlineSafetyFloatBtnView!!.visibility = View.GONE
                }

                // show assignments
                onlineSafetyLinearLayoutAss!!.setOnClickListener {
                    onlineSafetyAssignmentVisibility!!.visibility = View.VISIBLE
                    onlineSafetyObjectiveVisibility!!.visibility = View.GONE
                    onlineSafetyResourceVisibility!!.visibility = View.GONE
                    onlineSafetyFloatBtnView!!.visibility = View.GONE
                }

                // show course objective
                onlineSafetyLinearLayoutObj!!.setOnClickListener {
                    onlineSafetyObjectiveVisibility!!.visibility = View.VISIBLE
                    onlineSafetyAssignmentVisibility!!.visibility = View.GONE
                    onlineSafetyResourceVisibility!!.visibility = View.GONE
                    onlineSafetyFloatBtnView!!.visibility = View.GONE
                }

                // Intent to Discussion screen
                onlineSafetyLinearLayoutDis!!.setOnClickListener {
                    val intent = Intent(this@OnlineSafetyActivity, ChatOnlineSafety::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

            else {
                onlineSafetyFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}