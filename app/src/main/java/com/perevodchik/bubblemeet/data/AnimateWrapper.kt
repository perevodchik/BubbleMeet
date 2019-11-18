package com.perevodchik.bubblemeet.data

import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

class AnimateWrapper(v: View) {
    private val animateSpringX = SpringAnimation(v, DynamicAnimation.X, v.x)
    private val animateSpringY = SpringAnimation(v, DynamicAnimation.Y, v.y)
    private val animateSpringScaleX = SpringAnimation(v, DynamicAnimation.SCALE_X, 1.0f)
    private val animateSpringScaleY = SpringAnimation(v, DynamicAnimation.SCALE_Y, 1.0f)
    val view = v

    companion object {
        private const val stiffness = 1000.0f
        private const val inertiaMultiplierSmall = 1.0f
        private const val inertiaMultiplier = 1.5f
        private const val inertiaMultiplierBig = 3f
    }

    init {
        val springX = animateSpringX.spring
        val springY = animateSpringY.spring
        val scaleX = animateSpringScaleX.spring
        val scaleY = animateSpringScaleY.spring
        springY.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        springX.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        scaleX.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        scaleY.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
    }

    fun params() {
        val springX = animateSpringX.spring
        val springY = animateSpringY.spring
        val scaleX = animateSpringScaleX.spring
        val scaleY = animateSpringScaleY.spring

        springX.stiffness = stiffness
        springY.stiffness = stiffness
        springY.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        springX.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        scaleX.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        scaleY.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
    }

    fun params(stiffness: Float = 1000.0f) {
        val springX = animateSpringX.spring
        val springY = animateSpringY.spring
        springX.stiffness = stiffness
        springY.stiffness = stiffness
        springY.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        springX.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
    }

    fun inertial(valX: Float, valY: Float) {
        val translationX = animateSpringX.spring
        val translationY = animateSpringY.spring
        val vX = if(valX > 11) view.x + valX * inertiaMultiplier else view.x + valX * inertiaMultiplierSmall
        val vY = if(valY > 11) view.y + valY * inertiaMultiplier else view.y + valY * inertiaMultiplierSmall

        translationX.finalPosition = vX
        translationY.finalPosition = vY
        animateSpringX.start()
        animateSpringY.start()
    }

    fun setCoordinates(valX: Float, valY: Float) {
        val springX = animateSpringX.spring
        val springY = animateSpringY.spring
        springX.finalPosition = valX
        springY.finalPosition = valY
    }

    fun setX(valX: Float) {
        val springX = animateSpringX.spring
        springX.finalPosition = valX
    }

    fun setY(valY: Float) {
        val springY = animateSpringY.spring
        springY.finalPosition = valY
    }

    fun inertialMove(isX: Boolean, isY: Boolean) {
        if (isX) {
            animateSpringX.start()
        }
        if (isY) {
            animateSpringY.start()
        }
    }

    fun scale(value: Float) {
        val scaleX = animateSpringScaleX.spring
        val scaleY = animateSpringScaleY.spring
        scaleX.finalPosition = value
        scaleY.finalPosition = value
    }

    fun startMoveAnimation() {
        animateSpringX.start()
        animateSpringY.start()
    }

    fun startScaleAnimation() {
        animateSpringScaleX.start()
        animateSpringScaleY.start()
    }

    fun startAnimation() {
        startMoveAnimation()
        startScaleAnimation()
    }
}