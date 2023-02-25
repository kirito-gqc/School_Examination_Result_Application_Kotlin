package com.example.examresultenteringsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    //Toggle
    lateinit var toggle: ActionBarDrawerToggle

    //Layout
    lateinit var drawerLayout: DrawerLayout

    private lateinit var mDbRef: DatabaseReference


        lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHead = navView.getHeaderView(0)
        val navUser = navHead.findViewById<TextView>(R.id.Username)
        val navEmail = navHead.findViewById<TextView>(R.id.Email)

        mDbRef = FirebaseDatabase.getInstance().getReference("Users")
        mDbRef.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("username").value}"

                    navUser.text = name
                    navEmail.text = email

                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            it.isChecked = true

            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                R.id.nav_enterresult -> {
                    startActivity(Intent(this, StudentListActivity::class.java))
                }
                R.id.nav_chatroom -> {
                    startActivity(Intent(this, ChatListActivity::class.java))
                }
                R.id.nav_support -> {
                    startActivity(Intent(this, CustomerSupportActivity::class.java))
                }
                R.id.nav_logout -> {
                    firebaseAuth.signOut()
                    finish()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            true
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}





