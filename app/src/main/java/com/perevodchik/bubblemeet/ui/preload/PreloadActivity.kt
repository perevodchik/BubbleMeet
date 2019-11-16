package com.perevodchik.bubblemeet.ui.preload

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.Location

class PreloadActivity: AppCompatActivity() {
    private lateinit var presenter: PreloadPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preload)
        Location.initTracker(this)
        presenter = PreloadPresenter(this)
    }

}