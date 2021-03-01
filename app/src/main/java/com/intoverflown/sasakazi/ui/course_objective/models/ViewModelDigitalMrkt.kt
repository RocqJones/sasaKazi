package com.intoverflown.sasakazi.ui.course_objective.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelDigitalMrkt : ViewModel() {
    private val _youtubeLink = MutableLiveData<String>().apply {
        value = "https://youtu.be/ZVuHLPl69mM"
    }

    private val _courseObjective = MutableLiveData<String>().apply {
        value = "Digital marketing, also called online marketing, is the promotion of brands to connect with potential customers using the internet " +
                "and other forms of digital communication. This includes not only email, social media, and web-based advertising, but also text and " +
                "multimedia messages as a marketing channel."
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