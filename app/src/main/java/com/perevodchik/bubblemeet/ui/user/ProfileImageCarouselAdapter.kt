package com.perevodchik.bubblemeet.ui.user

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.util.Values
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class ProfileImageCarouselAdapter(_userData: UserData, _ctx: Context): PagerAdapter() {
    private val userData = _userData
    private val context = _ctx
    private val display by lazy { (context as Activity).windowManager.defaultDisplay }
    private var size: Point = Point()

    override fun getCount(): Int {
        var photos = 0
        if(userData.photo.isNullOrEmpty())
            return photos
        photos += userData.photo.size
        return photos
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ImageView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView: ImageView
        display.getSize(size)
        if (position == 0) {
            imageView = ImageView(context)
            Picasso.with(context).load("${Values.imgUrl}/${userData.avatarFull}").fit().centerCrop().into(imageView)
        } else {
            imageView = ImageView(context)
            Picasso.with(context).load("${Values.imgUrl}/${userData.photo[position - 1]}").fit().centerCrop().into(imageView)
        }
        (container as ViewPager).addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }


}