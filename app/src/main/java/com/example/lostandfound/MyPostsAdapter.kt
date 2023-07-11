package com.example.lostandfound

import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfound.databinding.FounditemRowBinding
import com.example.lostandfound.databinding.LostitemRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import papaya.`in`.sendmail.SendMail
import java.io.File


class MyPostsAdapter (private val arrayList: ArrayList<FoundModel>, val context: Context) :
    RecyclerView.Adapter<MyPostsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val Delete: Button = itemView.findViewById(R.id.Delete)
        val MyPosts_name: TextView = itemView.findViewById(R.id.MyPosts_name)
        val MyPosts_Phone: TextView = itemView.findViewById(R.id.MyPosts_phone)
        val MyPosts_message: TextView = itemView.findViewById(R.id.MyPosts_message)
        val MyPosts_time: TextView = itemView.findViewById(R.id.MyPosts_date)
        val MyPostsItemImage: ImageView = itemView.findViewById(R.id.MyPosts_ItemImage)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayList[position]
        currentItem.image?.let { holder.MyPostsItemImage.setImageResource(it) }
        holder.MyPosts_name.text = currentItem.name
        holder.MyPosts_Phone.text = currentItem.phone
        holder.MyPosts_message.text = currentItem.Message
        holder.MyPosts_time.text = currentItem.time

        lateinit var firebaseAuth: FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.getCurrentUser()
        val uid = user?.uid.toString()
//        val emailid = user?.email.toString()
        val db = FirebaseFirestore.getInstance()


        if(currentItem.collectionid ==1){
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Fetching Image...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            var ImageName: String = "Lost Item Post/${currentItem.UID}/${currentItem.time}"
            val storageRef = FirebaseStorage.getInstance().reference.child(ImageName)
            val localfile = File.createTempFile("tempImage","jpg")
            storageRef.getFile(localfile).addOnSuccessListener {

                if(progressDialog.isShowing)
                {
                    progressDialog.dismiss()
                }
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                holder.MyPostsItemImage.setImageBitmap(bitmap)
            }.addOnFailureListener{

                if(progressDialog.isShowing)
                {
                    progressDialog.dismiss()
                }
                Toast.makeText(context, it.message,Toast.LENGTH_SHORT).show()
            }
        }
        else if(currentItem.collectionid ==2){
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Fetching Image...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            var ImageName: String = "Found Item Post/${currentItem.UID}/${currentItem.time}"
            val storageRef = FirebaseStorage.getInstance().reference.child(ImageName)
            val localfile = File.createTempFile("tempImage","jpg")
            storageRef.getFile(localfile).addOnSuccessListener {

                if(progressDialog.isShowing)
                {
                    progressDialog.dismiss()
                }
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                holder.MyPostsItemImage.setImageBitmap(bitmap)
            }.addOnFailureListener{

                if(progressDialog.isShowing)
                {
                    progressDialog.dismiss()
                }
                Toast.makeText(context, it.message,Toast.LENGTH_SHORT).show()
            }
        }


        holder.Delete.setOnClickListener {

            if(currentItem.collectionid == 1)
            {


                val MyPostsRef = db.collection("Lost Item Post")
                MyPostsRef.whereEqualTo("UID",uid).whereEqualTo("time",currentItem.time).get()
                    .addOnSuccessListener { result ->
                        for(doc in result){
                            doc.reference.delete()
                            Toast.makeText(context, "Post deleted",Toast.LENGTH_SHORT).show()
                        }

                    }
            }
            if(currentItem.collectionid == 2)
            {
                val MyPostsRef = db.collection("Found Item Post")
                MyPostsRef.whereEqualTo("UID",uid).whereEqualTo("time",currentItem.time).get()
                    .addOnSuccessListener { result ->
                        for(doc in result){
                            doc.reference.delete()
                        }

                    }
            }
        }








    }

    override fun getItemCount(): Int {
        return arrayList.size

    }
}