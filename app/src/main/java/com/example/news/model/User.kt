package com.example.news.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val id :String= "",
                 val username :String="",
                 val email :String="",
                 val image :String ="",
                 val phone:String ="",
                 val  profileComplete:Int = 0
 ): Parcelable