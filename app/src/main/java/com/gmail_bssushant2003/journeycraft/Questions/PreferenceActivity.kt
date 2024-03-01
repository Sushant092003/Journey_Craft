package com.gmail_bssushant2003.journeycraft.Questions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.gmail_bssushant2003.journeycraft.DestinationListActivity
import com.gmail_bssushant2003.journeycraft.Models.Preferences
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityPreferenceBinding

class PreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferenceBinding
    private var isLiftedTemple = false
    private var isLiftedBeach = false
    private var isLiftedHistorical = false
    private var isLiftedMountain = false
    private var isLiftedLake = false
    private var isLiftedMuseum = false
    private var isLiftedPark = false
    private var isLiftedWildlife = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //change color of status bar
        window.statusBarColor = resources.getColor(R.color.white, theme)

        preferenceSelection()

        binding.buttonDone.setOnClickListener {
            if(isAnyTrue()){
                val preferences = Preferences(isLiftedTemple, isLiftedBeach, isLiftedHistorical, isLiftedMountain, isLiftedLake, isLiftedMuseum, isLiftedPark, isLiftedWildlife)
                val mobileNumber = intent.getStringExtra("mobilenumber")
                val name = intent.getStringExtra("name")
                val intent = Intent(this, DestinationListActivity::class.java)
                intent.putExtra("mobilenumber", mobileNumber)
                intent.putExtra("name", name)
                intent.putExtra("preferences", preferences)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Please select your preference", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isAnyTrue(): Boolean {
        return (isLiftedBeach || isLiftedTemple || isLiftedHistorical || isLiftedMountain || isLiftedMuseum
                || isLiftedLake || isLiftedPark || isLiftedWildlife)
    }

    private fun preferenceSelection() {

        binding.templeCard.setOnClickListener {
            isLiftedTemple = !isLiftedTemple
            animateCardView(binding.templeCard, isLiftedTemple)
        }

        binding.beachCard.setOnClickListener {
            isLiftedBeach = !isLiftedBeach
            animateCardView(binding.beachCard, isLiftedBeach)
        }

        binding.historicalPlacesCard.setOnClickListener {
            isLiftedHistorical = !isLiftedHistorical
            animateCardView(binding.historicalPlacesCard, isLiftedHistorical)
        }

        binding.mountainCard.setOnClickListener {
            isLiftedMountain = !isLiftedMountain
            animateCardView(binding.mountainCard, isLiftedMountain)
        }

        binding.lakesCard.setOnClickListener {
            isLiftedLake = !isLiftedLake
            animateCardView(binding.lakesCard, isLiftedLake)
        }

        binding.museumCard.setOnClickListener {
            isLiftedMuseum = !isLiftedMuseum
            animateCardView(binding.museumCard, isLiftedMuseum)
        }

        binding.parkCard.setOnClickListener {
            isLiftedPark = !isLiftedPark
            animateCardView(binding.parkCard, isLiftedPark)
        }

        binding.wildlifeCard.setOnClickListener {
            isLiftedWildlife = !isLiftedWildlife
            animateCardView(binding.wildlifeCard, isLiftedWildlife)
        }
    }

    private fun animateCardView(button: CardView, isLifted: Boolean) {
        val scaleFactor = if (isLifted) 1.1f else 1.0f
        button.animate()
            .scaleX(scaleFactor)
            .scaleY(scaleFactor)
            .setDuration(100)
            .start()


        val backgroundColor =
            if (isLifted) R.color.lifted_card_background else R.color.default_card_background
        button.setBackgroundColor(ContextCompat.getColor(this, backgroundColor))
    }
}