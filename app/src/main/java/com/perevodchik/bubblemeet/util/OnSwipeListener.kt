package com.perevodchik.bubblemeet.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

open class OnSwipeListener(_ctx: Context) : View.OnTouchListener {

    private val gestureDetector = GestureDetector( _ctx, GestureListener())

    fun onTouch(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            onTouch(e)
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            this@OnSwipeListener.onClick()
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            this@OnSwipeListener.onDoubleClick()
            return super.onDoubleTap(e)
        }

        /*
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            //this@OnSwipeListener.onClick()
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            //this@OnSwipeListener.onDoubleClick()
            return super.onDoubleTap(e)
        }
         */


        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result: Boolean
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) <= 100.0f || abs(velocityX) <= 100.0f) {
                        return false
                    }
                    if (diffX > 0.0f) {
                        this@OnSwipeListener.onSwipeRight()
                    } else {
                        this@OnSwipeListener.onSwipeLeft()
                    }
                    result = true
                } else if (abs(diffY) <= 100.0f || abs(velocityY) <= 100.0f) {
                    result = false
                } else {
                    if (diffY > 0.0f) {
                        this@OnSwipeListener.onSwipeBottom()
                    } else {
                        this@OnSwipeListener.onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                result = false
            }

            return result
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    open fun onSwipeRight() {}

    open fun onSwipeLeft() {}

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}

    open fun onClick() {}

    open fun onDoubleClick() {}
}