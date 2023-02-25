package com.example.examresultenteringsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.examresultenteringsystem.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityForgetPasswordBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()



        binding.ButtonSubmit.setOnClickListener{
            validatedata()
        }

        //handle click, open register activity
        binding.TextViewNewUser.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validatedata() {
        email = binding.EditTextEmail.text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.EditTextEmail.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(email)){
            //no password entered
            binding.EditTextEmail.error = "Please enter the email"
        }
        else{
            //data is validated, begin login
            firebaseForgetPassword()
        }
    }

    private fun firebaseForgetPassword() {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Toast.makeText(this@ForgetPasswordActivity,
                    "Email sent successfully to reset your password",
                    Toast.LENGTH_LONG).show()

                    finish()
                }
                else{
                    Toast.makeText(this@ForgetPasswordActivity,
                    task.exception!!.message.toString(),
                    Toast.LENGTH_LONG).show()
                }

            }
    }
}