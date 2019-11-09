package com.perevodchik.bubblemeet.util

import android.content.Context
import android.graphics.Point
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import kotlin.math.pow
import kotlin.math.sqrt

fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)

fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

fun View.getPoint(): Point {
    val arr = IntArray(2)
    getLocationOnScreen(arr)
    return Point(arr[0], arr[1])
}

fun Point.distance(p: Point): Float {
    return sqrt((p.x - x + 0.0).pow(2) + (p.y - y + 0.0).pow(2)).toFloat()
}