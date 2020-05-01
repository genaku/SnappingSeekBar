package com.tobishiba.snappingseekbar.sample.activities

import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.tobishiba.snappingseekbar.R
import hu.mesys.snappingseekbar.library.models.SeekBarElement
import hu.mesys.snappingseekbar.library.utils.UiUtils.LayoutPreparedListener
import hu.mesys.snappingseekbar.library.utils.UiUtils.waitForLayoutPrepared
import hu.mesys.snappingseekbar.library.views.SnappingSeekBar
import hu.mesys.snappingseekbar.library.views.SnappingSeekBar.OnItemSelectionListener

/**
 * User: tobiasbuchholz
 * Date: 28.07.14 | Time: 14:18
 *
 *
 * Modified by József Mezei on 07.03.2017
 */
class MainActivity : Activity(), OnItemSelectionListener {

    private var layout: LinearLayout? = null
    private var elements: MutableList<SeekBarElement>? = null
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
        val elements = mutableListOf<SeekBarElement>()
        elements.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                indicatorText = "1",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background_small,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                indicatorText = "2",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                indicatorText = "3",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background_small,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                indicatorText = "4",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                indicatorText = "5",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background_small,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                indicatorText = "6",
                indicatorTextColor = ContextCompat.getColor(this, R.color.blue)
        ))
        elements.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorReachedColor = ContextCompat.getColor(this, R.color.red),
                indicatorText = "7",
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
        val elements2 = mutableListOf<SeekBarElement>()
        elements2.add(SeekBarElement(
                indicatorDrawableId = R.drawable.rect_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorText = "1",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.green),
                indicatorText = "2",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background_small,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorText = "3",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(SeekBarElement(
                indicatorDrawableId = R.drawable.rect_background,
                indicatorColor = ContextCompat.getColor(this, R.color.green),
                indicatorText = "4",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorText = "5",
                indicatorTextColor = ContextCompat.getColor(this, R.color.green_darker)
        ))
        elements2.add(SeekBarElement(
                indicatorDrawableId = R.drawable.circle_background,
                indicatorColor = ContextCompat.getColor(this, R.color.blue),
                indicatorText = "6",
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
        Toast.makeText(this, getString(R.string.toast_item_selected, itemIndex.toString(), elements!![itemIndex].indicatorText), Toast.LENGTH_SHORT).show()
    }
}