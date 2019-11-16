package com.perevodchik.bubblemeet.custom

import android.view.View
import com.perevodchik.bubblemeet.data.AnimateWrapper
import com.perevodchik.bubblemeet.data.model.UserData

class Bubble(/*u: UserData, */v: View) {
    //val userData: UserData = u
    val view = v
    private val animateWrapper = AnimateWrapper(view)
    var x = 0
    var y = 0
    var scaleMultiplier = 0.0f

    fun startAnimation() {
        animateWrapper.startAnimation()
    }

    fun scale(value: Float) {
        animateWrapper.scale(value)
    }

    fun params() {
        animateWrapper.params()
    }

    fun move(valX: Float, valY: Float, isX: Boolean, isY: Boolean) {
        animateWrapper.setCoordinates(valX, valY)
        animateWrapper.inertialMove(isX, isY)
    }

    fun setY(valY: Float) {
        animateWrapper.setY(valY)
    }

    fun setX(valX: Float) {
        animateWrapper.setX(valX)
    }

}