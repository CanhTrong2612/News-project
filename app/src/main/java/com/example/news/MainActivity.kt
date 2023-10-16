package com.example.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.news.databinding.ActivityMainBinding
import com.example.news.databinding.ActivityRegisterBinding
import com.example.news.firebase.FirestoresClass
import com.example.news.model.User
import com.example.news.utils.Constant
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {
    private var binding :ActivityMainBinding? = null
    private var toolbar : Toolbar?= null
    private var drawerLayout :DrawerLayout?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        FirestoresClass().getUserDetail(this)
    }
    fun actionBar(){
        toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title= ""
        toolbar!!.setNavigationIcon(R.drawable.baseline_menu_24)
        toolbar?.setNavigationOnClickListener {
            toggleDrawer()
        }

    }
    fun toggleDrawer() {
         drawerLayout = findViewById(R.id.layout_drawer);
        if ( drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout!!.openDrawer(GravityCompat.START);
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile ->{
                startActivity(Intent(this,SettingActivity::class.java))
            }

        }
        return true
    }


    fun  navigationUserDetail(user: User){
        val image : CircleImageView = findViewById(R.id.nav_user_image)
        val email :TextView = findViewById(R.id.tv_email_nav)
        val name : TextView = findViewById(R.id.tv_username_nav)
        name.text =user.username
        email.text = user.email

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .into(image)
    }

}