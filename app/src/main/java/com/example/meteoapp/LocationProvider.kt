package com.example.meteoapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*

class LocationProvider(private val context: Context, private val onLocationReceived: (String) -> Unit) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLastKnownLocation()
        }
    }

    fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    getCityFromLocation(location)
                } else {
                    requestNewLocationData()
                }
            }
        }
    }

    private fun requestNewLocationData() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            getCityFromLocation(lastLocation)
        }
    }

    private fun getCityFromLocation(location: Location) {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val cityName = addresses[0].locality
                onLocationReceived(cityName)
            }
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
