package com.perevodchik.bubblemeet.ui.preload

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perevodchik.bubblemeet.R

class PreloadActivity: AppCompatActivity() {
    private lateinit var presenter: PreloadPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preload)
        presenter = PreloadPresenter(this)
    }

}