package com.perevodchik.bubblemeet.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng


class GpsTracker(_ctx: Context) : LocationListener {
    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    private val context: Context = _ctx
    // flag for GPS status
    private var isGPSEnabled = false
    // flag for network status
    private var isNetworkEnabled = false
    // flag for GPS status
    private var canGetLocation = false
    private var location: Location? = null // location
    private var latitude: Double = 0.toDouble() // latitude
    private var longitude: Double = 0.toDouble() // longitude
    // Declaring a Location Manager
    lateinit var locationManager: LocationManager

    companion object {
        // The minimum distance to change Updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 100 // 10 meters
        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute
    }

    fun getLocation(): Location? {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            Log.d("gps", "$isGPSEnabled")
            Log.d("network", "$isNetworkEnabled")

            if (!isGPSEnabled && !isNetworkEnabled) {
                return null
            } else {
                canGetLocation = true
                if (isNetworkEnabled) {
                    Log.d("Network", "Enabled")
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            101
                        )
                    }
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                    )
                    Log.d("Network", "Network")
                    location =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null) {
                        latitude = location?.latitude ?: 0.0
                        longitude = location?.longitude ?: 0.0
                    }

                }
                if (isGPSEnabled) {
                    if (location == null) {
                        Log.d("location", "null")
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                context as Activity,
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ),
                                101
                            )
                        }
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                        )
                        Log.d("GPS Enabled", "GPS Enabled")
                        location =
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (location != null) {
                            latitude = location?.latitude ?: 0.0
                            longitude = location?.longitude ?: 0.0
                        }

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    fun stopUsingGPS() {
        locationManager.removeUpdates(this@GpsTracker)
    }

    private fun getLatitude(): Double {
        latitude = location?.latitude ?: 0.0
        return latitude
    }

    private fun getLongitude(): Double {
        longitude = location?.longitude ?: 0.0
        return longitude
    }

    private fun canGetLocation(): Boolean {
        return canGetLocation
    }

    fun getLatLng(): LatLng {
        return if (canGetLocation()) LatLng(getLatitude(), getLongitude()) else LatLng(0.0, 0.0)
    }
}