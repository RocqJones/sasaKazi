package com.intoverflown.sasakazi.ui.course_objective.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelGame : ViewModel() {
    private val _youtubeLink = MutableLiveData<String>().apply {
        value = "https://youtu.be/7C92ZCnlmQo"
    }

    private val _courseObjective = MutableLiveData<String>().apply {
        value = "Game Development is the art of creating games and describes the design, development and release of a game. It may involve concept " +
                "generation, design, build, test and release. A game developer could be a programmer, a sound designer, an artist, a designer " +
                "or many other roles available in the industry."
    }

    private val _instructorName = MutableLiveData<String>().apply {
        value = "John Doe"
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