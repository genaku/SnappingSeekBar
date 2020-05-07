package com.genaku.snappingseekbar.model

import android.content.Context
import android.view.ViewGroup

/**
 * Author: Gena Kuchergin
 * Date: 07.05.2020
 */
class VariableIndicators(context: Context, model: SeekBarModel, viewGroup: ViewGroup) :
        DefaultIndicators(context, model, viewGroup) {

    override fun indicatorHeight(idx: Int, reached: Boolean): Float =
            when (val item = model.getItem(idx)) {
                is VariableSeekBarItem -> item.indicatorSize ?: indicatorSize
                else -> indicatorSize
            }

    override fun indicatorWidth(idx: Int, reached: Boolean): Float =
            when (val item = model.getItem(idx)) {
                is VariableSeekBarItem -> item.indicatorSize ?: indicatorSize
                else -> indicatorSize
            }

    override fun getIndicatorTextColor(idx: Int, reached: Boolean): Int =
            when (val item = model.getItem(idx)) {
                is VariableSeekBarItem -> item.indicatorTextColor ?: indicatorTextColor
                else -> indicatorTextColor
            }

    override fun getIndicatorTextSize(idx: Int, reached: Boolean): Float =
            when (val item = model.getItem(idx)) {
                is VariableSeekBarItem -> item.indicatorTextSize ?: indicatorTextSize
                else -> indicatorTextSize
            }

    override fun getIndicatorResId(idx: Int, reached: Boolean): Int =
            when (val item = model.getItem(idx)) {
                is VariableSeekBarItem -> item.indicatorDrawableId ?: indicatorDrawableId
                else -> indicatorDrawableId
            }

    override fun getIndicatorColor(idx: Int, reached: Boolean): Int {
        return when (val item = model.getItem(idx)) {
            is VariableSeekBarItem -> if (reached)
                item.indicatorReachedColor ?: indicatorReachedColor
            else
                item.indicatorColor ?: indicatorColor
            else -> if (reached)
                indicatorReachedColor
            else
                indicatorColor
        }
    }
}