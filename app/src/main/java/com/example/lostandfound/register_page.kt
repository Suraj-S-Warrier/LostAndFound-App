package com.example.lostandfound

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lostandfound.databinding.ActivityRegisterPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class register_page : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterPageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var ImageUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPageBinding.inflate(layoutInflater)
        var username: EditText
        var rollno: EditText
        var email: EditText
        var password: EditText
        var confirmpassword: EditText
        var phone: EditText
        var uploadpic: Button
        var submit: Button

        setContentView(binding.root)
// binding seems to be working good enough, but for getting user input and other stuff, use find by id anol because using binding it doesn't seem to work
        firebaseAuth = FirebaseAuth.getInstance()


        binding.button4.setOnClickListener {
            val intent =Intent(this, SelectImageScreen::class.java)
            startActivity(intent)
        }
        binding.button5.setOnClickListener {

            val db = Firebase.firestore
            val user = firebaseAuth.getCurrentUser()
            username = findViewById(R.id.editTextTextPersonName)
            rollno = findViewById(R.id.editTextTextPersonName2)
            email = findViewById(R.id.editTextTextEmailAddress)
            password = findViewById(R.id.editTextTextPassword)
            confirmpassword = findViewById(R.id.editTextTextPassword2)
            phone = findViewById(R.id.editTextPhone)
            uploadpic = findViewById(R.id.button4)
            submit = findViewById(R.id.button5)

            // in almost every file, the ids of the views are not very intuitive and i forgot to change those..if possible change those too
//            val email = binding.editTextTextEmailAddress.toString()
//            val pass = binding.editTextTextPassword.toString()
//            val confirmpass = binding.editTextTextPassword2.toString()
//            val name = binding.editTextTextPersonName.toString()
//            val rollno = binding.editTextTextPersonName2.toString()
            if (username.text.isNotEmpty() && rollno.text.isNotEmpty() && email.text.isNotEmpty() && password.text.isNotEmpty() && confirmpassword.text.isNotEmpty()){
                if (password.text.toString() == confirmpassword.text.toString()) {
                    if (email.text.split("@")[1] == "iitp.ac.in") {
                        firebaseAuth.createUserWithEmailAndPassword(
                            email.text.toString(),
                            password.text.toString()
                        )
                            .addOnCompleteListener {

                                if (it.isSuccessful) {



                                    if (user != null) {
                                        user.sendEmailVerification().addOnSuccessListener {
                                            Toast.makeText(
                                                this,
                                                "Click on the activation link sent to your email id to activate account",
                                                Toast.LENGTH_SHORT
                                            ).show()


                                        }.addOnFailureListener {
                                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                                        }
                                    }

//                                    Toast.makeText(
//                                        this,
//                                        "Account activated successfully",
//                                        Toast.LENGTH_SHORT
//                                    ).show()

                                    val uid = user?.uid.toString()
                                    val regData = hashMapOf(
                                        "uid" to uid,
                                        "username" to username.text.toString(),
                                        "rollno" to rollno.text.toString(),
                                        "email" to email.text.toString(),
                                        "phone" to phone.text.toString()
                                    )
                                    db.collection("users").document(uid).set(regData)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this,
                                                "Data added successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this,
                                                e.message.toString(),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
            // need to figure out how to send userdata to database after the email verification



                                } else {
                                    Toast.makeText(
                                        this,
                                        it.exception.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                    } else {
                        Toast.makeText(this, "needs an iitp email id", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "password not matching.Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else{
                Toast.makeText(this, "Fill in all the fields",Toast.LENGTH_SHORT).show()
            }



//                if(pass == confirmpass ){
//                    if(email.split("@")[1]== "iitp.ac.in") {
//                        firebaseAuth.createUserWithEmailAndPassword(email, pass)
//                            .addOnCompleteListener {
//
//                                // it basically returns the result of the createuser command
//                                if (it.isSuccessful) {
//
//                                    var user = firebaseAuth.getCurrentUser()
//                                    if (user != null) {
//                                        user.sendEmailVerification().addOnCompleteListener {
//                                            Toast.makeText(
//                                                this,
//                                                "Click on the activation link sent to your email id to activate account",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//
//                                        }
//                                    }
//                                    Toast.makeText(
//                                        this,
//                                        "Account activated successfully",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//
//                                    //intents are used to move from one activity to another or even one application to another but right now we prolly dont need the latter
//                                    val intent = Intent(this, MainActivity::class.java)
//                                    startActivity(intent)
//                                } else {
//                                    Toast.makeText(
//                                        this,
//                                        it.exception.toString(),
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//
//                            }
//                    }
//                    else{
//                        Toast.makeText(this, "Enter an iitp email id",Toast.LENGTH_SHORT).show()
//                    }
//
//
//                }
//                else{
//                    Toast.makeText(this, "password not matching. Please try again",Toast.LENGTH_SHORT).show()
//
//                }
//            }
//            else{
//                Toast.makeText(this, "One or more essential field(s) is missing", Toast.LENGTH_SHORT).show()
//            }





        }
    }


}