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
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelGame
import com.intoverflown.sasakazi.ui.discussions.ChatGame
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class GameActivity : AppCompatActivity() {

    // UI elements
    private var gameObjDescription : TextView? = null
    private var gameInstructorName : TextView? = null
    private var gameCertConditions : TextView? = null

    private var gameResourceLinks : TextView? = null
    private var gameResourceDescription : TextView? = null
    private var gameResourceBooks : TextView? = null
    private var gameResourceTranscripts : TextView? = null

    private var gameAssignmentDescription : TextView? = null
    private var gameAssignmentExercises : TextView? = null
    private var gameAssignmentTasks : TextView? = null

    private lateinit var viewModel: ViewModel

    private var gameVidPlayerView : YouTubePlayerView? = null
    private var gameFloatActionBtn : FloatingActionButton? = null
    private var gameFloatBtnView : View? = null
    private var gameBaseLayout : ConstraintLayout? = null

    // Float btn Linear Layout elements
    private var gameLinearLayoutObj : LinearLayout? = null
    private var gameLinearLayoutRes : LinearLayout? = null
    private var gameLinearLayoutAss : LinearLayout? = null
    private var gameLinearLayoutDis : LinearLayout? = null

    // layouts View 'Gone'
    private var gameResourceVisibility: View? = null
    private var gameObjectiveVisibility: View? = null
    private var gameAssignmentVisibility: View? = null

    private lateinit var fullUrl : String
    private lateinit var viewModelGame : ViewModelGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_game)

        viewModelGame = ViewModelProvider(this).get(ViewModelGame::class.java)

        gameFloatActionBtn = findViewById<View>(R.id.floatingActionBtn) as FloatingActionButton
        gameObjDescription = findViewById<View>(R.id.objectiveDescription) as TextView
        gameInstructorName = findViewById<View>(R.id.instructorName) as TextView
        gameCertConditions = findViewById<View>(R.id.conditionsList) as TextView
        gameVidPlayerView = findViewById(R.id.youtubePlayerView)
        gameFloatBtnView = findViewById(R.id.floatingBtnView)
        gameBaseLayout = findViewById(R.id.rootLayout)

        // Float btn elements
        gameLinearLayoutObj = findViewById(R.id.linearLayoutObj)
        gameLinearLayoutRes = findViewById(R.id.linearLayoutRes)
        gameLinearLayoutAss = findViewById(R.id.linearLayoutAss)
        gameLinearLayoutDis = findViewById(R.id.linearLayoutDis)

        // Views - include
        gameResourceVisibility = findViewById(R.id.resourcesVisibility)
        gameObjectiveVisibility = findViewById(R.id.objVisibility)
        gameAssignmentVisibility = findViewById(R.id.assignmentVisibility)

        // Resources
        gameResourceLinks = findViewById<View>(R.id.resourceLinks) as TextView
        gameResourceDescription = findViewById<View>(R.id.resourceDescription) as TextView
        gameResourceBooks = findViewById<View>(R.id.resourceBooks) as TextView
        gameResourceTranscripts = findViewById<View>(R.id.resourceTranscripts) as TextView

        // assignment
        gameAssignmentDescription = findViewById<View>(R.id.assignmentDescription) as TextView
        gameAssignmentExercises = findViewById<View>(R.id.assignmentExercises) as TextView
        gameAssignmentTasks = findViewById<View>(R.id.assignmentTasks) as TextView

        // initialize ViewModels
        val repository = Repository()
        val viewModelFactory = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewModel::class.java)

        // set mutable LiveData
        setGameLiveDataHere()
    }

    private fun setGameLiveDataHere() {
        viewModel.fetchGameData()
        viewModel.myMobileResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {

                // set to UI
                gameObjDescription!!.text = response.body()?.course_objective
                gameInstructorName!!.text = response.body()?.course_instructor
                gameCertConditions!!.text = response.body()?.course_certificate_eligibility

                // get url and extract the link id as follows
                fullUrl = response.body()?.course_video_url!!
                Log.i("YouTubeURL: ", fullUrl)

                // extract link id from url
                val extractedVidID : String? = fullUrl.substringAfterLast("youtu.be/")

                gameVidPlayerView!!.let { lifecycle.addObserver(it) }

                gameVidPlayerView!!.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
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
                gameResourceLinks!!.text = response.body()?.course_resources

                // assignment ui
            } else {
                Log.e("Response API Error: ", response.errorBody().toString())
            }
        })

        gameFloatActionBtn!!.setOnClickListener {
            if (gameFloatBtnView!!.visibility == View.GONE) {
                gameFloatBtnView!!.visibility = View.VISIBLE
                // when you touch outside the view
                gameBaseLayout!!.setOnClickListener {
                    if (gameFloatBtnView!!.visibility == View.VISIBLE) {
                        gameFloatBtnView!!.visibility = View.GONE
                    }
                }

                // show resources
                gameLinearLayoutRes!!.setOnClickListener {
                    gameResourceVisibility!!.visibility = View.VISIBLE
                    gameAssignmentVisibility!!.visibility = View.GONE
                    gameObjectiveVisibility!!.visibility = View.GONE
                    gameFloatBtnView!!.visibility = View.GONE
                }

                // show assignments
                gameLinearLayoutAss!!.setOnClickListener {
                    gameAssignmentVisibility!!.visibility = View.VISIBLE
                    gameObjectiveVisibility!!.visibility = View.GONE
                    gameResourceVisibility!!.visibility = View.GONE
                    gameFloatBtnView!!.visibility = View.GONE
                }

                // show course objective
                gameLinearLayoutObj!!.setOnClickListener {
                    gameObjectiveVisibility!!.visibility = View.VISIBLE
                    gameAssignmentVisibility!!.visibility = View.GONE
                    gameResourceVisibility!!.visibility = View.GONE
                    gameFloatBtnView!!.visibility = View.GONE
                }

                // Intent to Discussion screen
                gameLinearLayoutDis!!.setOnClickListener {
                    val intent = Intent(this@GameActivity, ChatGame::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

            else {
                gameFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}