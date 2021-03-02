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
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelGame
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class GameActivity : AppCompatActivity() {

    // UI elements
    private var gameObjDescription : TextView? = null
    private var gameInstructorName : TextView? = null
    private var gameCertConditions : TextView? = null
    private var gameVidPlayerView : YouTubePlayerView? = null
    private var gameFloatActionBtn : FloatingActionButton? = null
    private var gameFloatBtnView : View? = null
    private var gameBaseLayout : ConstraintLayout? = null

    // Float btn elements
    private var gameFloatTxtRes : TextView? = null
    private var gameFloatTxtObj : TextView? = null
    private var gameFloatTxtDis : TextView? = null
    private var gameFloatTxtAss : TextView? = null
    private var gameFloatResourceBtn : FloatingActionButton? = null
    private var gameFloatObjectiveBtn : FloatingActionButton? = null
    private var gameFloatAssignmentBtn : FloatingActionButton? = null
    private var gameFloatDiscussionBtn : FloatingActionButton? = null

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
        gameFloatTxtObj = findViewById<View>(R.id.textViewObj) as TextView
        gameFloatTxtRes = findViewById<View>(R.id.textViewResources) as TextView
        gameFloatTxtDis = findViewById<View>(R.id.textViewDiscussion) as TextView
        gameFloatTxtAss = findViewById<View>(R.id.textViewAssignment) as TextView
        gameFloatResourceBtn = findViewById<View>(R.id.resources) as FloatingActionButton
        gameFloatObjectiveBtn = findViewById<View>(R.id.objective) as FloatingActionButton
        gameFloatAssignmentBtn = findViewById<View>(R.id.assignment) as FloatingActionButton
        gameFloatDiscussionBtn = findViewById<View>(R.id.discussion) as FloatingActionButton

        // Views - include
        gameResourceVisibility = findViewById(R.id.resourcesVisibility)
        gameObjectiveVisibility = findViewById(R.id.objVisibility)
        gameAssignmentVisibility = findViewById(R.id.assignmentVisibility)

        // set mutable LiveData
        setGameLiveDataHere()
    }

    private fun setGameLiveDataHere() {
        viewModelGame.textCourseObj.observe(this) {
            gameObjDescription!!.text = it
        }

        viewModelGame.textInstructorName.observe(this) {
            gameInstructorName!!.text = it
        }
        viewModelGame.textCertRequirements.observe(this) {
            gameCertConditions!!.text = it
        }

        // get url and extract the link id in the subsequent function
        viewModelGame.youtubeLink.observe(this) { it ->
            fullUrl = it

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
        }

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
                gameFloatResourceBtn!!.setOnClickListener {
                    gameResourceVisibility!!.visibility = View.VISIBLE
                    gameAssignmentVisibility!!.visibility = View.GONE
                    gameObjectiveVisibility!!.visibility = View.GONE
                    gameFloatBtnView!!.visibility = View.GONE
                }

                gameFloatTxtRes!!.setOnClickListener {
                    gameResourceVisibility!!.visibility = View.VISIBLE
                    gameAssignmentVisibility!!.visibility = View.GONE
                    gameObjectiveVisibility!!.visibility = View.GONE
                    gameFloatBtnView!!.visibility = View.GONE
                }

                // show assignments
                gameFloatAssignmentBtn!!.setOnClickListener {
                    gameAssignmentVisibility!!.visibility = View.VISIBLE
                    gameObjectiveVisibility!!.visibility = View.GONE
                    gameResourceVisibility!!.visibility = View.GONE
                    gameFloatBtnView!!.visibility = View.GONE
                }

                gameFloatTxtAss!!.setOnClickListener {
                    gameAssignmentVisibility!!.visibility = View.VISIBLE
                    gameObjectiveVisibility!!.visibility = View.GONE
                    gameResourceVisibility!!.visibility = View.GONE
                    gameFloatBtnView!!.visibility = View.GONE
                }

                // show course objective
                gameFloatObjectiveBtn!!.setOnClickListener {
                    gameObjectiveVisibility!!.visibility = View.VISIBLE
                    gameAssignmentVisibility!!.visibility = View.GONE
                    gameResourceVisibility!!.visibility = View.GONE
                    gameFloatBtnView!!.visibility = View.GONE
                }

                gameFloatTxtObj!!.setOnClickListener {
                    gameObjectiveVisibility!!.visibility = View.VISIBLE
                    gameAssignmentVisibility!!.visibility = View.GONE
                    gameResourceVisibility!!.visibility = View.GONE
                    gameFloatBtnView!!.visibility = View.GONE
                }

                // Intent to Discussion screen
//                gameFloatDiscussionBtn!!.setOnClickListener { }
//                gameFloatTxtDis!!.setOnClickListener {  }
            }

            else {
                gameFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}