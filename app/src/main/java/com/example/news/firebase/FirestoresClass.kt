package com.example.news.firebase

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.example.news.LoginActivity
import com.example.news.MainActivity
import com.example.news.ProfileActivity
import com.example.news.RegisterActivity
import com.example.news.model.User
import com.example.news.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoresClass {
    private var mFireStore = FirebaseFirestore.getInstance()
    fun registerUser(activity: RegisterActivity, user: User){
        mFireStore.collection("users")
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while registering the user",e)

            }
    }
    fun getCurrentID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
    fun getUserDetail(activity: Activity){
        mFireStore.collection("users")
            .document(getCurrentID())
            .get()
            .addOnSuccessListener {document->
                val user = document.toObject(User::class.java)
                when(activity){
                    is LoginActivity ->{
                        if (user!= null)
                            activity. userLoggedInSuccess(user)
                    }
                    is MainActivity -> {
                        if (user!=null){
                            activity.navigationUserDetail(user)
                        }
                    }
//                    is ProfileActivity ->{
//                        if (user!= null)
//                            activity.getDataUser(user)
//                    }
//                    is SettingActivity ->{
//                        if (user!= null)
//                            activity.userDetailsSuccess(user)
//                    }
                }
            }
    }
    fun uploadImageToCloudStorge(activity: Activity, imageUri: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constant.USER_PROFILE_IMAGE + System.currentTimeMillis() +
                    "." + Constant.getFileExtension(activity, imageUri!!)
        )
        sRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase image url", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
            taskSnapshot.metadata!!.reference!!.downloadUrl
        }
            .addOnSuccessListener { uri ->
                Log.e("Downlaod image Url", uri.toString())
                when (activity) {
                    is ProfileActivity -> {
                        activity.imageUploadSuccess(imageUri!!.toString())
                    }


                }

            }
            .addOnFailureListener {
                when (activity) {
                    is ProfileActivity ->
                        activity.hideProgressDialog()
                }
            }
    }
    fun updateUser(activity: ProfileActivity, hashMap: HashMap<String, Any>) {
        mFireStore.collection("users")
            .document(getCurrentID())
            .update(hashMap)
            .addOnSuccessListener {
                activity.updateUserSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while updating the user details", e)
            }

    }
}