package com.gmail_bssushant2003.journeycraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.databinding.ActivityDestinationListBinding

class DestinationListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDestinationListBinding
    private lateinit var itemsList: ArrayList<Items>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //change status bar color to white
        window.statusBarColor = resources.getColor(R.color.white, theme)

        itemsList = arrayListOf()

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        for(i in  1..30){
            var item = Items(R.drawable.beach, "Curabitur Beach", "Rome, Italy")
            if(i % 2 == 0)
                item = Items(R.drawable.mountain, "Mount Everest", "India")
            itemsList.add(item)
        }

        val myAdapter = MyAdapter(this, itemsList)
        binding.recyclerView.adapter = myAdapter

    }
}