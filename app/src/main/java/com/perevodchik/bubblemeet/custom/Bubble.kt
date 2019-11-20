package com.perevodchik.bubblemeet.custom

import android.view.View
import com.perevodchik.bubblemeet.data.AnimateWrapper
import com.perevodchik.bubblemeet.data.model.UserData

class Bubble(u: UserData, v: View) {
    val userData: UserData = u
    val view: View = v
    val animateWrapper: AnimateWrapper = AnimateWrapper(view)
    var scaleSize: Float = 1.0f
}