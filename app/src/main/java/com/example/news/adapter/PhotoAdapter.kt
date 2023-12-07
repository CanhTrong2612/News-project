package com.example.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.news.R
import com.example.news.model.Photo
import com.squareup.picasso.Picasso
class PhotoAdapter(val context:Context, val list:ArrayList<Photo>): PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_layout_image,container,false)
        val img :ImageView= view.findViewById(R.id.image_photo)
        val model = list[position]
        if (model!=null){
            Picasso.get()
                .load(model.imgId)
                .fit()
                .into(img)
        }
        container.addView(view)

        return view
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view ==`object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)

    }

//class ViewPagerAdapter(val context: Context,val list:ArrayList<Photo>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//       return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_image,parent,false))
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is MyViewHolder){
//            val model = list[position]
//            if (model!=null){
//                Picasso.get()
//                    .load(model.imgId)
//                    .fit()
//                    .into(holder.img)
//            }
//        }
//    }
//    class MyViewHolder( view:View):RecyclerView.ViewHolder(view){
//        val img:ImageView = view.findViewById(R.id.image_photo)
//    }
}