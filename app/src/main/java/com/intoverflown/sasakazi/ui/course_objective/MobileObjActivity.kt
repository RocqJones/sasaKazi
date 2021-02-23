package com.intoverflown.sasakazi.ui.course_objective

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.ui.course_objective.models.MobileViewModel

class MobileObjActivity : AppCompatActivity() {

    // UI elements
    private var course_txtView : TextView? = null
    private var intro_video : VideoView? = null
    private var obj_description : TextView? = null
    private var instructor_name : TextView? = null
    private var cert_conditions : TextView? = null

    private lateinit var mobileViewModel : MobileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obj_mobile)
        mobileViewModel = ViewModelProvider(this).get(MobileViewModel::class.java)

        course_txtView = findViewById<View>(R.id.courseTxtView) as TextView
        intro_video = findViewById<View>(R.id.courseIntroVideoView) as VideoView
        obj_description = findViewById<View>(R.id.objectiveDescription) as TextView
        instructor_name = findViewById<View>(R.id.instructorName) as TextView
        cert_conditions = findViewById<View>(R.id.conditionsList) as TextView

        // set mutable LiveData
        setLiveDataHere()
    }

    private fun setLiveDataHere() {
        mobileViewModel.course.observe(this) {
            course_txtView!!.text = it
        }

        mobileViewModel.textCourseObj.observe(this) {
            obj_description!!.text = it
        }

        mobileViewModel.textInstructorName.observe(this) {
            instructor_name!!.text = it
        }
        mobileViewModel.textCertRequirements.observe(this) {
            cert_conditions!!.text = it
        }
    }
}