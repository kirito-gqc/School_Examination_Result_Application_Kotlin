package com.example.examresultenteringsystem

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StudentListActivity : AppCompatActivity() {
    private lateinit var addsBtn: FloatingActionButton
    private lateinit var studentRecyclerView : RecyclerView
    private lateinit var studentList: ArrayList<Student>
    private lateinit var adapter: StudentAdapter
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    lateinit var toggle: ActionBarDrawerToggle

    //Layout
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)
        fireAuth = FirebaseAuth.getInstance()
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHead = navView.getHeaderView(0)
        val navUser = navHead.findViewById<TextView>(R.id.Username)
        val navEmail = navHead.findViewById<TextView>(R.id.Email)

        mDbRef = FirebaseDatabase.getInstance().getReference("Users")
        mDbRef.child(fireAuth.uid!!)
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
                    fireAuth.signOut()
                    finish()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }

            true
        }

        addsBtn = findViewById(R.id.ButtonAdd)

        mDbRef = FirebaseDatabase.getInstance().getReference()

        studentList = ArrayList()
        adapter = StudentAdapter(this, studentList)

        studentRecyclerView = findViewById(R.id.studentRecyclerView)

        mDbRef.child("Student").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                studentList.clear()
                for(readSnapshot in snapshot.children){
                    val studentData = readSnapshot.getValue(Student::class.java)

                    if(studentData?.status != "Removed") {

                        studentList.add(studentData!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        addsBtn.setOnClickListener{
            addInfo()
        }

        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        studentRecyclerView.adapter = adapter
    }

    private fun addInfo() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_student_item,null)

        val studentName = v.findViewById<EditText>(R.id.Text_student_name)
        val className = v.findViewById<EditText>(R.id.Text_class_name)
        val test1 = v.findViewById<EditText>(R.id.Text_test_1)
        val test2 = v.findViewById<EditText>(R.id.Text_test_2)

        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok") { dialog, _ ->
            val name = studentName.text.toString()
            val classname = className.text.toString()
            val test1 = test1.text.toString()
            val test2 = test2.text.toString()
            val status = "Available"
            mDbRef = FirebaseDatabase.getInstance().reference.child("Student").child(name)

            val studentHashMap = HashMap<String, Any>()
            studentHashMap["studentname"] = name
            studentHashMap["classname"] = classname
            studentHashMap["test1"] = test1
            studentHashMap["test2"] = test2
            studentHashMap["status"] = "Available"

            mDbRef.updateChildren(studentHashMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Student $name created", Toast.LENGTH_SHORT).show()

                    }

                }
            val newstudent = Student(name,classname,test1,test2,status)
            studentList.add(newstudent)
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
            dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"Student Adding Canceled", Toast.LENGTH_SHORT).show()
        }
        addDialog.create()
        addDialog.show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}



