package com.gmail_bssushant2003.journeycraft

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.gmail_bssushant2003.journeycraft.Models.Items
import com.gmail_bssushant2003.journeycraft.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var isLiftedVeg = false
    private var isLiftedNonVeg = false
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //change status bar color to white
        window.statusBarColor = resources.getColor(R.color.white, theme)

        val individualDestination = intent.getStringExtra("individualDestination")
        val destinationstate = intent.getStringExtra("statedestination")

//        Toast.makeText(this,individualDestination,Toast.LENGTH_SHORT).show()
//        Toast.makeText(this,destinationstate,Toast.LENGTH_LONG).show()

        val textViewindidest = findViewById<TextView>(R.id.individualdest)
        val textViewstate = findViewById<TextView>(R.id.statedest)

        textViewindidest.text = individualDestination
        textViewstate.text = destinationstate

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

    }
}