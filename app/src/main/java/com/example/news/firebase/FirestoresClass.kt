package com.example.news.firebase

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.news.LoginActivity
import com.example.news.MainActivity
import com.example.news.ProfileActivity
import com.example.news.RegisterActivity
import com.example.news.SettingActivity
import com.example.news.ViewHistoryActivity
import com.example.news.adapter.NewsAdapter
import com.example.news.model.News
import com.example.news.model.User
import com.example.news.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoresClass {
    private var mFireStore = FirebaseFirestore.getInstance()
   // val mFireStore = Firebase.firestore
    fun registerUser(activity: RegisterActivity, user: User) {
        mFireStore.collection("user")
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }
            .addOnFailureListener { e ->
//                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while registering the user", e)

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

    fun getUserDetail(activity: Activity) {
        mFireStore.collection("user")
            .document(getCurrentID())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                Log.e("ttttt",user.toString())
                when (activity) {
                    is LoginActivity -> {
                        if (user != null)
                            activity.userLoggedInSuccess(user)

                    }

                    is MainActivity -> {
                        if (user != null) {
                            activity.navigationUserDetail(user)
                        }
                    }
//                    is ProfileActivity ->{
//                        if (user!= null)
//                            activity.getDataUser(user)
//                    }
                    is SettingActivity ->{
                        if (user!= null)
                            activity.loadData(user)
                        Log.e("tudududut",user.toString())
                    }
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
        mFireStore.collection("user")
            .document(getCurrentID())
            .update(hashMap)
            .addOnSuccessListener {
                Log.e("vdlvnf",hashMap.toString())
                activity.updateUserSuccess()

            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while updating the user details", e)
            }
    }
    fun viewHistory(adapter: NewsAdapter,news: News){
        mFireStore.collection("new")
            .document()
            .set(news, SetOptions.merge())
            .addOnSuccessListener {

            }
    }
    fun getViewHistory(activity: ViewHistoryActivity){
        mFireStore.collection("new")
            .get()
            .addOnSuccessListener { document->
                val listNews = ArrayList<News>()
                Log.e("vvvkdv",document.documents.toString())
                for ( i in document){
                    var news = i.toObject(News::class.java)!!
                     news.id = i.id.trim()
                    listNews.add(news)
                }

                activity.load(listNews)
            }

    }
    fun deleteViewHistory(activity: ViewHistoryActivity,id:String){
        mFireStore.collection("new")
            .document(id)
            .delete()
            .addOnSuccessListener {
                activity.getViewHistorySuccess()
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"loi",e)
            }
    }


}
