package com.genaku.snappingseekbar.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.RelativeLayout
import kotlin.math.roundToInt

/**
 * User: tobiasbuchholz
 * Date: 28.07.14 | Time: 14:18
 */
fun getDPinPixel(context: Context, value: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics).roundToInt()
}

fun measureTextViewWidth(text: String, textSize: Float): Int {
    val p = Paint()
    p.textSize = textSize
    p.getTextBounds(text, 0, text.length, Rect())
    val mt: Float = p.measureText(text)
    return mt.roundToInt()
}

fun Drawable.setColor(color: Int) {
    this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
}

fun setLeftMargin(view: View, leftMargin: Int) {
    val params = view.layoutParams as RelativeLayout.LayoutParams
    params.leftMargin = leftMargin
    view.layoutParams = params
}

fun waitForLayoutPrepared(view: View?, listener: LayoutPreparedListener?) {
    view ?: return
    val viewTreeObserver = view.viewTreeObserver
    viewTreeObserver?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            invokeLayoutListener()
            removeGlobalOnLayoutListenerIfNeeded()
        }

        private fun invokeLayoutListener() {
            listener?.onLayoutPrepared(view)
        }

        private fun removeGlobalOnLayoutListenerIfNeeded() {
            val laterViewTreeObserver = view.viewTreeObserver ?: return
            if (laterViewTreeObserver.isAlive) {
                laterViewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}

interface LayoutPreparedListener {
    fun onLayoutPrepared(preparedView: View)
}
