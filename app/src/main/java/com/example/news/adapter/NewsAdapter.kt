package com.example.news.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.ViewHistoryActivity
import com.example.news.WebViewActivity
import com.example.news.firebase.FirestoresClass
import com.example.news.model.News

class NewsAdapter(val context: Context, val list: List<News?>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var mClickListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
//        return ViewHolder(view, mClickListener)
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is ViewHolder){
            holder.desc.text = model!!.title
            holder.time.text = model.time.substring(4, 16)
            Glide.with(context).load(model.linkImage).into(holder.img)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("link",model.link)
                FirestoresClass().viewHistory(this,model)
                context.startActivity(intent)
            }
        }
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.img)
        val desc: TextView = view.findViewById(R.id.news_title)
        val time: TextView = view.findViewById(R.id.news_time)

//        init {
//            view.setOnClickListener {
//                listener.onItemClick(adapterPosition)
//            }
//        }
    }

}