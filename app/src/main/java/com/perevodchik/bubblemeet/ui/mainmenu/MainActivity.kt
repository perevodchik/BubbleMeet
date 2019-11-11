package com.perevodchik.bubblemeet.ui.mainmenu

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.mainmenu.bubble.BubbleFragment


class MainActivity : AppCompatActivity() {
    private lateinit var presenter: MainPresenter

    companion object {
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, BubbleFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
