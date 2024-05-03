package com.gmail_bssushant2003.journeycraft.DetailedPlan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.MyAdapter
import com.gmail_bssushant2003.journeycraft.Adapters.PlacesAdapter
import com.gmail_bssushant2003.journeycraft.MainActivity
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityPlanBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.commons.lang3.ObjectUtils.Null
import java.io.IOException

class PlanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlanBinding
    private lateinit var placesList : ArrayList<String>
    private lateinit var timeList : ArrayList<String>
    private lateinit var finalPlacesList : ArrayList<String>
    private lateinit var tempList : ArrayList<String>
    private lateinit var myAdapter : PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        placesList = arrayListOf()
        finalPlacesList = arrayListOf()
        tempList = arrayListOf()
        timeList = arrayListOf()

        window.statusBarColor = resources.getColor(R.color.white, theme)

        //take data from intent
        val place = intent.getIntExtra("Place", 0)
        val startTime = intent.getStringExtra("StartTime")
        val endTime = intent.getStringExtra("EndTime")

        tempList = arrayListOf("Ramtirth Waterfall", "Gaganbawada", "New Palace", "Dajipur Wildlife Sanctuary", "Panhala Fort", "Mahalaximi temple", "Jyotiba temple", "KIT's College of Engineering, Kolhapur", "DYP city mall", "Kanerimath")
        callAPI(place.toString(), startTime!!, endTime!!)


        for(i in placesList) Log.d("Gaurav", i.toString())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        myAdapter = PlacesAdapter(this, placesList)
//        binding.recyclerView.adapter = myAdapter


        // by map logo
        binding.mapLogo.setOnClickListener {
            goInMap()
        }

        //help page
        binding.imageViewHelp.setOnClickListener {
            startActivity(Intent(this, ChatBotActivity::class.java))
        }

    }

    private fun goInMap() {
        
        var loc : String = ""

        for(i in finalPlacesList){
            loc = "$loc$i/"
        }

//        Log.d("GGGGGG", loc)

        val gmmIntentUri1 = Uri.parse("https://www.google.com/maps/dir/$loc")

        val mapIntent1 = Intent(Intent.ACTION_VIEW, gmmIntentUri1)

        mapIntent1.`package` = "com.google.android.apps.maps"

        if (mapIntent1.resolveActivity(packageManager) != null) {
            startActivity(mapIntent1)
        } else {
            Toast.makeText(this, "Google Maps app is not installed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun callAPI(place: String, st: String, et: String) {

        val client = OkHttpClient()

        Log.d("Sushant", place)
        Log.d("Sushant", st)
        Log.d("Sushant", et)

        val baseUrl = "http://192.168.104.85:5000/data?startloc=$place&starttime=$st&endtime=$et"

        val request = Request.Builder()
            .url(baseUrl)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Gaurav", "1]failed to load response due to ${e.message}")
//                progressDialog.dismiss()
                runOnUiThread {
                    myAdapter = PlacesAdapter(this@PlanActivity, tempList, timeList)
                    binding.recyclerView.adapter = myAdapter
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    try {
                        val responseBody = response.body?.string()
                        var cnt = 0

                        if (responseBody != null) {
                            for (item in responseBody.split(";")) {
                                cnt++
                                if(cnt % 2 != 0) placesList.add(item.trim())
                                else timeList.add(item.trim())
                            }
                        }


                        for(i in placesList){
                            if(i == "" || i == "Kolhapur") continue
                            finalPlacesList.add(i)
                        }

                        runOnUiThread {
                            myAdapter = PlacesAdapter(this@PlanActivity, finalPlacesList, timeList)
                            binding.recyclerView.adapter = myAdapter

                            myAdapter.setOnClickListener(object : MyAdapter.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val gmmIntentUri1 = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${placesList[position]}")

                                    val mapIntent1 = Intent(Intent.ACTION_VIEW, gmmIntentUri1)

                                    mapIntent1.`package` = "com.google.android.apps.maps"

                                    if (mapIntent1.resolveActivity(packageManager) != null) {
                                        startActivity(mapIntent1)
                                    } else {
                                        Toast.makeText(this@PlanActivity, "Google Maps app is not installed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            })
                        }

                    }
                    catch (e : Exception){
                        e.printStackTrace()
                    }
                }
                else{
                    val responseBody = response.body?.string()
                    Log.d("Gaurav", "Failed to load response due to: $responseBody")
                }
            }

        })

    }
}