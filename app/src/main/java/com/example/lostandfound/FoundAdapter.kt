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


class FoundAdapter (private val arrayList: ArrayList<FoundModel>, val context: Context) :
    RecyclerView.Adapter<FoundAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val Claimit:Button = itemView.findViewById(R.id.Claim_It)
        val Finder_name: TextView = itemView.findViewById(R.id.Finder_name)
        val Finder_Phone: TextView = itemView.findViewById(R.id.Finder_phone)
        val Finder_message: TextView = itemView.findViewById(R.id.Finder_message)
        val Found_time: TextView = itemView.findViewById(R.id.Found_date)
        val FoundItemImage: ImageView = itemView.findViewById(R.id.FoundItemFeedImage)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.founditem_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayList[position]
        currentItem.image?.let { holder.FoundItemImage.setImageResource(it) }
        holder.Finder_name.text = currentItem.name
        holder.Finder_Phone.text = currentItem.phone
        holder.Finder_message.text = currentItem.Message
        holder.Found_time.text = currentItem.time

        lateinit var firebaseAuth: FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.getCurrentUser()
        val uid = user?.uid.toString()
        val emailid = user?.email.toString()
        var phone_no: String  =""
        var username: String=""

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
            holder.FoundItemImage.setImageBitmap(bitmap)
        }.addOnFailureListener{

            if(progressDialog.isShowing)
            {
                progressDialog.dismiss()
            }
            Toast.makeText(context, it.message,Toast.LENGTH_SHORT).show()
        }

//        val TAG: String = this.context.toString()


        val db = FirebaseFirestore.getInstance()
        val lostRef = db.collection("users")
        lostRef.whereEqualTo("uid",uid).get().addOnSuccessListener{ result ->
            phone_no = result.documents[0]["phone"].toString()
            username = result.documents[0]["username"].toString()

        }

        holder.Claimit.setOnClickListener {

            val mail = SendMail(
                "LostAndFound2101cs75@gmail.com", "wxomqsziyoefttdm",
                currentItem.email,
                "The item's owner has been found'",
                "Owner:${username}\nPhone:${phone_no}\nEmail:${emailid}"
            )
            mail.execute()
            Toast.makeText(context,"mail sent to finder",Toast.LENGTH_SHORT ).show()
        }



    }

    override fun getItemCount(): Int {
        return arrayList.size

    }
}
