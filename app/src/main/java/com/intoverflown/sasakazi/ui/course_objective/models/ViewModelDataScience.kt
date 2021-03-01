package com.intoverflown.sasakazi.ui.course_objective.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelDataScience : ViewModel() {
    private val _youtubeLink = MutableLiveData<String>().apply {
        value = "https://youtu.be/X3paOmcrTjQ"
    }

    private val _courseObjective = MutableLiveData<String>().apply {
        value = "Data science is an inter-disciplinary field that uses scientific methods, processes, algorithms and systems to extract knowledge " +
                "and insights from many structural and unstructured data. Data science is related to data mining, machine learning and big data."
    }

    private val _instructorName = MutableLiveData<String>().apply {
        value = "John Doe"
    }

    private val _certRequirements = MutableLiveData<String>().apply {
        value = "Programming: Python, SQL, Scala, Java, R, MATLAB.\n" +
                "Machine Learning: Natural Language Processing, Classification, Clustering, ...\n" +
                "Data Visualization: Tableau, SAS, D3.js, Python, Java, R libraries.\n" +
                "Big data platforms: MongoDB, Oracle, Microsoft Azure, Cloudera."
    }

    val youtubeLink : LiveData<String> = _youtubeLink
    val textCourseObj : LiveData<String> = _courseObjective
    val textInstructorName : LiveData<String> = _instructorName
    val textCertRequirements : LiveData<String> = _certRequirements
}