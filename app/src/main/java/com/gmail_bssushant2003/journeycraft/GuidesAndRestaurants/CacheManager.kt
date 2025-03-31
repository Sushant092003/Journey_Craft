package com.gmail_bssushant2003.journeycraft.GuidesAndRestaurants

import android.content.Context
import android.content.SharedPreferences
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gmail_bssushant2003.journeycraft.Models.Restaurant
import androidx.core.content.edit

object CacheManager {

    private const val PREF_NAME = "JourneyCraftCache"
    private const val RESTAURANT_KEY = "cached_restaurants"
    private const val GUIDE_KEY = "cached_guides"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveRestaurants(context: Context, restaurantList: List<Restaurant>) {
        val json = Gson().toJson(restaurantList)
        getPreferences(context).edit() { putString(RESTAURANT_KEY, json) }
    }

    fun getCachedRestaurants(context: Context): List<Restaurant>? {
        val json = getPreferences(context).getString(RESTAURANT_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Restaurant>>() {}.type
            Gson().fromJson(json, type)
        } else {
            null
        }
    }

    fun saveGuides(context: Context, guidesList: List<Guide>) {
        val json = Gson().toJson(guidesList)
        getPreferences(context).edit() { putString(GUIDE_KEY, json) }
    }

    fun getCachedGuides(context: Context): List<Guide>? {
        val json = getPreferences(context).getString(GUIDE_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Guide>>() {}.type
            Gson().fromJson(json, type)
        } else {
            null
        }
    }
}
