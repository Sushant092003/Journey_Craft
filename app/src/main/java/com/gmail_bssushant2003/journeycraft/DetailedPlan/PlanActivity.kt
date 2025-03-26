package com.gmail_bssushant2003.journeycraft.DetailedPlan

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail_bssushant2003.journeycraft.Adapters.MyAdapter
import com.gmail_bssushant2003.journeycraft.Adapters.PlacesAdapter
import com.gmail_bssushant2003.journeycraft.Constants.ApiConstants
import com.gmail_bssushant2003.journeycraft.Models.TripRecord
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityPlanBinding
import com.google.firebase.database.FirebaseDatabase
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import androidx.core.net.toUri

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


        //check if it is an active plan
        val tripData = intent.getStringExtra("tripData")
        if(tripData != null){

            var cnt = 0

            for (item in tripData.split(";")) {
                cnt++
                if(cnt % 2 != 0) placesList.add(item.trim())
                else timeList.add(item.trim())
            }

            updateUI(placesList, timeList)
        }
        else{
            tempList = arrayListOf("Ramtirth Waterfall", "Gaganbawada", "New Palace", "Dajipur Wildlife Sanctuary", "Panhala Fort", "Mahalaximi temple", "Jyotiba temple", "KIT College, Kolhapur", "DYP city mall")
            callAPI(place.toString(), startTime!!, endTime!!)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        myAdapter = PlacesAdapter(this, placesList)
//        binding.recyclerView.adapter = myAdapter



        //help page
        binding.imageViewHelp.setOnClickListener {
            startActivity(Intent(this, ChatBotActivity::class.java))
        }

    }

    private fun goInMap(placeList : ArrayList<String>) {
        
        var loc : String = ""

        for(i in placeList){
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

        val progressBar = ProgressDialog(this@PlanActivity).apply {
            setMessage("Loading...")
            setCancelable(false)
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
        }

        val baseUrl = "${ApiConstants.showDetailedPlanApiUrl}?startloc=$place&starttime=$st&endtime=$et"

        val request = Request.Builder()
            .url(baseUrl)
            .get()
            .build()

//        progressBar.show()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Gaurav", "1]failed to load response due to ${e.message}")
//                progressDialog.dismiss()

                updateUI(tempList, timeList)
            }

            override fun onResponse(call: Call, response: Response) {
//                progressBar.hide()
                if(response.isSuccessful){
                    try {
                        val responseBody = response.body?.string()
                        var cnt = 0

                        //store response in firebase
                        storeResponseInFirebase(responseBody!!)

                        Log.d("Adarsh", responseBody.toString())

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

                        updateUI(finalPlacesList, timeList)

                    }
                    catch (e : Exception){
                        progressBar.hide()
                        e.printStackTrace()
                    }
                }
                else{
                    val responseBody = response.body?.string()
                    progressBar.hide()
                    Log.d("Gaurav", "Failed to load response due to: $responseBody")
                }
            }

            private fun storeResponseInFirebase(response: String) {
                val recordFile = getSharedPreferences("records", MODE_PRIVATE)
                val phoneNumber = recordFile.getString("phoneNumber", "")

                val database = FirebaseDatabase.getInstance()
                val tripRef = database.getReference("trips").child("$phoneNumber")

                val customUid = UUID.randomUUID().toString()
                val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                    Date()
                )

                // Create a TripRecord object
                val tripRecord = TripRecord(response, currentDateTime)

                tripRef.child(customUid).setValue(tripRecord)
            }


        })

    }

    fun updateUI(placeList : ArrayList<String>, timeList : ArrayList<String>) {
        runOnUiThread {
            myAdapter = PlacesAdapter(this@PlanActivity, placeList, timeList)
            binding.recyclerView.adapter = myAdapter

            // by map logo
            binding.mapLogo.setOnClickListener {
                goInMap(placeList)
            }

            myAdapter.setOnClickListener(object : MyAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val gmmIntentUri1 =
                        "https://www.google.com/maps/dir/?api=1&destination=${placeList[position]}".toUri()

                    val mapIntent1 = Intent(Intent.ACTION_VIEW, gmmIntentUri1)

                    mapIntent1.`package` = "com.google.android.apps.maps"

                    if (mapIntent1.resolveActivity(packageManager) != null) {
                        startActivity(mapIntent1)
                    } else {
                        Toast.makeText(
                            this@PlanActivity,
                            "Google Maps app is not installed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }
    }
}