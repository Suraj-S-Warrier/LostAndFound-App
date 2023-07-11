package com.example.lostandfound

import android.os.Bundle;
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MyPosts : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<FoundModel>
    private lateinit var myadapter: MyPostsAdapter
    private lateinit var db: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)

        recyclerView = findViewById(R.id.recyclerView_MyPosts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        myadapter = MyPostsAdapter(userArrayList, this)
        recyclerView.adapter = myadapter


        EventChangeListener()

    }

    private fun EventChangeListener() {
        lateinit var firebaseAuth: FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.getCurrentUser()
        val uid = user?.uid.toString()
        db = FirebaseFirestore.getInstance()
        db.collection("Found Item Post").whereEqualTo("UID",uid).
        addSnapshotListener(object: EventListener<QuerySnapshot>{
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ){
                if(error!= null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }

                for(dc : DocumentChange in value?.documentChanges!!){

                    if(dc.type == DocumentChange.Type.ADDED){
                        userArrayList.add(dc.document.toObject(FoundModel::class.java))
                    }

                }
                myadapter.notifyDataSetChanged()

            }

        })

        db.collection("Lost Item Post").whereEqualTo("UID",uid).
        addSnapshotListener(object: EventListener<QuerySnapshot>{
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ){
                if(error!= null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }

                for(dc : DocumentChange in value?.documentChanges!!){

                    if(dc.type == DocumentChange.Type.ADDED){
                        userArrayList.add(dc.document.toObject(FoundModel::class.java))
                    }

                }
                myadapter.notifyDataSetChanged()

            }

        })
    }
}