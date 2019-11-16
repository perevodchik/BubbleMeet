package com.perevodchik.bubblemeet.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng

class Location {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit  var gpsTracker: GpsTracker

        fun initTracker(activity: Activity) {
            gpsTracker = GpsTracker(activity)
        }

        fun getCoordinates(): Location? {
            return gpsTracker.getLocation()
        }
    }

    data class Coordinates(var lat: Double, var lng: Double) {
        override fun toString(): String {
            return "$lat, $lng"
        }
    }
}