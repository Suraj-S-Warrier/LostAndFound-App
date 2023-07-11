package com.example.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.lostandfound.databinding.ActivityRegisterPageBinding
import com.example.lostandfound.databinding.ActivityUpdatePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.logging.Logger.global

class UpdatePassword : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityUpdatePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var currentpassword: EditText
        var newpassword: EditText
        var confirmpassword: EditText

        firebaseAuth = FirebaseAuth.getInstance()



        binding.button2.setOnClickListener{
            val user = Firebase.auth.currentUser
            val db = Firebase.firestore
            val UID = user?.uid
            var userPassword :String =""
            var userEmail= ""
            var emailagain: String = ""
            var ct: Int =0


            val userdata = db.collection("users").whereEqualTo("uid", UID.toString())
            userdata.get().addOnSuccessListener {

               for(doc in it){
                   userEmail = doc["email"].toString()

                   currentpassword = findViewById(R.id.editTextTextPassword4)
                   newpassword = findViewById(R.id.editTextTextPassword5)
                   confirmpassword = findViewById(R.id.editTextTextPassword6)
                   if(currentpassword.text.toString().isNotEmpty() && newpassword.text.toString().isNotEmpty() && confirmpassword.text.toString().isNotEmpty()){
                       if(newpassword.text.toString() == confirmpassword.text.toString()){


                    val credential = EmailAuthProvider
                        .getCredential(userEmail, currentpassword.text.toString())
                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener{
                            Toast.makeText(this, "user re-authenticated",Toast.LENGTH_SHORT).show()
                        }
                        ?.addOnCanceledListener {
                            Toast.makeText(this, "authentication went wrong",Toast.LENGTH_SHORT).show()
                        }
                    user?.updatePassword(newpassword.text.toString())
                        ?.addOnSuccessListener {
                            Toast.makeText(this, "Password Changed successfully",Toast.LENGTH_SHORT).show()

                        }
                        ?.addOnFailureListener {
                            Toast.makeText(this, "Failed to change password",Toast.LENGTH_SHORT).show()
                        }


                       }
                       else{
                           Toast.makeText(this, "Password not matching. Please try again.",Toast.LENGTH_SHORT).show()
                       }
                   }
                   else{
                       Toast.makeText(this, "Fill all the fields",Toast.LENGTH_SHORT).show()
                   }

               }
            }

//            var userEmail = userdata?.result?.data?.get("email").toString()


            currentpassword = findViewById(R.id.editTextTextPassword4)
            newpassword = findViewById(R.id.editTextTextPassword5)
            confirmpassword = findViewById(R.id.editTextTextPassword6)
            if(currentpassword.text.toString().isNotEmpty() && newpassword.text.toString().isNotEmpty() && confirmpassword.text.toString().isNotEmpty()){
                if(newpassword.text.toString() == confirmpassword.text.toString()){


//                    val credential = EmailAuthProvider
//                        .getCredential(userEmail, currentpassword.text.toString())
//                    user?.reauthenticate(credential)
//                        ?.addOnCompleteListener{
//                            Toast.makeText(this, "user re-authenticated",Toast.LENGTH_SHORT).show()
//                        }
//                        ?.addOnCanceledListener {
//                            Toast.makeText(this, "authentication went wrong",Toast.LENGTH_SHORT).show()
//                        }
//                    user?.updatePassword(newpassword.text.toString())
//                        ?.addOnSuccessListener {
//                            Toast.makeText(this, "Password Changed successfully",Toast.LENGTH_SHORT).show()
//
//                        }
//                        ?.addOnFailureListener {
//                            Toast.makeText(this, "Failed to change password",Toast.LENGTH_SHORT).show()
//                        }

//                        var newData = curUser
//                        if (newData != null) {
//                            newData["password"] = newpassword.text.toString()
//                        }
//                        if (newData != null ) {
//
//                            db.collection("users").document().update(newData as Map<String, Any>)
//                                .addOnSuccessListener {
//                                    Toast.makeText(this, "password changed in database successfully", Toast.LENGTH_SHORT).show()
//                                    val intent = Intent(this, Main_page::class.java)
//                                    startActivity(intent)
//                                }
//                                .addOnFailureListener{
//                                    Toast.makeText(this, "Failed to add data to database",Toast.LENGTH_SHORT).show()
//                                }
//
//
//                        }


                }
                else{
                    Toast.makeText(this, "Password not matching. Please try again.",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Fill all the fields",Toast.LENGTH_SHORT).show()
            }

        }
    }
}