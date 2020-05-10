package com.genaku.snappingseekbar.model

import android.view.View
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatTextView

interface IIndicators {

    var indicatorSize: Float
    var indicatorTextSize: Float
    var indicatorDrawableId: Int
    var indicatorTextColor: Int
    var indicatorColor: Int
    var indicatorReachedColor: Int

    fun clear()
    fun initIndicators(seekBar: SeekBar, afterInit: () -> Unit)
    fun setIndicatorTextMargin(margin: Float)
    fun setIndicatorTextMargin(marginLeft: Float, marginTop: Float, marginRight: Float, marginBottom: Float)
    fun createIndicator(idx: Int, reached: Boolean): View
    fun createIndicatorText(idx: Int, text: String, reached: Boolean): AppCompatTextView
    fun indicatorWidth(idx: Int, reached: Boolean): Float
    fun indicatorHeight(idx: Int, reached: Boolean): Float
    fun checkIndicatorColor(progressTo: Float)
    fun getIndicator(idx: Int): View
    fun getIndicatorColor(idx: Int, reached: Boolean): Int
    fun getIndicatorResId(idx: Int, reached: Boolean): Int
    fun getIndicatorTextSize(idx: Int, reached: Boolean): Float
    fun getIndicatorTextColor(idx: Int, reached: Boolean): Int
}
