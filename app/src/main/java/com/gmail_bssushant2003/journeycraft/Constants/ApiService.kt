package com.gmail_bssushant2003.journeycraft.Constants

import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.Models.LatLng
import com.gmail_bssushant2003.journeycraft.Models.Restaurant
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/public/find-guides")
    fun findNearbyGuides(@Body locations: List<LatLng>): Call<List<Guide>>


    @POST("/public/find-restaurants")
    fun findNearbyRestaurants(@Body locations: List<LatLng>): Call<List<Pair<LatLng, Restaurant>>>
}