package com.example.foodorderapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.foodorderapp.databinding.ActivityMainBinding
import com.example.foodorderapp.databinding.ActivityStartBinding
import com.example.foodorderapp.MainActivity as homeActivity

class StartActivity : AppCompatActivity() {
    lateinit var binding : ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getWindow().setStatusBarColor(Color.parseColor("#49239F"));
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = Color.parseColor("#49239F")


        binding.getstartedbtn.setOnClickListener {
            val intent = Intent(this, homeActivity::class.java)
            startActivity(intent)
        }
    }
}

