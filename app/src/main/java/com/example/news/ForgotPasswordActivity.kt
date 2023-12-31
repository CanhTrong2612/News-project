package com.example.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.news.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    private var binding:ActivityForgotPasswordBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        binding?.btsubmit?.setOnClickListener {
            val email = binding?.etmailForgotpassword?.text.toString()
            if (email.isEmpty()){
                showErrorSnackBar("please enter e-mail!!",true)
            }else{
                showProgressDialog()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideProgressDialog()
                        if (task.isSuccessful){
                            Toast.makeText(this,R.string.email_sent_successful, Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        else{
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
            }
        }
    }
    fun actionBar(){
        setSupportActionBar(binding?.toolbarForgotpassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back)
        supportActionBar?.title= "Forgot password"
        binding?.toolbarForgotpassword?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        binding?.toolbarForgotpassword?.setNavigationOnClickListener { onBackPressed() }

    }
}