package com.perevodchik.bubblemeet.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.tabs.TabLayout
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.util.dpToPx
import com.perevodchik.bubblemeet.util.margin

class UserFullFragment(_userData: UserData): Fragment() {
    private val userData: UserData = _userData
    private val presenter = UserFullPresenter(this)
    private var tabLayoutProfile: TabLayout? = null
    private var biographyTextView: TextView? = null
    private var nameTextView: TextView? = null
    private var oldAndCityTextView: TextView? = null
    private var imagesCarousel: ViewPager? = null
    private var layout: FlexboxLayout? = null
    private val list = arrayListOf<String>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile_full, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayoutProfile = view.findViewById(R.id.tab_layout)
        biographyTextView = view.findViewById(R.id.profile_full_biography)
        nameTextView = view.findViewById(R.id.profile_full_name)
        oldAndCityTextView = view.findViewById(R.id.profile_full_age_city)
        imagesCarousel = view.findViewById(R.id.user_photos_carousel)
        layout = view.findViewById(R.id.profile_full_stats)

        biographyTextView?.text = userData.hobbes
        nameTextView?.text = userData.name
        oldAndCityTextView?.text = "${userData.age}, ${userData.city}"

        imagesCarousel?.adapter = ProfileImageCarouselAdapter(userData, context!!)
        tabLayoutProfile?.setupWithViewPager(imagesCarousel!!)

        list.add(userData.looking)
        list.add(if(userData.smoking == 1) "Smoking" else "No smoking")
        list.add(if(userData.height > 0) userData.height.toString() else "")
        list.add(userData.eyeColor)
        list.add(if(userData.marred == 1) "Married" else "Not married")
        list.add(if(userData.smoking == 1) "Have children" else "No children")
        list.add(if(userData.smoking == 1) "Love to cook" else "No cooking")

        for(s in list) {
            if(s.isNullOrEmpty())
                continue
            val t = TextView(context!!)
            t.text = s
            t.textSize = 14.0F
            t.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite))
            t.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            t.setPaddingRelative(context!!.dpToPx(20.0F), context!!.dpToPx(10.0F), context!!.dpToPx(20.0F), context!!.dpToPx(10.0F))
            t.margin(left = 5F, right = 5F, top = 5F)
            t.background = ContextCompat.getDrawable(context!!, R.drawable.button_white)
            layout?.addView(t)
        }

        presenter.addHistory(userData.id)
    }
}