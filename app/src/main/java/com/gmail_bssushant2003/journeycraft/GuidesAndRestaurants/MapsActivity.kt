package com.gmail_bssushant2003.journeycraft.GuidesAndRestaurants

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gmail_bssushant2003.journeycraft.Fragments.GuideDetailsDialogFragment
import com.gmail_bssushant2003.journeycraft.Fragments.GuidesFragment.RetrofitClient
import com.gmail_bssushant2003.journeycraft.Models.Guide
import com.gmail_bssushant2003.journeycraft.R
import com.gmail_bssushant2003.journeycraft.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityMapsBinding
    private lateinit var googleMap: GoogleMap
    private var guideList = ArrayList<Guide>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.white, theme)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.guide_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val placeLatLng = intent.getParcelableExtra<LatLng>("placeLatLng")
        googleMap.addMarker(MarkerOptions().position(placeLatLng!!).title("Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 15f))

        Log.d("Gaurav", "Location ${placeLatLng}")

        val placeLatLngList : ArrayList<com.gmail_bssushant2003.journeycraft.Models.LatLng> = arrayListOf(com.gmail_bssushant2003.journeycraft.Models.LatLng(placeLatLng.latitude, placeLatLng.longitude))

        sendLocationsToServer(placeLatLngList)
    }

    private fun addPinToLocation(guide: Guide) {
        val location = LatLng(guide.latitude, guide.longitude)


        // Create a custom green marker
        val markerOptions = MarkerOptions()
            .position(location)
            .icon(resizeMarker(this,R.drawable.ic_guide_icon,120,120))  // Green Pin

        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        val marker = googleMap.addMarker(markerOptions)
        marker?.tag = guide

        googleMap.setOnMarkerClickListener { marker ->
            val guide = marker.tag as? Guide
            guide?.let {
                showGuideDetailsDialog(it)
            }
            true  // Return true to consume the click event
        }
    }

    private fun showGuideDetailsDialog(guide: Guide) {
        val dialog = GuideDetailsDialogFragment(guide)
        dialog.show(supportFragmentManager, "GuideDetailsDialog")
    }


    private fun sendLocationsToServer(placesLatLngList: ArrayList<com.gmail_bssushant2003.journeycraft.Models.LatLng>?) {
        RetrofitClient.apiService.findNearbyGuides(placesLatLngList!!).enqueue(object :
            Callback<List<Guide>> {
            override fun onResponse(call: Call<List<Guide>>, response: retrofit2.Response<List<Guide>>) {
                if (response.isSuccessful) {
                    val fetchedData = response.body() ?: emptyList()
                    guideList.addAll(fetchedData)
                } else {
                    Log.e("ResponseError", "Error: ${response.errorBody()?.string()}")
                }

                for (guide in guideList) {
                    Log.d("Gaurav", "Guide Name: ${guide.name}")
                    addPinToLocation(guide)
                }
            }

            override fun onFailure(call: Call<List<Guide>>, t: Throwable) {
                Log.e("NetworkError", "Failed: ${t.message}")
            }
        })
    }

    fun resizeMarker(context: Context, drawableRes: Int, width: Int, height: Int): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(context, drawableRes) ?: return BitmapDescriptorFactory.defaultMarker()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}