package com.example.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lostandfound.databinding.ActivityMainBinding
import com.example.lostandfound.databinding.ActivityMainPageBinding

class Main_page : AppCompatActivity() {

    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.LostItemPost.setOnClickListener{
            val intent=  Intent(this,PostLostItem::class.java )
            startActivity(intent)
        }
        binding.FoundItemPost.setOnClickListener{
            val intent=  Intent(this,PostFoundItem::class.java )
            startActivity(intent)
        }
        binding.LostItemFeed.setOnClickListener{
            val intent=  Intent(this,FeedLostItem::class.java)
            startActivity(intent)
        }
        binding.FoundItemFeed.setOnClickListener {
            val intent = Intent(this, FeedFoundItem::class.java)
            startActivity(intent)
        }
        binding.MyPosts.setOnClickListener {
            val intent=  Intent(this,MyPosts::class.java)
            startActivity(intent)
        }
        binding.UpdatePassword.setOnClickListener{
            val intent=  Intent(this,UpdatePassword::class.java)
            startActivity(intent)
        }


    }
}