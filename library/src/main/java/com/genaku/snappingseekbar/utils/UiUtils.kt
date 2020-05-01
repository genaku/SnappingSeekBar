package com.genaku.snappingseekbar.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
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
object UiUtils {

    @JvmStatic
    fun setColor(drawable: Drawable?, color: Int) {
        drawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
    }

    @JvmStatic
    fun setLeftMargin(view: View, leftMargin: Int) {
        val params = view.layoutParams as RelativeLayout.LayoutParams
        params.leftMargin = leftMargin
        view.layoutParams = params
    }

    @JvmStatic
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
                val laterViewTreeObserver = view.viewTreeObserver
                if (laterViewTreeObserver != null && laterViewTreeObserver.isAlive) {
                    laterViewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }

    @JvmStatic
    fun getDPinPixel(context: Context, value: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics).roundToInt()
    }

    interface LayoutPreparedListener {
        fun onLayoutPrepared(preparedView: View)
    }
}