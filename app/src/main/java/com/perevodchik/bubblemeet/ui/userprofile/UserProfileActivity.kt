package com.perevodchik.bubblemeet.ui.userprofile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.ui.preload.PreloadActivity
import com.perevodchik.bubblemeet.util.Values
import com.perevodchik.bubblemeet.util.dpToPx
import com.perevodchik.bubblemeet.util.margin
import com.squareup.picasso.Picasso
import java.lang.Exception

class UserProfileActivity : AppCompatActivity() {
    private lateinit var flex: FlexboxLayout
    private val list = arrayListOf<String>()
    private val profile = UserInstance.profile
    private lateinit var logout: ImageView
    private lateinit var s: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        s = getSharedPreferences("BubbleMeet", Context.MODE_PRIVATE)
        flex = findViewById(R.id.profile_user_stats)
        logout = findViewById(R.id.btn_logout)
        logout.setOnClickListener {
            val e = s.edit()

            e.remove("session")
            e.remove("id")
            e.remove("password")
            e.remove(NotificationCompat.CATEGORY_EMAIL)
            e.apply()

            startActivity(Intent(this, PreloadActivity::class.java))
        }

        list.add(profile.looking)
        list.add(if(profile.smoking == 1) "Smoking" else if(profile.smoking == 0) "No smoking" else "")
        list.add(profile.height.toString())
        list.add(profile.eyeColor)
        list.add(if(profile.marred == 1) "Married" else if(profile.smoking == 0) "Not married" else "")
        list.add(if(profile.smoking == 1) "Have children" else if(profile.smoking == 0) "No children" else "")
        list.add(if(profile.smoking == 1) "Love to cook" else if(profile.smoking == 0) "No cooking" else "")

        findViewById<TextView>(R.id.profile_user_name).text = (profile.name + " " + profile.last_name)
        findViewById<TextView>(R.id.profile_biography).text = profile.hobbes
        val text = profile.age.toString()
        if(profile.city.isNotEmpty()) text.plus(", " + profile.city)
        findViewById<TextView>(R.id.profile_year_country).text = (text)

        for(s in list) {
            if(s.isEmpty() || s == "-1")
                continue
            val t = TextView(this)
            t.text = s
            t.textSize = 14.0F
            t.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            t.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            t.setPaddingRelative(dpToPx(20.0F), dpToPx(10.0F), dpToPx(20.0F), dpToPx(10.0F))
            t.margin(left = 5F, right = 5F, top = 5F)
            t.background = ContextCompat.getDrawable(this, R.drawable.button_white)
            flex.addView(t)
        }

        findViewById<ImageView>(R.id.btn_back_from_profile).setOnClickListener {
            this.finish()
        }

        loadImg("${Values.imgUrl}/${profile.avatarFull}", R.id.profile_user_avatar)

        try {
            loadImg("${Values.imgUrl}/${profile.photo[0]}", R.id.user_photo_0)
            loadImg("${Values.imgUrl}/${profile.photo[1]}", R.id.user_photo_1)
            loadImg("${Values.imgUrl}/${profile.photo[2]}", R.id.user_photo_2)
        } catch (ex: RuntimeException) {}
    }

    private fun loadImg(img: String, view: Int) {
        try {
            Picasso.with(this).load(img).into(findViewById<ImageView>(view))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
