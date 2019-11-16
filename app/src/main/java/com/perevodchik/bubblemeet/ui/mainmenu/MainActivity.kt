package com.perevodchik.bubblemeet.ui.mainmenu

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.ui.mainmenu.bubble.BubbleFragment
import com.perevodchik.bubblemeet.ui.mainmenu.presenter.LikesPresenter
import com.perevodchik.bubblemeet.util.Location
import com.perevodchik.bubblemeet.util.UserInstance

class MainActivity : AppCompatActivity() {
    private lateinit var presenter: MainPresenter
    var flag = 0

    companion object {
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("code", "request -> $requestCode !!! result -> $resultCode")

        val str = data?.getStringExtra("filter") ?: "-str"
        Log.d("filter string ->", str)
        UserInstance.addFilters(str)

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)

        LikesPresenter(null).fetchLikes(UserInstance.userLikes)

        if (savedInstanceState == null) {
            val fragment = BubbleFragment()
            //fragment.setUsers(UserInstance.allUsers)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
