package com.intoverflown.sasakazi.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.ui.course_objective.CourseObjectiveActivity

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    // UI
    private var mobile_card: CardView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // UI references
        uiRefs()

        return root
    }

    private fun uiRefs() {
        mobile_card?.findViewById<View>(R.id.home_mobileAppCard)
        mobile_card?.setOnClickListener { intentToObjScreen() }
    }

    private fun intentToObjScreen() {
        val intent = Intent(activity, CourseObjectiveActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

//        val b = Intent(activity, SellingSubmitDataActivity::class.java)
//        startActivity(b)
    }
}
