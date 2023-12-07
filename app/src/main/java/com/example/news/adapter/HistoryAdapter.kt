package com.example.news.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.ViewHistoryActivity
import com.example.news.WebViewActivity
import com.example.news.firebase.FirestoresClass
import com.example.news.model.News

class HistoryAdapter(val context: Context,val list: ArrayList<News>,val activity: ViewHistoryActivity):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_view,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HistoryViewHolder)
        {
            Glide.with(context).load(list[position].linkImage).into(holder.img)
            holder.titl.text = list[position].title
            holder.date.text = list[position].time
            holder.btn.setOnClickListener {
                activity.delete(list[position].id)
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("link",list[position].link)
                context.startActivity(intent)
            }
        }


    }
    class HistoryViewHolder(view:View):RecyclerView.ViewHolder(view){
        val titl :TextView = view.findViewById(R.id.news_title_history)
        val date :TextView = view.findViewById(R.id.news_time_history)
        val img :ImageView = view.findViewById(R.id.imgHistory)
        val btn:ImageButton = view.findViewById(R.id.btn_close)
    }

}