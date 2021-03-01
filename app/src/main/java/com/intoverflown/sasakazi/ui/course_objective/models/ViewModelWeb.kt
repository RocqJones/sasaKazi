package com.intoverflown.sasakazi.ui.course_objective.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelWeb : ViewModel() {
    private val _youtubeLink = MutableLiveData<String>().apply {
        value = "https://youtu.be/3JluqTojuME"
    }

    private val _courseObjective = MutableLiveData<String>().apply {
        value = "Web development is the work involved in developing a Web site for the Internet (World Wide Web) or an intranet (a private " +
                "network). Among Web professionals, \"Web development\" usually refers to the main non-design aspects of building Web sites: " +
                "writing markup and coding."
    }

    private val _instructorName = MutableLiveData<String>().apply {
        value = "Azron Brian"
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