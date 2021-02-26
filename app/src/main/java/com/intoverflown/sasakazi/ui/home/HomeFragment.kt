package com.intoverflown.sasakazi.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.ui.course_objective.MobileObjActivity

class HomeFragment : Fragment() {

    private var TAG = "HomeFragment"

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

        mobile_card = root.findViewById(R.id.home_mobileAppCard)

        // UI references
        uiRefs()

        return root
    }

    private fun uiRefs() {
        mobile_card!!.setOnClickListener {
            Log.d(TAG, "Mobile Card onClick: Success")
            mobileObjScreen()
        }
    }

    private fun mobileObjScreen() {
        val intent = Intent(this@HomeFragment.context, MobileObjActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

        // set content for Mobile here
    }
}
