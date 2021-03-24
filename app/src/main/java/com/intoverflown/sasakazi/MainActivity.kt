package com.intoverflown.sasakazi

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.intoverflown.sasakazi.ui.navdrawer.ProfileActivity
import com.intoverflown.sasakazi.users.LoginActivity


class MainActivity : AppCompatActivity() {

    // Firebase
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null

    // Nav UI elements
    private var navFullName: TextView? = null
    private var navEmailAddress: TextView? = null
    private var navProfileImage: ImageView? = null
    private var linearLayoutAction : LinearLayout? = null

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)

        // toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Bottom NavBar
        val navBottomView: BottomNavigationView = findViewById(R.id.nav_bottom_view)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        // val navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_contacts, R.id.navigation_blog), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
        navBottomView.setupWithNavController(navController)

        // initialize references
        initializeREF()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Nav UI and Firebase refs
    private fun initializeREF() {
        // get the navigation view here and then get it's nav_header_view otherwise there will be a NullPointerException error
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navView.getHeaderView(0)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        navFullName = headerView.findViewById(R.id.nav_fullName)
        navEmailAddress = headerView.findViewById(R.id.nav_emailAddress)
        navProfileImage = headerView.findViewById(R.id.nav_drawerCircleImg)
        linearLayoutAction = headerView.findViewById(R.id.nav_header_linearLayout)

        // when clicked it intents to profile
        linearLayoutAction!!.setOnClickListener { intentToProfileAction() }

        showNavDrawerUserDet()
    }

    // show user profile pic, full-name and email in the nav header
    private fun showNavDrawerUserDet() {
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        navEmailAddress?.text = mUser.email

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Picasso.get().load(snapshot.child("profile-url").value as String).into(navProfileImage)
                navProfileImage?.let {
                    Glide.with(this@MainActivity).load(snapshot.child("profile-url").value as String).circleCrop().into(
                        it
                    )
                }
                navFullName?.text = snapshot.child("fullname").value as String
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // logOut
    fun logOutCurrentUser(item: MenuItem) {
        mAuth = FirebaseAuth.getInstance()
        mAuth!!.signOut()

        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    fun intentToProfile(item: MenuItem) {
        intentToProfileAction()
    }

    // intent to Profile
    private fun intentToProfileAction() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}