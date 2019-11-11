package com.perevodchik.bubblemeet.util

import android.app.Activity
import android.app.Service
import android.location.LocationManager
import androidx.core.app.ActivityCompat

class Location {

    companion object {

        fun getCoordinates(activity: Activity): Coordinates? {
            val lm = activity.getSystemService(Service.LOCATION_SERVICE) as LocationManager
            if (
                ActivityCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") != 0
                && ActivityCompat.checkSelfPermission(activity, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                return null
            }
            val location = lm.getLastKnownLocation("network")
            return if (location != null) {
                Coordinates(location.latitude, location.longitude)
            } else null
        }

    }

    data class Coordinates(var lat: Double, var lng: Double) {
        override fun toString(): String {
            return "$lat, $lng"
        }
    }
}