package com.example.news

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.news.databinding.ActivityLoginBinding
import com.example.news.firebase.FirestoresClass
import com.example.news.model.User
import com.example.news.utils.Constant
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {
    private var binding : ActivityLoginBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.tvregisterLogin?.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        binding?.buttonLogin?.setOnClickListener{
            loginUsers()
        }
        binding?.tvForgotpassword?.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }

    }
    fun validateForm(): Boolean{
        return when{
            TextUtils.isEmpty(binding?.editTextEmail?.text.toString())->{
                showErrorSnackBar("Please enter email.", true)
                false
            }
            TextUtils.isEmpty(binding?.editTextPassword?.text.toString())->{
                showErrorSnackBar("Please enter password.",true)
                false
            }
            else->{
                true
            }
        }
    }
    fun loginUsers()
    {
        val email = binding?.editTextEmail?.text.toString()
        val password = binding?.editTextPassword?.text.toString()
       if (validateForm()){
            showProgressDialog()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e(ContentValues.TAG, "signInWithEmail:success")
                        FirestoresClass().getUserDetail(this)

                    } else {
                        hideProgressDialog()
                        showErrorSnackBar("login fail", true)

                    }
                }
        }
    }

//    fun userLoggedInSuccess(user: User) {
//        hideProgressDialog()
//        startActivity(Intent(this,MainActivity::class.java))
//        finish()
//    }
    fun userLoggedInSuccess(user: User) {
         hideProgressDialog()
        if (user.profileComplete==0){
            val intent = (Intent(this, ProfileActivity::class.java))
            intent.putExtra(Constant.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

}