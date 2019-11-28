package com.perevodchik.bubblemeet.ui.preload

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.Location

class PreloadActivity: AppCompatActivity() {
    private lateinit var presenter: PreloadPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preload)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            101
        )
        Location.initTracker(this)
        presenter = PreloadPresenter(this)
    }

}