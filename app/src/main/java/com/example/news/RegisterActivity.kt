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
        if (validateForm()) {
            showProgressDialog()
            val email = binding?.edEmailRegister?.text.toString()
            val password = binding?.edPassRegister?.text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val user = User(
                            firebaseUser.uid,
                            binding?.edUsernameRegister?.text.toString(),
                            binding?.edEmailRegister?.text.toString()

                        )
                        FirestoresClass().registerUser(this, user)
//                        FirebaseAuth.getInstance().signOut()
//                        finish()
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    fun userRegisterSuccess(){
        hideProgressDialog()
        Toast.makeText(this, "You are registered successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,LoginActivity::class.java))

    }
}