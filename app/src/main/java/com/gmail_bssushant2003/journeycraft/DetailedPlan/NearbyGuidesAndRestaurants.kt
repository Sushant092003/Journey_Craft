package com.gmail_bssushant2003.journeycraft.DetailedPlan

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gmail_bssushant2003.journeycraft.Constants.ApiConstants.nearbyRestGuideApiUrl
import com.gmail_bssushant2003.journeycraft.Constants.ApiService
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.Models.LatLng
import okhttp3.OkHttpClient
import com.gmail_bssushant2003.journeycraft.databinding.ActivityNearbyGuidesAndRestaurantsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NearbyGuidesAndRestaurants : AppCompatActivity() {

    private lateinit var binding : ActivityNearbyGuidesAndRestaurantsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearbyGuidesAndRestaurantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //receive latLng list from intent
        val placesLatLngList = intent.getSerializableExtra("placesLatLngList") as? ArrayList<LatLng>



        binding.buttonSubmit.setOnClickListener {
            sendLocationsToServer(placesLatLngList)
        }
    }

    private fun sendLocationsToServer(placesLatLngList: ArrayList<LatLng>?) {
        RetrofitClient.apiService.findNearbyGuides(placesLatLngList!!).enqueue(object :
            Callback<List<Guide>> {
            override fun onResponse(call: Call<List<Guide>>, response: retrofit2.Response<List<Guide>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        Log.d("Gaurav", it.name!!)
                    }
                } else {
                    Log.e("ResponseError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Guide>>, t: Throwable) {
                Log.e("NetworkError", "Failed: ${t.message}")
            }


        })
    }

    object RetrofitClient {
        private var BASE_URL = nearbyRestGuideApiUrl

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
        }

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}