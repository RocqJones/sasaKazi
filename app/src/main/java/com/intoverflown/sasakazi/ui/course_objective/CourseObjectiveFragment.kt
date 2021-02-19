package com.intoverflown.sasakazi.ui.course_objective

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intoverflown.sasakazi.R

class CourseObjectiveFragment : Fragment() {

    companion object {
        fun newInstance() = CourseObjectiveFragment()
    }

    private lateinit var viewModel: CourseObjectiveViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_course_objective, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CourseObjectiveViewModel::class.java)
        // TODO: Use the ViewModel
    }

}