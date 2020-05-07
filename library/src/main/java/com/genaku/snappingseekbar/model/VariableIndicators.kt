package com.genaku.snappingseekbar.model

import android.content.Context
import android.view.ViewGroup

class VariableIndicators(context: Context, model: SeekBarModel, viewGroup: ViewGroup)
    : DefaultIndicators(context, model, viewGroup) {

    override fun indicatorHeight(idx: Int, reached: Boolean): Float =
            (model.getItem(idx) as VariableSeekBarItem?)?.indicatorSize ?: indicatorSize

    override fun indicatorWidth(idx: Int, reached: Boolean): Float =
            (model.getItem(idx) as VariableSeekBarItem?)?.indicatorSize ?: indicatorSize

    override fun getIndicatorTextColor(idx: Int, reached: Boolean): Int =
            (model.getItem(idx) as VariableSeekBarItem?)?.indicatorTextColor ?: indicatorTextColor

    override fun getIndicatorTextSize(idx: Int, reached: Boolean): Float =
            (model.getItem(idx) as VariableSeekBarItem?)?.indicatorTextSize ?: indicatorTextSize

    override fun getIndicatorResId(idx: Int, reached: Boolean): Int =
            (model.getItem(idx) as VariableSeekBarItem?)?.indicatorDrawableId ?: indicatorDrawableId

    override fun getIndicatorColor(idx: Int, reached: Boolean): Int {
        val element = model.getItem(idx) as VariableSeekBarItem?
        return if (reached)
            element?.indicatorReachedColor ?: indicatorReachedColor
        else
            element?.indicatorColor ?: indicatorColor
    }
}