package com.genaku.snappingseekbar.model

data class VariableSeekBarItem(
        override var name: String,
        var indicatorDrawableId: Int? = null,
        var indicatorSize: Float? = null,
        var indicatorColor: Int? = null,
        var indicatorReachedColor: Int? = null,
        var indicatorTextColor: Int? = null,
        var indicatorTextSize: Float? = null
) : ISeekBarItem