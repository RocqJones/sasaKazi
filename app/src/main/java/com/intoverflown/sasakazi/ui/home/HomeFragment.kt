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
import com.intoverflown.sasakazi.ui.course_objective.DigitalMarketingActivity
import com.intoverflown.sasakazi.ui.course_objective.GameObjActivity
import com.intoverflown.sasakazi.ui.course_objective.MobileObjActivity
import com.intoverflown.sasakazi.ui.course_objective.WebObjActivity

class HomeFragment : Fragment() {

    private var TAG = "HomeFragment: "

    private lateinit var homeViewModel: HomeViewModel

    // UI
    private var mobileCard: CardView? = null
    private var webCard: CardView? = null
    private var gameCard: CardView? = null
    private var digitalMrktCard : CardView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // UI references
        mobileCard = root.findViewById(R.id.home_mobileAppCard)
        webCard = root.findViewById(R.id.home_webAppCard)
        gameCard = root.findViewById(R.id.home_gameDevCard)
        digitalMrktCard = root.findViewById(R.id.home_digitalMarketingCard)

        // intent to base course screens
        uiScreenTransactions()

        return root
    }

    private fun uiScreenTransactions() {
        mobileCard!!.setOnClickListener {
            Log.d(TAG, "Mobile Card onClick: Success")
            val mobileIntent = Intent(this@HomeFragment.context, MobileObjActivity::class.java)
            mobileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(mobileIntent)
        }

        webCard!!.setOnClickListener {
            Log.d(TAG, "Web Card onClick: Success")
            val webIntent = Intent(this@HomeFragment.context, WebObjActivity::class.java)
            webIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(webIntent)
        }

        gameCard!!.setOnClickListener {
            Log.d(TAG, "Game Card onClick: Success")
            val gameIntent = Intent(this@HomeFragment.context, GameObjActivity::class.java)
            gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(gameIntent)
        }

        digitalMrktCard!!.setOnClickListener {
            Log.d(TAG, "Digital Market Card onClick: Success")
            val digitalIntent = Intent(this@HomeFragment.context, DigitalMarketingActivity::class.java)
            digitalIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(digitalIntent)
        }
    }
}
