package com.gmail_bssushant2003.journeycraft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.MyAdapter
import com.gmail_bssushant2003.journeycraft.Models.Items
import com.gmail_bssushant2003.journeycraft.databinding.ActivityDestinationListBinding

class DestinationListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDestinationListBinding
    private lateinit var itemsList: ArrayList<Items>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinationListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemsList = arrayListOf()

        //change status bar color to white
        window.statusBarColor = resources.getColor(R.color.white, theme)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        itemsList = arrayListOf(
            Items(R.drawable.kolhapur, "Kolhapur", "Maharashtra, India"),
            Items(R.drawable.goa, "Goa", "Goa, India"),
            Items(R.drawable.ooty, "Ooty", "Tamil Nadu, India"),
            Items(R.drawable.mahabaleshwar, "Mahabaleshwar", "Maharashtra, India"),
            Items(R.drawable.lakshadweep, "Lakshadweep", "Lakshadweep , India"),
            Items(R.drawable.manali, "Manali", "Himachal Pradesh, India"),
        )

//        for(i in  1..30){
//            var item = Items(R.drawable.beach, "Curabitur Beach", "Rome, Italy")
//            if(i % 2 == 0)
//                item = Items(R.drawable.mountain, "Mount Everest", "India")
//            itemsList.add(item)
//        }

        val myAdapter = MyAdapter(this, itemsList)
        binding.recyclerView.adapter = myAdapter

        myAdapter.setOnClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@DestinationListActivity, MainActivity::class.java)
                intent.putExtra("individualDestination", itemsList[position])
                startActivity(intent)
            }
        })
    }
}