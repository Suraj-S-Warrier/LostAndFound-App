package com.example.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lostandfound.databinding.ActivityMainBinding
import com.example.lostandfound.databinding.ActivityRegisterPageBinding
import com.google.firebase.auth.FirebaseAuth

class register_page : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.button5.setOnClickListener{

            // in almost every file, the ids of the views are not very intuitive and i forgot to change those..if possible change those too
            val email = binding.editTextTextEmailAddress.toString()
            val pass = binding.editTextTextPassword.toString()
            val confirmpass = binding.editTextTextPassword2.toString()
            val name = binding.editTextTextPersonName.toString()
            val rollno = binding.editTextTextPersonName2.toString()

            if(name.isNotEmpty() && rollno.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty()){
                if(pass == confirmpass){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{

                        // it basically returns the result of the createuser command
                        if(it.isSuccessful){
                            Toast.makeText(this, "Account activated successfully",Toast.LENGTH_SHORT).show()

                            //intents are used to move from one activity to another or even one application to another but right now we prolly dont need the latter
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }

                    }

                }
                else{
                    Toast.makeText(this, "Password is not matching. Please try again.",Toast.LENGTH_SHORT).show()

                }
            }
            else{
                Toast.makeText(this, "One or more essential field(s) is missing", Toast.LENGTH_SHORT).show()
            }



        }
    }
}