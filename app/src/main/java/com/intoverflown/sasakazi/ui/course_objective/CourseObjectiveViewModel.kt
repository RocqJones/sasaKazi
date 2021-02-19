package com.intoverflown.sasakazi.ui.course_objective

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CourseObjectiveViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "You will learn the the basis of the Android platform and the application lifecycle. " +
                "You will be able to write simple GUI applications, use built-in widgets and components, " +
                "work with the database to store data locally, and much more by the end of this Android training course."
    }
    val text: LiveData<String> = _text
}