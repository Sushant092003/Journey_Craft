package com.gmail_bssushant2003.journeycraft

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.gmail_bssushant2003.journeycraft.Models.Items
import com.gmail_bssushant2003.journeycraft.Transport.TransportActivity
import com.gmail_bssushant2003.journeycraft.databinding.ActivityMainBinding
import com.google.maps.model.LatLng

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var isLiftedVeg = false
    private var isLiftedNonVeg = false
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var latLng : LatLng = LatLng(16.691307, 74.244865)

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

        if(individualDestination == "Kolhapur"){
            latLng = LatLng(16.691307, 74.244865)
        }
        else if(individualDestination == "Goa"){
            latLng = LatLng(15.496777, 73.827827)
        }
        else if(individualDestination == "Ooty"){
            latLng = LatLng(11.410000,76.699997)
        }
        else if(individualDestination == "Mahabaleshwar"){
            latLng = LatLng(17.921721, 73.655602)
        }
        else if(individualDestination == "Lakshadweep"){
            latLng = LatLng(11.7056501,72.7152889)
        }
        else if(individualDestination == "Manali"){
            latLng = LatLng(32.239632,77.188713)
        }

        binding.availableTransport.setOnClickListener {
            val intent = Intent(this, TransportActivity::class.java)
            intent.putExtra("destinationLat", latLng.lat)
            intent.putExtra("destinationLng", latLng.lng)
            startActivity(intent)
        }

    }
}