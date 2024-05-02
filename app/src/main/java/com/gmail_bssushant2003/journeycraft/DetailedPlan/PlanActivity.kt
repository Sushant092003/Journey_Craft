package com.gmail_bssushant2003.journeycraft.DetailedPlan

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.MyAdapter
import com.gmail_bssushant2003.journeycraft.Adapters.PlacesAdapter
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityPlanBinding

class PlanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlanBinding
    private lateinit var placesList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.white, theme)

        placesList = arrayListOf("Ramtirth Waterfall", "Gaganbawada", "New Palace", "Dajipur Wildlife Sanctuary", "Panhala Fort", "Mahalaximi temple", "Jyotiba temple", "KIT's College of Engineering", "DYP city mall", "Kanerimath")


        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val myAdapter = PlacesAdapter(this, placesList)
        binding.recyclerView.adapter = myAdapter


        Log.d("Gaurav", placesList.size.toString())


    }
}