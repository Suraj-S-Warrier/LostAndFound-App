package com.example.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lostandfound.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
// havent made it look appealing..have to change its looks a lot but first functional parts needs to be done
// also some explanation comments written in reset_password.kt file for clarity
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.editTextNumberPassword.setOnClickListener{

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
            val email = binding.editTextTextEmailAddress3.toString()
            val password = binding.editTextNumberPassword.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // post login activities
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.button3.setOnClickListener{
            val intent = Intent(this, register_page::class.java)
            startActivity(intent)
        }
    }
}