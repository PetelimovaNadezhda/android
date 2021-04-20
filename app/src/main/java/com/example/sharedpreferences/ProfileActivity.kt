package com.example.sharedpreferences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedpreferences.databinding.ProfileActivityBinding


private lateinit var binding: ProfileActivityBinding

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val text = getString(R.string.completed_topics) + " " + intent.getStringExtra("topics")
        binding.textView2.text = text

    }
}