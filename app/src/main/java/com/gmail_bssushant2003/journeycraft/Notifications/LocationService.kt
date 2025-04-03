package com.gmail_bssushant2003.journeycraft.Notifications

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.gmail_bssushant2003.journeycraft.Constants.ApiConstants
import com.gmail_bssushant2003.journeycraft.Constants.ApiService
import com.gmail_bssushant2003.journeycraft.Models.LatLng
import com.google.android.gms.location.*
import com.gmail_bssushant2003.journeycraft.Models.StreetLocation
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val lastNotificationTimes = mutableMapOf<String, Long>()
    private val apiService: ApiService

    private var lastKnownLocation: Location? = null // 🔹 Store last known location

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.nearbyRestGuideApiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (shouldFetchNewLocations(location)) {
                        fetchNearbyLocations(location)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification("Tracking location..."))
        startLocationTracking()
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun startLocationTracking() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            stopSelf()
            return
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000 // Updates every 5 seconds
        ).setMinUpdateIntervalMillis(2000)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            ContextCompat.getMainExecutor(this),
            locationCallback
        )
    }

    private fun shouldFetchNewLocations(location: Location): Boolean {
        lastKnownLocation?.let {
            val distance = it.distanceTo(location) // 🔹 Calculate distance
            if (distance < 500) { // 🔹 Check if user moved 500m
                Log.d("LocationService", "User moved only $distance meters. Skipping API call.")
                return false
            }
        }

        lastKnownLocation = location // 🔹 Update last known location
        return true
    }

    private fun fetchNearbyLocations(location: Location) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val latLng = LatLng(location.latitude, location.longitude)
                val response = apiService.getNearbyLocations(latLng).execute() // 🔹 Synchronous call

                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        checkProximity(location, response.body()!!)
                    }
                } else {
                    Log.e("LocationService", "API call failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("LocationService", "API request error: ${e.message}")
            }
        }
    }

    private fun checkProximity(location: Location, places: List<StreetLocation>) {
        val notifiedPlaces = mutableListOf<String>()

        for (place in places) {
            val distance = FloatArray(1)
            Location.distanceBetween(
                location.latitude, location.longitude, place.lat, place.lng, distance
            )

            if (distance[0] < 500) { // ✅ Within 0.5 km
                sendNotification("You're near ${place.name}!", "Tap to navigate", place.lat, place.lng)
                notifiedPlaces.add(place.name)
            }
        }

        if (notifiedPlaces.isNotEmpty()) {
            Log.d("LocationService", "Notifications sent for: ${notifiedPlaces.joinToString()}")
        }
    }


    private fun sendNotification(title: String, message: String, lat: Double, lng: Double) {
        val uri = "geo:$lat,$lng?q=$lat,$lng($title)".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "LOCATION_CHANNEL")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(title.hashCode(), notification) // Unique ID per location
    }

    private fun createNotification(text: String): Notification {
        return NotificationCompat.Builder(this, "LOCATION_CHANNEL")
            .setContentTitle("Location Tracking")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "LOCATION_CHANNEL", "Location Updates", NotificationManager.IMPORTANCE_HIGH
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
