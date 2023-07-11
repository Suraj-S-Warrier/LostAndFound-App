package com.example.lostandfound

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lostandfound.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File


// haven't made it look appealing..have to change its looks a lot but first functional parts needs to be done
// also some explanation comments written in reset_password.kt file for clarity
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading..")
        progressDialog.setCancelable(false)
        progressDialog.show()
        var ImageName: String = "/logo.png"
        val storageRef = FirebaseStorage.getInstance().reference.child(ImageName)
        val localfile = File.createTempFile("tempImage","png")
        storageRef.getFile(localfile).addOnSuccessListener {

            if(progressDialog.isShowing)
            {
                progressDialog.dismiss()
            }
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.logo.setImageBitmap(bitmap)
        }.addOnFailureListener{

            if(progressDialog.isShowing)
            {
                progressDialog.dismiss()
            }
            Toast.makeText(this, it.message,Toast.LENGTH_SHORT).show()
        }

        binding.textView2.setOnClickListener{

            // in the assignment, it is given to login using a username and password.. but yt seem to have only login with email and password method..have to find another way or have to go with this only
            val username = binding.editTextTextEmailAddress3.toString()


            if(username.isNotEmpty()) {
                val intent = Intent(this, reset_password::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Enter a valid username",Toast.LENGTH_SHORT).show()
            }

        }
        binding.button.setOnClickListener{
            val email: EditText=findViewById(R.id.editTextTextEmailAddress3)
            val password: EditText = findViewById(R.id.editTextTextPassword3)
            if(email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = firebaseAuth.currentUser?.isEmailVerified
                        if(user == true)
                        {
                            val intent = Intent(this, Main_page::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(this, "Please verify your email id",Toast.LENGTH_SHORT).show()
                        }

                    }
                    else {
                        Toast.makeText(this, "account not found. Please try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this, "Fill in all the fields",Toast.LENGTH_SHORT).show()
            }
        }
        binding.button3.setOnClickListener{
            val intent = Intent(this, register_page::class.java)
            startActivity(intent)
        }
    }
}