package com.gmail_bssushant2003.journeycraft

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.gmail_bssushant2003.journeycraft.Models.Items
import com.gmail_bssushant2003.journeycraft.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var isLiftedVeg = false
    private var isLiftedNonVeg = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //change status bar color to white
        window.statusBarColor = resources.getColor(R.color.white, theme)

        val item = intent.getSerializableExtra("individualDestination") as? Items

        if(item != null){
            binding.imageView.setImageResource(item.image)
            binding.title.text = item.title
            binding.location.text = item.location
        }
    }
}