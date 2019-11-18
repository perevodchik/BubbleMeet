package com.perevodchik.bubblemeet.util

import android.content.Context
import android.util.DisplayMetrics
import java.lang.Math
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

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

    fun differenceBetween(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        unit: Char
    ): Double {
        val dist = 60.0 * rad2deg(
            acos(
                sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(
                    deg2rad(
                        lat2
                    )
                ) * cos(deg2rad(lon1 - lon2))
            )
        ) * 1.1515
        if (unit == 'K') {
            return dist * 1.609344
        }
        return if (unit == 'N') {
            dist * 0.8684
        } else dist
    }

    private fun deg2rad(deg: Double): Double {
        return 3.141592653589793 * deg / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return 180.0 * rad / 3.141592653589793
    }
}