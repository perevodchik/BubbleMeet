package com.perevodchik.bubblemeet.ui.user

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.util.Values
import com.squareup.picasso.Picasso

class ProfileImageCarouselAdapter(_userData: UserData, _ctx: Context): PagerAdapter() {
    private val userData = _userData
    private val context = _ctx

    override fun getCount(): Int {
        var photos = 1
        if(userData.photo.isNotEmpty())
            photos += userData.photo.size
        return photos
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ImageView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView: ImageView
        if (position == 0) {
            imageView = ImageView(context)
            Picasso.with(context).load("${Values.imgUrl}/${userData.avatarFull}").into(imageView)
        } else {
            imageView = ImageView(context)
            Picasso.with(context).load("${Values.imgUrl}/${userData.photo[position - 1]}").into(imageView)
        }
        (container as ViewPager).addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }


}