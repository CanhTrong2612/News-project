package com.example.news

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adapter.HistoryAdapter
import com.example.news.adapter.SaveAdapter
import com.example.news.databinding.ActivitySaveBinding
import com.example.news.firebase.FirestoresClass
import com.example.news.model.News

class SaveActivity : AppCompatActivity() {
    private var binding: ActivitySaveBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
       getViewHistorySuccess()
    }
    fun actionBar(){
        setSupportActionBar(binding?.toolbarHistory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back)
        supportActionBar?.title= ""
        binding?.toolbarHistory?.setNavigationOnClickListener { onBackPressed() }
    }
    fun load(list: ArrayList<News>){
        val adapter = SaveAdapter(this,list,this)
        binding?.rvSave?.layoutManager= LinearLayoutManager(this)
        binding?.rvSave?.adapter= adapter
    }
    fun getViewHistorySuccess(){
        FirestoresClass().get(this)
    }
    fun deleteReadLater(id:String){
        dilaog(id)
//        FirestoresClass().deleteReadLater(this,id)
//        Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show()
    }
    fun dilaog(id:String){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Delete")
        dialog.setMessage("Do you want to delete this item?")
        dialog.setPositiveButton("Yes"){dialog,which->
            FirestoresClass().deleteReadLater(this,id)

        }
        dialog.setNegativeButton("No"){dialog,which->
            dialog.dismiss()
        }
        dialog.create()
        dialog.show()

    }
}