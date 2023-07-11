package com.example.lostandfound

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lostandfound.databinding.ActivityRegisterPageBinding
import com.example.lostandfound.databinding.ActivitySelectImageScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class SelectImageScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySelectImageScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var ImageUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectImageScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectImage()
        binding.UploadImageAfterSelection.setOnClickListener {
            uploadImage()
        }

    }

    private fun uploadImage() {

        lateinit var firebaseAuth: FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val uid = user?.uid
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading file....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("${uid}/$filename")

        storageReference.putFile(ImageUri).
                addOnSuccessListener {
                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
            .addOnFailureListener{
                Toast.makeText(this, "Error Occurred",Toast.LENGTH_SHORT).show()
                this.finish()
            }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.ProfileImage.setImageURI(ImageUri)
        }
    }
}