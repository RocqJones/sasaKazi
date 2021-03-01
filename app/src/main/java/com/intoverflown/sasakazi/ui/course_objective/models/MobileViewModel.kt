package com.intoverflown.sasakazi.ui.course_objective.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MobileViewModel : ViewModel() {
    private val _youtubeLink = MutableLiveData<String>().apply {
        value = "https://youtu.be/snqMchRhQnk"
    }

    private val _courseObjective = MutableLiveData<String>().apply {
        value = "You will learn the the basis of the Android platform and the application lifecycle. " +
                "You will be able to write simple GUI applications, use built-in widgets and components, " +
                "work with the database to store data locally, and much more by the end of this Android training course."
    }

    private val _instructorName = MutableLiveData<String>().apply {
        value = "Jones Mbindyo"
    }

    private val _certRequirements = MutableLiveData<String>().apply {
        value = "Note: You must complete the Andela survey in your Pluralsight Skills, " +
                "track-focused channel, in order to be eligible for Learning Phase I "
    }

    val youtubeLink : LiveData<String> = _youtubeLink
    val textCourseObj : LiveData<String> = _courseObjective
    val textInstructorName : LiveData<String> = _instructorName
    val textCertRequirements : LiveData<String> = _certRequirements
}