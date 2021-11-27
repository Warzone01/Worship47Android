package com.kirdevelopment.worship47andorid2.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kirdevelopment.worship47andorid2.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}