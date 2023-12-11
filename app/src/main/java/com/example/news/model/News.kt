package com.example.news.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class News(
    val title :String = "",
    val link: String ="",
    val time: String= "",
    val linkImage :String = "",
//    val description: String="",
    var id :String= ""
): Parcelable