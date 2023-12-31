package com.example.news

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adapter.HistoryAdapter
import com.example.news.databinding.ActivityViewHistoryBinding
import com.example.news.firebase.FirestoresClass
import com.example.news.model.News

class ViewHistoryActivity : AppCompatActivity() {
    private var binding:ActivityViewHistoryBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewHistoryBinding.inflate(layoutInflater)
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
        val adapter = HistoryAdapter(this,list,this)
        binding?.rvHistory?.layoutManager= LinearLayoutManager(this)
        binding?.rvHistory?.adapter= adapter
    }
    fun getViewHistorySuccess(){
        FirestoresClass().getViewHistory(this)
    }
    fun delete(id:String){
        dilaog(id)
    }
    fun dilaog(id:String){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Delete")
        dialog.setMessage("Do you want to delete this item?")
        dialog.setPositiveButton("Yes"){dialog,which->
            FirestoresClass().deleteViewHistory(this,id)

        }
        dialog.setNegativeButton("No"){dialog,which->
            dialog.dismiss()
        }
        dialog.create()
        dialog.show()

    }

}