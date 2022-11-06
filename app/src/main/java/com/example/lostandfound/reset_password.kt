package com.example.lostandfound

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.lostandfound.databinding.ActivityRegisterPageBinding
import com.example.lostandfound.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class reset_password : AppCompatActivity() {

    // binding basically helps to get a hold of all the views which are there in the activity
    private lateinit var binding: ActivityResetPasswordBinding

    // need to use this..basically getting the firebase object
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)

        // i dont know why..but some the firebase tutorial asked to chagne this setcontentview's argument to binding.root
        // also..need to set up firebase. first, got to tools and open firebase and connect to firebase..chrome opens..create a project..follow instructions..so when we click firebase, chrome opens to firebase
        // click the project you want to connect with. connection done

        // the add sdk option does everything else automatically
        setContentView(binding.root)

        //assigning a particular instance to that object defined before..
        firebaseAuth = FirebaseAuth.getInstance()


        binding.resetButton.setOnClickListener{
            // whatever output we get like this, we need to first convert to string using tostring function so that we can use it later
            val email: EditText = findViewById(R.id.editTextTextEmailAddress2)
            if(email.text.toString().isNotEmpty()){

                // in the assignment, it is given to send email and then give link- clicking that, we get new password and confirm password text-boxes.. but in this, all the process is done automatically..not being able to change anything and by default, only new password option seems to be there
                firebaseAuth.sendPasswordResetEmail(email.text.toString())
                    .addOnSuccessListener {

                        // toast is basically giving a message in the output
                        Toast.makeText(this, "Please check your email",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Please enter a registered email id",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}