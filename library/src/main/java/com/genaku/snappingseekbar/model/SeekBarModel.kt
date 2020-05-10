package com.genaku.snappingseekbar.model

import kotlin.math.roundToInt

/**
 * Author: Gena Kuchergin
 * Date: 01.05.2020
 */
open class SeekBarModel {

    private var _items: List<ISeekBarItem> = emptyList()
    private var _width: Float = 0f
    private var _sectionWidth: Float = 0f
    private var _selectedIdx: Int = 0

    val selectedIdx: Int
        get() = _selectedIdx

    val size: Int
        get() = _items.size

    fun setItems(items: List<ISeekBarItem>) {
        _items = ArrayList(items)
        updateSectionLength()
    }

    fun getTitle(idx: Int): String {
        return _items[idx].name
    }

    fun getItem(idx: Int): ISeekBarItem {
        return _items[idx]
    }

    fun setWidth(width: Float) {
        _width = width
        updateSectionLength()
    }

    private fun updateSectionLength() {
        _sectionWidth = _width / (_items.size - 1)
    }

    fun getIdx(position: Float): Int {
        if (_sectionWidth == 0f) return 0
        return (position / _sectionWidth).roundToInt()
    }

    fun getPosition(idx: Int): Float = _sectionWidth * idx

    fun setIndex(idx: Int) {
        _selectedIdx = idx
    }

    fun closestPosition(position: Float): Float {
        _selectedIdx = getIdx(position)
        return _selectedIdx * _sectionWidth
    }
}