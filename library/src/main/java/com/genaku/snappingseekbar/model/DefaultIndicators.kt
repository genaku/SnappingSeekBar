package com.genaku.snappingseekbar.model

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatTextView
import com.genaku.snappingseekbar.MarginData
import com.genaku.snappingseekbar.utils.LayoutPreparedListener
import com.genaku.snappingseekbar.utils.getDPinPixel
import com.genaku.snappingseekbar.utils.measureTextViewWidth
import com.genaku.snappingseekbar.utils.setColor
import com.genaku.snappingseekbar.utils.setLeftMargin
import com.genaku.snappingseekbar.utils.waitForLayoutPrepared
import kotlin.math.roundToInt

/**
 * Author: Gena Kuchergin
 * Date: 01.05.2020
 */
open class DefaultIndicators(
        protected val context: Context,
        protected val model: SeekBarModel,
        protected val viewGroup: ViewGroup
) : IIndicators {

    private val indicators: MutableList<View> = mutableListOf()
    private val labels: MutableMap<Int, AppCompatTextView> = mutableMapOf()
    private var indicatorTextMargin = MarginData()
    private var reachedIndicator = 0

    override var indicatorSize = 0f
    override var indicatorDrawableId: Int = 0
    override var indicatorTextColor = 0
    override var indicatorColor: Int = 0
    override var indicatorReachedColor: Int = 0
    override var indicatorTextSize = 0f

    override fun initIndicators(seekBar: SeekBar) {
        waitForLayoutPrepared(seekBar, object : LayoutPreparedListener {
            override fun onLayoutPrepared(preparedView: View) {
                val width = preparedView.width
                model.setWidth(width.toFloat())
                seekBar.max = width
                val thumb = seekBar.thumb
                val thumbWidth = thumb.intrinsicWidth.toFloat()
                val thumbHalfHeight = thumb.intrinsicHeight / 2
                val indicatorPosKoef = 1f - thumbWidth / width
                clear()
                initIndicators(thumbWidth, thumbHalfHeight, indicatorPosKoef)
                initTextLabels(width, thumbWidth, thumbHalfHeight, indicatorPosKoef)
            }
        })
    }

    private fun initIndicators(thumbWidth: Float, thumbHalfHeight: Int, indicatorPosKoef: Float) {
        for (i in 0 until model.size) {
            val indicator = createIndicator(i, i == 0)
            val width = indicatorWidth(i, false)
            val height = indicatorHeight(i, false)
            val indicatorParams = RelativeLayout.LayoutParams(width.roundToInt(), height.roundToInt())
            indicatorParams.leftMargin = (indicatorPosKoef * model.getPosition(i) + ((thumbWidth - width) / 2f)).roundToInt()
            indicatorParams.topMargin = thumbHalfHeight - (height / 2f).roundToInt()
            viewGroup.addView(indicator, indicatorParams)
        }
    }

    private fun initTextLabels(seekBarWidth: Int, thumbWidth: Float, thumbHalfHeight: Int, indicatorPosKoef: Float) {
        val width = seekBarWidth - thumbWidth
        if (getAllIndicatorTextWidth() > width) {
            showBoundIndicatorTexts(thumbWidth, thumbHalfHeight, indicatorPosKoef)
        } else {
            showAllIndicatorTexts(thumbWidth, thumbHalfHeight, indicatorPosKoef)
        }
    }

    private fun getAllIndicatorTextWidth(): Int {
        var textWidth = 0
        val margin = (indicatorTextMargin.right + indicatorTextMargin.left).roundToInt()
        for (i in 0 until model.size) {
            val width = measureTextViewWidth(model.getTitle(i), getIndicatorTextSize(i, true))
            textWidth += width + margin
        }
        return textWidth
    }

    private fun showBoundIndicatorTexts(thumbWidth: Float, thumbHalfHeight: Int, indicatorPosKoef: Float) {
        addTextIndicator(0, thumbWidth, thumbHalfHeight, indicatorPosKoef)
        addTextIndicator(model.size - 1, thumbWidth, thumbHalfHeight, indicatorPosKoef)
    }

    private fun showAllIndicatorTexts(thumbWidth: Float, thumbHalfHeight: Int, indicatorPosKoef: Float) {
        for (i in 0 until model.size) {
            addTextIndicator(i, thumbWidth, thumbHalfHeight, indicatorPosKoef)
        }
    }

    private fun addTextIndicator(index: Int, thumbWidth: Float, thumbHalfHeight: Int, indicatorPosKoef: Float) {
        val textParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val numberLeftMargin = (indicatorPosKoef * model.getPosition(index) + thumbWidth / 2f).roundToInt()
        val numberTopMargin = (thumbHalfHeight - (indicatorHeight(index, index <= reachedIndicator) / 2f).roundToInt() + indicatorTextMargin.top).roundToInt()
        val view = createIndicatorText(index, model.getTitle(index), index <= reachedIndicator)
        textParams.setMargins(
                getDPinPixel(context, indicatorTextMargin.left),
                numberTopMargin,
                getDPinPixel(context, indicatorTextMargin.right),
                getDPinPixel(context, indicatorTextMargin.bottom))
        viewGroup.addView(view, textParams)
        waitForLayoutPrepared(view, object : LayoutPreparedListener {
            override fun onLayoutPrepared(preparedView: View) {
                val layoutRight = viewGroup.width - viewGroup.paddingRight
                val viewWidth = preparedView.width
                val leftMargin = numberLeftMargin - viewWidth / 2
                val finalMargin = when {
                    leftMargin < viewGroup.paddingLeft -> viewGroup.paddingLeft
                    leftMargin + viewWidth > layoutRight -> layoutRight - viewWidth
                    else -> leftMargin
                }
                setLeftMargin(preparedView, finalMargin)
            }
        })
    }

    override fun clear() {
        clearLabels()
        clearIndicators()
    }

    private fun clearLabels() {
        labels.forEach { viewGroup.removeView(it.value) }
        labels.clear()
    }

    private fun clearIndicators() {
        indicators.forEach { viewGroup.removeView(it) }
        indicators.clear()
    }

    override fun indicatorHeight(idx: Int, reached: Boolean): Float = indicatorSize

    override fun indicatorWidth(idx: Int, reached: Boolean): Float = indicatorSize

    override fun createIndicator(idx: Int, reached: Boolean): View {
        val indicator = View(context)
        indicator.setBackgroundDrawable(context.resources.getDrawable(getIndicatorResId(idx, reached)))
        indicator.background?.setColor(getIndicatorColor(idx, reached))
        indicators.add(idx, indicator)
        return indicator
    }

    override fun createIndicatorText(idx: Int, text: String, reached: Boolean): AppCompatTextView {
        val textIndicator = AppCompatTextView(context)
        textIndicator.text = text
        textIndicator.setTextSize(TypedValue.COMPLEX_UNIT_PX, getIndicatorTextSize(idx, reached))
        textIndicator.setTextColor(getIndicatorTextColor(idx, reached))
        labels[idx] = textIndicator
        return textIndicator
    }

    override fun getIndicatorTextColor(idx: Int, reached: Boolean): Int = indicatorTextColor

    override fun getIndicatorTextSize(idx: Int, reached: Boolean): Float = indicatorTextSize

    override fun checkIndicatorColor(progressTo: Float) {
        val newReachedIndicator = model.getIdx(progressTo)
        if (reachedIndicator == newReachedIndicator) return
        if (reachedIndicator > newReachedIndicator)
            changeIndicatorColor(reachedIndicator, newReachedIndicator + 1, true)
        else
            changeIndicatorColor(newReachedIndicator, reachedIndicator, false)
        reachedIndicator = newReachedIndicator
    }

    private fun changeIndicatorColor(reach: Int, from: Int, back: Boolean) {
        for (i in from..reach) {
            updateIndicatorColor(i, !back)
        }
    }

    override fun setIndicatorTextMargin(margin: Float) {
        setIndicatorTextMargin(margin, margin, margin, margin)
    }

    override fun getIndicator(idx: Int): View = indicators[idx]

    override fun getIndicatorColor(idx: Int, reached: Boolean): Int =
            if (reached) indicatorReachedColor else indicatorColor

    private fun updateIndicatorColor(idx: Int, reached: Boolean) {
        indicators[idx].background.setColor(getIndicatorColor(idx, reached))
    }

    override fun getIndicatorResId(idx: Int, reached: Boolean): Int {
        return indicatorDrawableId
    }

    override fun setIndicatorTextMargin(marginLeft: Float, marginTop: Float, marginRight: Float, marginBottom: Float) {
        indicatorTextMargin.left = marginLeft
        indicatorTextMargin.top = marginTop
        indicatorTextMargin.right = marginRight
        indicatorTextMargin.bottom = marginBottom
    }
}