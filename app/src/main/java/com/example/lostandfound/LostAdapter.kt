package com.example.lostandfound

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import papaya.`in`.sendmail.SendMail
import java.io.File


class LostAdapter (private val arrayList: ArrayList<LostModel>, val context: Context) :
    RecyclerView.Adapter<LostAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val IFoundIt:Button = itemView.findViewById(R.id.Found_It)
        val Loser_name: TextView = itemView.findViewById(R.id.Loser_name)
        val Loser_Phone: TextView = itemView.findViewById(R.id.Loser_phone)
        val Loser_message: TextView = itemView.findViewById(R.id.Loser_message)
        val Lost_time: TextView = itemView.findViewById(R.id.Lost_date)
        val LostItemImage: ImageView = itemView.findViewById(R.id.LostItemFeedImage)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lostitem_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayList[position]
        currentItem.image?.let { holder.LostItemImage.setImageResource(it) }
        holder.Loser_name.text = currentItem.name
        holder.Loser_Phone.text = currentItem.phone
        holder.Loser_message.text = currentItem.Message
        holder.Lost_time.text = currentItem.time

        lateinit var firebaseAuth: FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.getCurrentUser()
        val uid = user?.uid.toString()
        var phone_no: String  =""
        var username: String=""

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
            holder.LostItemImage.setImageBitmap(bitmap)
        }.addOnFailureListener{

            if(progressDialog.isShowing)
            {
                progressDialog.dismiss()
            }
            Toast.makeText(context, it.message,Toast.LENGTH_SHORT).show()
        }



        val db = FirebaseFirestore.getInstance()
        val lostRef = db.collection("users")
        lostRef.whereEqualTo("uid",uid).get().addOnSuccessListener{ result ->
            phone_no = result.documents[0]["phone"].toString()
            username = result.documents[0]["username"].toString()


        }




        holder.IFoundIt.setOnClickListener {

            val mail = SendMail(
                "LostAndFound2101cs75@gmail.com", "wxomqsziyoefttdm",
                currentItem.email,
                "Item has been found",
                "Finder:${username}\nPhone:${phone_no}"
            )
            mail.execute()
            Toast.makeText(context,"Mail sent to owner",Toast.LENGTH_SHORT ).show()
        }



    }

    override fun getItemCount(): Int {
        return arrayList.size

    }
}
