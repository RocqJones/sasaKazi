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
import com.intoverflown.sasakazi.ui.course_objective.models.ViewModelWeb
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
            }

            else {
                gameFloatBtnView!!.visibility = View.GONE
            }
        }
    }
}