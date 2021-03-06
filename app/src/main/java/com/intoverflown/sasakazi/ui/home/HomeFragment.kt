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
import com.intoverflown.sasakazi.ui.course_objective.*

class HomeFragment : Fragment() {

    private var TAG = "HomeFragment: "

    private lateinit var homeViewModel: HomeViewModel

    // UI
    private var mobileCard: CardView? = null
    private var webCard: CardView? = null
    private var gameCard: CardView? = null
    private var digitalMrktCard : CardView? = null
    private var dataScience: CardView? = null
    private var onlineSafety: CardView? = null

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
        dataScience = root.findViewById(R.id.home_dataScienceCard)
        onlineSafety = root.findViewById(R.id.home_onlineSafetyCard)

        // intent to base course screens
        uiScreenTransactions()

        return root
    }

    private fun uiScreenTransactions() {
        mobileCard!!.setOnClickListener {
            Log.d(TAG, "Mobile Card onClick: Success")
            val mobileIntent = Intent(this@HomeFragment.context, MobileActivity::class.java)
            mobileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(mobileIntent)
        }

        webCard!!.setOnClickListener {
            Log.d(TAG, "Web Card onClick: Success")
            val webIntent = Intent(this@HomeFragment.context, WebActivity::class.java)
            webIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(webIntent)
        }

        gameCard!!.setOnClickListener {
            Log.d(TAG, "Game Card onClick: Success")
            val gameIntent = Intent(this@HomeFragment.context, GameActivity::class.java)
            gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(gameIntent)
        }

        digitalMrktCard!!.setOnClickListener {
            Log.d(TAG, "Digital Market Card onClick: Success")
            val digitalIntent = Intent(this@HomeFragment.context, DigitalMarketingActivity::class.java)
            digitalIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(digitalIntent)
        }

        dataScience!!.setOnClickListener {
            Log.d(TAG, "Digital Market Card onClick: Success")
            val dataScIntent = Intent(this@HomeFragment.context, DataScienceActivity::class.java)
            dataScIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(dataScIntent)
        }

        onlineSafety!!.setOnClickListener {
            Log.d(TAG, "Online Safety Card onClick: Success")
            val onlineSafetyIntent = Intent(this@HomeFragment.context, OnlineSafetyActivity::class.java)
            onlineSafetyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(onlineSafetyIntent)
        }
    }
}
