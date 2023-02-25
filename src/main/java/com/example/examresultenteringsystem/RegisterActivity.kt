package com.example.examresultenteringsystem

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.examresultenteringsystem.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityRegisterBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseUserID: String =""
    private var name = ""
    private var ic = ""
    private var email = ""
    private var password = ""
    private var confirmpassword = ""

    private lateinit var refUsers: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure ActionBar //enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Register"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click, open login activity
        binding.TextViewExistingUser.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //handle click, begin startup
        binding.ButtonRegister.setOnClickListener{
            //validate data
            validateData()
        }
    }

    private fun validateData() {
        //get data
        name = binding.EditTextName.text.toString().trim()
        ic = binding.EditTextIC.text.toString().trim()
        email = binding.EditTextEmail.text.toString().trim()
        password = binding.EditTextPassword.text.toString().trim()
        confirmpassword = binding.EditTextConfirmPassword.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.EditTextEmail.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(name)){
            //name isn't entered
            binding.EditTextName.error = "Please enter your name"
        }
        else if (TextUtils.isEmpty(ic)){
            //ic isn't entered
            binding.EditTextIC.error = "Please enter your IC number"
        }
        else if (ic.length != 12){
            //ic length do not have 12 number
            binding.EditTextIC.error = "IC Number must be 12 digits"
        }
        else if (TextUtils.isEmpty(email)){
            //ic isn't entered
            binding.EditTextEmail.error = "Please enter your email"
        }
        else if (TextUtils.isEmpty(password)){
            //password isn't entered
            binding.EditTextPassword.error = "Please enter the password"
        }
        else if (password.length <8){
            //password length is less than 8
            binding.EditTextPassword.error = "Password must at least 8 character long"
        }
        else if (TextUtils.isEmpty(confirmpassword)){
            //CONFIRM password isn't entered
            binding.EditTextConfirmPassword.error = "Please enter the confirm password"
        }
        else if (confirmpassword != password){
            //confirm password is not equal to password
            binding.EditTextConfirmPassword.error = "Unmatched password is entered"
        }
        else{
            //data is valid, continue register
            firebaseRegister()
        }
    }

    private fun firebaseRegister() {
        //show progress
        progressDialog.show()

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //register sucessful
                progressDialog.dismiss()
                //get current user
                firebaseUserID = firebaseAuth.currentUser!!.uid
                refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                val userHashMap = HashMap<String, Any>()
                userHashMap["uid"] = firebaseUserID
                userHashMap["username"] = name
                userHashMap["ic"] = ic
                userHashMap["email"] = email
                userHashMap["position"] = "teacher"

                refUsers.updateChildren(userHashMap)
                    .addOnCompleteListener { task->
                        if (task.isSuccessful)
                        {
                            Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
            }
            .addOnFailureListener{ e->
                //register failed
                progressDialog.dismiss()
                Toast.makeText(this,"Register failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean
    {
        onBackPressed() //go back to previous activity
        return super.onSupportNavigateUp()
    }
}