package hu.mesys.snappingseekbar.library.models

data class SeekBarElement(
        var indicatorDrawableId: Int,
        var indicatorDrawableSize: Int = 10,
        var indicatorColor: Int = 0,
        var indicatorReachedColor: Int = 0,
        var indicatorText: String,
        var indicatorTextColor: Int = 0,
        var indicatorTextSize: Int = 12
)