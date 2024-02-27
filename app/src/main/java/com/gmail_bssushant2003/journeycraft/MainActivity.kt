package com.gmail_bssushant2003.journeycraft

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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

        //uplift button
        binding.buttonVeg.setOnClickListener {
            isLiftedVeg = !isLiftedVeg
            animateCardView(binding.buttonVeg, isLiftedVeg, true)
        }
        binding.buttonNonveg.setOnClickListener {
            isLiftedNonVeg = !isLiftedNonVeg
            animateCardView(binding.buttonNonveg, isLiftedNonVeg, false)
        }
    }

    private fun animateCardView(button: CardView, isLifted: Boolean, isVeg: Boolean) {
        // Calculate the scale factor based on the current state
        val scaleFactor = if (isLifted) 1.1f else 1.0f
        // Animate the CardView's scaleX and scaleY properties
        button.animate()
            .scaleX(scaleFactor)
            .scaleY(scaleFactor)
            .setDuration(100) // Adjust the duration as needed
            .start()

        if(isVeg){
            val colorRes = if (isLifted) R.color.veg_dark else R.color.veg_light
            button.setCardBackgroundColor(ContextCompat.getColor(this, colorRes))
        }
        else{
            val colorRes = if (isLifted) R.color.nonveg_dark else R.color.nonveg_light
            button.setCardBackgroundColor(ContextCompat.getColor(this, colorRes))
        }
    }
}