package com.example.examresultenteringsystem

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.examresultenteringsystem.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding:ActivityLoginBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog:ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onStart() {
        super.onStart()
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
//        checkUser()

        //handle click, open register activity
        binding.TextViewNewUser.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //handle click, open forget password
        binding.TextViewForgetPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }


        //handle click, begin login
        binding.ButtonLogin.setOnClickListener{
            //before logging in, validate data
            validateData()

        }
    }

    private fun validateData() {
        //get data
        email = binding.EditTextEmail.text.toString().trim()
        password = binding.EditTextPassword.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.EditTextEmail.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(email)){
            //no password entered
            binding.EditTextEmail.error = "Please enter the email"
        }
        else if (TextUtils.isEmpty(password)){
            //no password entered
            binding.EditTextPassword.error = "Please enter the password"
        }
        else{
            //data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        //show progree
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Logged In as $email", Toast.LENGTH_SHORT).show()

                //open main activity
                startActivity(Intent(this,MainActivity::class.java))
                finish()

            }
            .addOnFailureListener{ e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

//    private fun checkUser() {
//        //if user is already logged in go to main activity
//        //get current user
//        val firebaseUser = firebaseAuth.currentUser
//        if (firebaseUser != null){
//            //user is logged in
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//    }
}