package com.example.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.news.databinding.ActivityRegisterBinding
import com.example.news.firebase.FirestoresClass
import com.example.news.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity :BaseActivity() {
    private var binding: ActivityRegisterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.tvloginRegister?.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding?.buttonRegister?.setOnClickListener{
            registerUser()
        }
    }

    private fun validateForm(): Boolean {
        return when {
            TextUtils.isEmpty(binding?.edUsernameRegister?.text.toString()) -> {
                showErrorSnackBar("Please enter user name.", true)
                false
            }
            TextUtils.isEmpty(binding?.edEmailRegister?.text.toString()) -> {
                showErrorSnackBar("Please enter email.", true)
                false
            }
            TextUtils.isEmpty(binding?.edPassRegister?.text.toString()) -> {
                showErrorSnackBar("Please enter password.", true)
                false
            }
            TextUtils.isEmpty(binding?.edConfirmPassword?.text.toString()) -> {
                showErrorSnackBar("Please enter confirm password.", true)
                false
            }
            else -> {
                true
            }

        }
    }
    private fun registerUser(){
        val email = binding?.edEmailRegister?.text.toString()
        val password = binding?.edPassRegister?.text.toString()
        if (validateForm()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result!!.user!!
                        val user = User(
                            firebaseUser.uid,
                            binding?.edUsernameRegister?.text.toString().trim(),
                            binding?.edEmailRegister?.text.toString().trim()
                        )
                        FirestoresClass().registerUser(this, user)
//                        FirebaseAuth.getInstance().signOut()
//                        finish()
                    } else {
                       // hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    fun userRegisterSuccess(){
        Toast.makeText(this, "You are registered successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,LoginActivity::class.java))
    }
}