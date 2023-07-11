package com.example.lostandfound

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lostandfound.databinding.ActivityPostFoundItemBinding
import com.example.lostandfound.databinding.ActivityPostLostItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class PostFoundItem : AppCompatActivity() {
    private lateinit var binding: ActivityPostFoundItemBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var calendar: Calendar
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var ImageUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostFoundItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.UploadFoundItemImgs.setOnClickListener {
            selectImage()
        }
        binding.submitFoundItem.setOnClickListener{
            val name = binding.NameFoundItem.text.toString()
            val phone = binding.FoundItemPhone.text.toString()
            val whereFound = binding.WhereFoundInfo.text.toString()
            val Msg = binding.MsgFoundItem.text.toString()
            var date: String


            if(name.isNotEmpty() && phone.isNotEmpty() && whereFound.isNotEmpty() && Msg.isNotEmpty()){
                val db = Firebase.firestore
                var user = firebaseAuth.getCurrentUser()
                val uid = user?.uid.toString()
                val emailid = user?.email.toString()
                calendar = Calendar.getInstance()
                simpleDateFormat =  SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
                date = simpleDateFormat.format(calendar.time)
                val postData = hashMapOf(
                    "UID" to uid,
                    "time" to date,
                    "name" to name,
                    "phone" to phone,
                    "Where" to whereFound,
                    "Message" to Msg,
                    "email" to emailid,
                    "collectionid" to 2

                )
                uploadImage()
                db.collection("Found Item Post").add(postData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "data added successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Main_page::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }

//                db.collection("users").document(uid).collection("MyPosts").add(postData)



            }
            else{
                Toast.makeText(this, "One or more essential field(s) left blank", Toast.LENGTH_SHORT).show()
            }
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
        val storageReference = FirebaseStorage.getInstance().getReference("Found Item Post/${uid}/$filename")

        storageReference.putFile(ImageUri).
        addOnSuccessListener {
            Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
//            this.finish()
        }
            .addOnFailureListener{
                Toast.makeText(this, "Error Occurred",Toast.LENGTH_SHORT).show()
//                this.finish()
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
            binding.PostFoundItemImage.setImageURI(ImageUri)
        }
    }
}