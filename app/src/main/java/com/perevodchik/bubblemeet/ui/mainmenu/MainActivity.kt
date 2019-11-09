package com.perevodchik.bubblemeet.ui.mainmenu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.mainmenu.bubble.BubbleFragment

class MainActivity : AppCompatActivity() {
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)

        if (savedInstanceState == null) {
            //val fragment = BubbleFragment0()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, BubbleFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
