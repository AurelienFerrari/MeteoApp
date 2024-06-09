    package com.example.meteoapp

    import android.Manifest
    import android.app.Activity
    import android.content.Context
    import android.content.pm.PackageManager
    import android.location.Location
    import android.location.LocationListener
    import android.location.LocationManager
    import android.widget.Toast
    import androidx.core.app.ActivityCompat

    class LocationProvider(private val context: Context, private val onLocationReceived: (String) -> Unit) {
        companion object {
            const val LOCATION_PERMISSION_REQUEST_CODE = 1
        }

        private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        fun checkLocationPermission() {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                getLastKnownLocation()
            }
        }

        fun getLastKnownLocation() {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                location?.let {
                    onLocationReceived(getCityNameFromLocation(it))
                }
            }
        }

        private fun getCityNameFromLocation(location: Location): String {
            // Remplacez cette méthode par l'implémentation réelle pour obtenir le nom de la ville à partir des coordonnées de localisation.
            return "CityName"
        }
    }
