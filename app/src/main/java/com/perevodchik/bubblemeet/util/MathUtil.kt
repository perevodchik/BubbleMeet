package com.perevodchik.bubblemeet.util

import android.content.Context
import android.util.DisplayMetrics

object Math {

    fun dpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun pixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun dpToPixel(dp: Float, multiplier: Float): Float {
        return dp * multiplier
    }

    fun pixelsToDp(px: Float, multiplier: Float): Float {
        return px / multiplier
    }
}