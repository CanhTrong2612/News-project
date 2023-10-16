package com.example.news.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constant {
    const val EXTRA_USER_DETAILS ="extra_user_detail"
    const val READ_STORE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE=2
    const val USER_PROFILE_IMAGE = "user_profile_image"
    const val COMPLETE_PROFILE ="profileComplete"
    const val PHONE = "phone"
    const val IMAGE ="image"
    const val MY_PROFILE_REQUEST_CODE = 11

    fun getFileExtension(activity: Activity, uri: Uri):String{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
            .toString()
    }
}