package com.genaku.sample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.genaku.snappingseekbar.SnappingSeekBar
import com.genaku.snappingseekbar.SnappingSeekBar.OnItemSelectionListener
import com.genaku.snappingseekbar.model.VariableSeekBarItem
import com.genaku.snappingseekbar.utils.UiUtils.LayoutPreparedListener
import com.genaku.snappingseekbar.utils.UiUtils.waitForLayoutPrepared

/**
 * User: tobiasbuchholz
 * Date: 28.07.14 | Time: 14:18
 *
 *
 * Modified by József Mezei on 07.03.2017
 */
class MainActivity : Activity(), OnItemSelectionListener {

    private var layout: LinearLayout? = null
    private var elements: MutableList<VariableSeekBarItem>? = null
    private lateinit var snappingSeekBar: SnappingSeekBar
    private lateinit var snappingSeekBar2: SnappingSeekBar
    private var colorBlack = 0
    private var colorRed = 0
    private var colorGrey = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById<View>(R.id.activity_main_layout) as LinearLayout
        snappingSeekBar = findViewById<View>(R.id.snapp) as SnappingSeekBar
        snappingSeekBar2 = findViewById<View>(R.id.snapp_2) as SnappingSeekBar
        init()
        snappingSeekBarFromList()
        snappingSeekBarFromList2()
    }

    private fun init() {
        colorBlack = ContextCompat.getColor(this, R.color.black)
        colorRed = ContextCompat.getColor(this, R.color.red)
        colorGrey = ContextCompat.getColor(this, R.color.grey)
    }

    private fun snappingSeekBarFromList() {
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
        this.elements = elements
        snappingSeekBar.setItems(elements)
                .setProgressBaseDrawable(R.drawable.progress)
                .setProgressColor(colorRed)
                .setThumbnailColor(colorRed)
                .setOnItemSelectionListener(this)
        waitForLayoutPrepared(snappingSeekBar, object : LayoutPreparedListener {
            override fun onLayoutPrepared(preparedView: View) {
                snappingSeekBar.setProgressToIndex(2)
            }
        })
    }

    private fun snappingSeekBarFromList2() {
        val elements2 = mutableListOf<VariableSeekBarItem>()
        elements2.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.rect_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                name = "1",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.green),
                name = "2",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background_small,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                name = "3",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.rect_background,
                indicatorColor = ContextCompat.getColor(this, R.color.green),
                name = "4",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                name = "5",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(VariableSeekBarItem(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                name = "6",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        snappingSeekBar2.setItems(elements2)
                .setProgressBaseDrawable(R.drawable.progress)
                .setProgressColor(colorRed)
                .setThumbnailColor(colorRed)
                .setOnItemSelectionListener(this)
        waitForLayoutPrepared(snappingSeekBar2, object : LayoutPreparedListener {
            override fun onLayoutPrepared(preparedView: View) {
                snappingSeekBar2.setProgressToIndex(2)
            }
        })
    }

    override fun onItemSelected(itemIndex: Int, itemString: String?) {
        Toast.makeText(this, getString(R.string.toast_item_selected, itemIndex.toString(), elements!![itemIndex].name), Toast.LENGTH_SHORT).show()
    }
}