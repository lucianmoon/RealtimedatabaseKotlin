package com.example.realtimedatabasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.realtimedatabasekotlin.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.scan.setOnClickListener {
            Intent(this,ScannerUpdate::class.java).also{
                startActivity(it)
            }
        }
        binding.list.setOnClickListener {
            Intent(this,UserListActivity::class.java).also{
                startActivity(it)
                finish()
            }
        }
    }
}