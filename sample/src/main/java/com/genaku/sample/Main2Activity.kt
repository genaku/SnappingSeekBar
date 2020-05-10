package com.genaku.sample

import android.app.Activity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.genaku.snappingseekbar.model.SimpleSeekBarItem
import com.genaku.snappingseekbar.model.VariableSeekBarItem
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val items = mutableListOf<SimpleSeekBarItem>()
        val oldItems = Array<String?>(4) { i -> "$i" }
        for (i in 100..200 step 5) {
            items.add(SimpleSeekBarItem("$i"))
        }
        val colorRed = ContextCompat.getColor(this, R.color.red)
        seek.setItems(items)
                .setProgressBaseDrawable(R.drawable.progress)
                .setProgressColor(colorRed)
                .setThumbColor(colorRed)
        seekOld.setItems(oldItems)
        snappingSeekBarFromList()
    }

    private fun snappingSeekBarFromList() {
        val elements = getElements()
        val colorRed = ContextCompat.getColor(this, R.color.red)
        snapp.setItems(elements)
                .setProgressBaseDrawable(R.drawable.progress)
                .setProgressColor(colorRed)
                .setThumbnailColor(colorRed)
                .setProgressToIndex(2)
    }

    private fun getElements(): MutableList<VariableSeekBarItem> {
        val elements = mutableListOf<VariableSeekBarItem>()
        elements.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                name = "1",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background_small,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                name = "2",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                name = "3",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background_small,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                name = "4",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                name = "5",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background_small,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                name = "6",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                name = "7",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        return elements
    }
}
