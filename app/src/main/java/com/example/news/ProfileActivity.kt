package com.example.news

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.news.databinding.ActivityProfileBinding
import com.example.news.firebase.FirestoresClass
import com.example.news.model.User
import com.example.news.utils.Constant

class ProfileActivity :BaseActivity() {
    private var binding:ActivityProfileBinding?= null
    private var mSelectImageUri: Uri? = null
    private lateinit var mImageUri :String
    private var mUser:User?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        if (intent.hasExtra(Constant.EXTRA_USER_DETAILS)){
            mUser = intent.getParcelableExtra(Constant.EXTRA_USER_DETAILS)
        }
        binding?.ivCameraProfile?.setOnClickListener {
            getImage()
        }
        binding?.btSubmitProfile?.setOnClickListener {
            if (validateUserProfile()) {
                showProgressDialog()
                if (mSelectImageUri != null) {
                    FirestoresClass().uploadImageToCloudStorge(this, mSelectImageUri)
                } else {
                    updateProfile()
                }
            }
        }

        getDataUer(mUser!!)
    }
    fun actionBar(){
        setSupportActionBar(binding?.toolbarProduct)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back)
        supportActionBar?.title= ""
        binding?.toolbarProduct?.setNavigationOnClickListener { onBackPressed() }
    }
    fun getImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            showImageChoosen()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.READ_STORE_PERMISSION_CODE
            )
        }
    }
    private fun showImageChoosen() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, Constant.PICK_IMAGE_REQUEST_CODE)
        //getAction.launch(gallery)

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showImageChoosen()
        } else {
            Toast.makeText(
                this,
                "You just dennied the permission for storage.You can aslo allow it from setting",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            requestCode == Constant.PICK_IMAGE_REQUEST_CODE && data!!.data != null
        ) {
            mSelectImageUri = data!!.data
            binding?.ivProfile?.setImageURI(Uri.parse(mSelectImageUri!!.toString()))
        }
    }
    fun validateUserProfile():Boolean{
        return when{
            TextUtils.isEmpty(binding?.profilePhone?.text)->{
                showErrorSnackBar("Please enter mobie number",false)
                false
            }

            else -> {
                true
            }
        }
    }
    fun getDataUer(user: User) {
        if (user.profileComplete==0){
            binding?.profileName?.isEnabled = false
            binding?.profileName?.setText(user.username)

            binding?.profileEmail?.isEnabled = false
            binding?.profileEmail?.setText(user.email)

        }
        else{
            binding?.profileName?.isEnabled = false
            binding?.profileName?.setText(user.username)

            binding?.profileEmail?.isEnabled = false
            binding?.profileEmail?.setText(user.email)

            binding?.profilePhone?.isEnabled = false
            binding?.profilePhone?.setText(user.phone)
            binding?.ivProfile?.let {
                Glide.with(this)
                    .load(user.image)
                    .override(200,300)
                    .centerCrop()
                    .into(it)
            };

        }
    }
    fun imageUploadSuccess(image: String) {
        mImageUri = image
        updateProfile()
        hideProgressDialog()
    }
    fun updateProfile(){
        val hashMap = HashMap<String,Any>()
        hashMap[Constant.COMPLETE_PROFILE]=1
        val mobieNumber = binding?.profilePhone?.text.toString()
        hashMap[Constant.PHONE] = mobieNumber
        hashMap[Constant.IMAGE]= mImageUri
        binding?.btSubmitProfile?.text = "Submit"
        FirestoresClass().updateUser(this,hashMap)
        Log.e("dldvniv",hashMap.toString())
    }

    fun updateUserSuccess() {
        startActivity(Intent(this,MainActivity::class.java))
    }
}