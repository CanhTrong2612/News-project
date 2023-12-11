package com.example.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.news.databinding.ActivitySettingBinding
import com.example.news.firebase.FirestoresClass
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private var biding:ActivitySettingBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(biding?.root)
        loadDataSuccess()

        biding?.btnHistorySetting?.setOnClickListener {
            startActivity(Intent(this,ViewHistoryActivity::class.java))
        }
        biding?.btnLogout?.setOnClickListener {
            logout()
        }
        biding?.btnReadLater?.setOnClickListener {
            startActivity(Intent(this,SaveActivity::class.java))
        }


    }
    fun loadData(user: com.example.news.model.User)
    {
        if (user!=null){
            biding?.tvName?.setText(user.username)
            biding?.tvEmail?.setText(user.email)
            Glide.with(this).load(user.image).into(biding!!.profileImage)
        }

    }
    fun loadDataSuccess()
    {
        FirestoresClass().getUserDetail(this)
    }
    fun logout(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

}