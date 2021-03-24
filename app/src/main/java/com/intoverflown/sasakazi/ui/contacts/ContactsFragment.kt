package com.intoverflown.sasakazi.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.intoverflown.sasakazi.R

class ContactsFragment : Fragment() {

    private var mUserRef: DatabaseReference? = null
    private var contactProgressBar: ProgressBar? = null
    private var contactRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_contacts, container, false)

        mUserRef = FirebaseDatabase.getInstance().reference.child("Users")

        contactRecyclerView = root.findViewById(R.id.contactRecyclerView)
        contactRecyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        contactRecyclerView!!.layoutManager = layoutManager

        contactProgressBar = root.findViewById(R.id.contactProgressBar)

        displayUsers()

        return root
    }

    private fun displayUsers() {
        val options: FirebaseRecyclerOptions<Users> = FirebaseRecyclerOptions.Builder<Users>()
            .setQuery(mUserRef!!, Users::class.java)
            .build()
        val adapter: FirebaseRecyclerAdapter<Users, UsersViewAdapter> =
            object :
                FirebaseRecyclerAdapter<Users, UsersViewAdapter>(
                    options
                ) {
                override fun onBindViewHolder(
                    @NonNull holder: UsersViewAdapter,
                    position: Int,
                    @NonNull model: Users
                ) {
                    getRef(position).key
                    model.fullname
                    model.phone

                    holder.name.text = model.fullname
                    holder.phone.text = model.phone

//                    mUserRef!!.addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            holder.profilePic.let {
//                                Glide.with(this@ContactsFragment).load(snapshot.child("profile-url").value as String).circleCrop().into(
//                                    it
//                                )
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) { }
//                    })

                    // set photo
//                    holder.profilePic.let {
//                        Glide.with(this@ContactsFragment).load(model.profileurl).circleCrop().into(
//                            it
//                        )
//                    }
                }

                @NonNull
                override fun onCreateViewHolder(
                    @NonNull viewGroup: ViewGroup,
                    i: Int
                ): UsersViewAdapter {
                    val view: View = LayoutInflater.from(context)
                        .inflate(R.layout.content_contacts, viewGroup, false)
                    return UsersViewAdapter(
                        view
                    )
                }

                override fun onDataChanged() {
                    if (contactProgressBar != null) {
                        contactProgressBar!!.visibility = View.GONE
                    }
                }
            }
        contactRecyclerView!!.adapter = adapter
        adapter.startListening()
    }

    class UsersViewAdapter(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById<View>(R.id.contactFullName) as TextView
        var phone : TextView = itemView.findViewById<View>(R.id.contactPhone) as TextView
        var profilePic : ImageView = itemView.findViewById<View>(R.id.contactProfilePic) as ImageView
    }
}