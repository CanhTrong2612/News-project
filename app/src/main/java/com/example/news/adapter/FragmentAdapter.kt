package com.example.news.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.news.fragment.BongDaFragment
import com.example.news.fragment.CongngheFragment
import com.example.news.fragment.DocLaFragment
import com.example.news.fragment.GiaiTriFragment
import com.example.news.fragment.MoiFragment
import com.example.news.fragment.PhapluatFragment
import com.example.news.fragment.SuckhoeFragment
import com.example.news.fragment.GiaoducFragment

class FragmentAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle){

    override fun getItemCount(): Int = 10

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> {
                return MoiFragment()
            }
            1 -> {
                return CongngheFragment()
            }
            2 -> {
                return DocLaFragment()
            }

            3 -> {
                return GiaiTriFragment()
            }

            4 -> {
                return PhapluatFragment()
            }
            5 -> {
                return SuckhoeFragment()
            }
            6 -> {
                return GiaoducFragment()
            }


            else -> return BongDaFragment()

        }
    }
}