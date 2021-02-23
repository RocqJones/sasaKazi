package com.intoverflown.sasakazi.ui.course_objective

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CourseObjectiveViewModel : ViewModel() {
    private fun mobileData() {
        val _course = MutableLiveData<String>().apply {
            value = "Mobile App Development"
        }
        val _youtube_link = MutableLiveData<String>().apply {
            value = "https://youtu.be/WvwwL0TwH6U"
        }

        val _course_objective = MutableLiveData<String>().apply {
            value = "You will learn the the basis of the Android platform and the application lifecycle. " +
                    "You will be able to write simple GUI applications, use built-in widgets and components, " +
                    "work with the database to store data locally, and much more by the end of this Android training course."
        }

        val _instructor_name = MutableLiveData<String>().apply {
            value = "Jones Mbindyo"
        }

        val _cert_requirements = MutableLiveData<String>().apply {
            value = "Note: You must complete the Andela survey in your Pluralsight Skills, " +
                    "track-focused channel, in order to be eligible for Learning Phase I "
        }

        val course : LiveData<String> = _course
        val text_youtube : LiveData<String> = _youtube_link
        val text_course_obj : LiveData<String> = _course_objective
        val text_instructor_name : LiveData<String> = _instructor_name
        val text_cert_requirements : LiveData<String> = _cert_requirements
    }

}