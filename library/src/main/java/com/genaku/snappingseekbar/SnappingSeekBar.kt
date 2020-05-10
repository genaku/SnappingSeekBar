package com.genaku.snappingseekbar

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import com.genaku.snappingseekbar.model.IIndicators
import com.genaku.snappingseekbar.model.ISeekBarItem
import com.genaku.snappingseekbar.model.SeekBarModel
import com.genaku.snappingseekbar.model.SimpleSeekBarItem
import com.genaku.snappingseekbar.model.VariableIndicators
import com.genaku.snappingseekbar.utils.setColor
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * User: tobiasbuchholz
 * Date: 28.07.14 | Time: 14:18
 *
 *
 * Modified by József Mezei on 07.03.2017
 * Refactored to Kotlin and SOLID by Gena Kuchergin on 07.05.2020
 */
class SnappingSeekBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), SeekBar.OnSeekBarChangeListener {

    private var density = 0f
    private var seekBar: SeekBar? = null
    private var listener: OnItemSelectedListener? = null

    private var thumbPosition = NOT_INITIALIZED_THUMB_POSITION

    private var progressFrom = 0
    private var progressTo = 0f
    private val model: SeekBarModel = SeekBarModel()

    private lateinit var indicators: IIndicators

    init {
        initDensity()
        initSeekBar()
        handleAttributeSet(attrs)
    }

    private fun initDensity() {
        density = context.resources.displayMetrics.density
    }

    private fun initSeekBar() {
        removeView(seekBar)
        seekBar = AppCompatSeekBar(context).apply {
            setOnSeekBarChangeListener(this@SnappingSeekBar)
        }
        val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(seekBar, params)
    }

    private fun handleAttributeSet(attrs: AttributeSet?) {
        indicators = VariableIndicators(context, model, this)
        if (attrs == null) {
            initDefaultValues()
        } else {
            handleAttributes(attrs)
        }
    }

    private fun initDefaultValues() {
        setProgressBaseDrawable(R.drawable.progress)
        setThumbDrawable(R.drawable.apptheme_scrubber_control_selector_holo_light)
        setProgressColor(ContextCompat.getColor(context, R.color.black))
        setThumbColor(ContextCompat.getColor(context, R.color.green))
        with(indicators) {
            indicatorDrawableId = R.drawable.circle_background
            setIndicatorTextSize(12f)
            indicatorColor = ContextCompat.getColor(context, R.color.blue)
            indicatorTextColor = ContextCompat.getColor(context, R.color.black)
        }
        setIndicatorTextMargin(0f, 16f, 0f, 0f)
        setIndicatorSize(11.3f)
    }

    private fun handleAttributes(attrs: AttributeSet) {
        val attributes =
                context.theme.obtainStyledAttributes(attrs, R.styleable.SnappingSeekBar, 0, 0)
        try {
            initThumb(attributes)
            initIndicator(attributes)
            initProgress(attributes)
        } finally {
            attributes.recycle()
        }
    }

    private fun initThumb(attributes: TypedArray) {
        setThumbDrawable(
                attributes.getResourceId(
                        R.styleable.SnappingSeekBar_thumbDrawable,
                        R.drawable.apptheme_scrubber_control_selector_holo_light
                )
        )
        setThumbColor(
                attributes.getColor(
                        R.styleable.SnappingSeekBar_thumbnailColor,
                        ContextCompat.getColor(context, R.color.yellow)
                )
        )
    }

    private fun initIndicator(attributes: TypedArray) {
        with(indicators) {
            indicatorDrawableId = attributes.getResourceId(
                    R.styleable.SnappingSeekBar_indicatorDrawable,
                    R.drawable.circle_background
            )
            indicatorColor = attributes.getColor(
                    R.styleable.SnappingSeekBar_indicatorColor,
                    ContextCompat.getColor(context, R.color.white)
            )
            indicatorReachedColor = attributes.getColor(
                    R.styleable.SnappingSeekBar_indicatorReachedColor,
                    ContextCompat.getColor(context, R.color.blue)
            )
            indicatorTextColor = attributes.getColor(
                    R.styleable.SnappingSeekBar_indicatorTextColor,
                    ContextCompat.getColor(context, R.color.black)
            )
            setIndicatorTextSize(attributes.getDimension(R.styleable.SnappingSeekBar_indicatorTextSize, 12f))

            val itemsArrayId =
                    attributes.getResourceId(R.styleable.SnappingSeekBar_indicatorTextArrayId, 0)
            if (itemsArrayId > 0) {
                val items = resources.getStringArray(itemsArrayId)
                setItems(items.mapTo(mutableListOf()) { SimpleSeekBarItem(it) })
            }
        }
        setIndicatorSize(
                attributes.getDimension(R.styleable.SnappingSeekBar_indicatorSize, 11.3f)
        )
        val margin =
                attributes.getDimension(R.styleable.SnappingSeekBar_indicatorTextMargin, -1f)
        if (margin != -1f)
            setIndicatorTextMargin(margin)
        else
            setIndicatorTextMargin(
                    marginLeft = 0f,
                    marginTop = attributes.getDimension(
                            R.styleable.SnappingSeekBar_indicatorTextMarginTop,
                            16f
                    ),
                    marginRight = 0f,
                    marginBottom = attributes.getDimension(
                            R.styleable.SnappingSeekBar_indicatorTextMarginBottom,
                            0f
                    )
            )
    }

    fun setIndicatorTextMargin(margin: Float) {
        indicators.setIndicatorTextMargin(margin * density)
    }

    fun setIndicatorTextMargin(
            marginLeft: Float,
            marginTop: Float,
            marginRight: Float,
            marginBottom: Float
    ) {
        indicators.setIndicatorTextMargin(
                marginLeft * density,
                marginTop * density,
                marginRight * density,
                marginBottom * density
        )
    }

    private fun initProgress(attributes: TypedArray) {
        setProgressBaseDrawable(
                attributes.getResourceId(
                        R.styleable.SnappingSeekBar_progressDrawable,
                        R.drawable.progress
                )
        )
        setProgressColor(
                attributes.getColor(
                        R.styleable.SnappingSeekBar_progressColor,
                        ContextCompat.getColor(context, R.color.black)
                )
        )
    }

    var progress: Int
        get() = seekBar?.progress ?: 0
        set(progress) {
            progressTo = progress.toFloat()
            handleSnapToClosestValue(true)
        }

    fun setIndicatorTextColor(indicatorTextColor: Int) = apply {
        indicators.indicatorTextColor = indicatorTextColor
    }

    fun setIndicatorColor(indicatorColor: Int) = apply {
        indicators.indicatorColor = indicatorColor
    }

    fun setIndicatorReachedColor(color: Int) = apply {
        indicators.indicatorReachedColor = color
    }

    fun setIndicatorTextSize(textSize: Float) = apply {
        indicators.indicatorTextSize = textSize * density
    }

    fun setIndicatorSize(indicatorSize: Float) = apply {
        indicators.indicatorSize = indicatorSize * density
    }

    fun setItems(items: List<ISeekBarItem>) = apply {
        model.setItems(items)
        seekBar?.run {
            indicators.initIndicators(this) {
                setProgressToIndex(model.selectedIdx)
            }
        }
    }

    fun setThumbDrawable(thumbDrawableId: Int) = apply {
        val thumb = resources.getDrawable(thumbDrawableId)
        seekBar?.thumb = thumb
        val thumbPadding = thumb.intrinsicWidth / 2
        seekBar?.setPadding(thumbPadding, 0, thumbPadding, 0)
    }

    fun setThumbColor(thumbColor: Int) = apply {
        seekBar?.thumb?.setColor(thumbColor)
    }

    fun setProgressBaseDrawable(progressBaseDrawable: Int) = apply {
        seekBar?.progressDrawable = resources.getDrawable(progressBaseDrawable)
    }

    fun setProgressToIndex(index: Int) {
        progressTo = model.getPosition(index)
        handleSnapToIndex(index)
        indicators.checkIndicatorColor(progressTo)
    }

    fun setProgressToIndexWithAnimation(index: Int) {
        progressTo = model.getPosition(index)
        animateProgressBar(progressTo, true)
    }

    fun setProgressColor(progressColor: Int) = apply {
        seekBar?.progressDrawable?.setColor(progressColor)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        progressTo = progress.toFloat()
        initThumbPosition(progress, fromUser)
        handleSetProgressFrom(progress)
        if (fromUser) indicators.checkIndicatorColor(progressTo)
    }

    private fun initThumbPosition(progress: Int, fromUser: Boolean) {
        if (thumbPosition == NOT_INITIALIZED_THUMB_POSITION && fromUser) {
            thumbPosition = progress
        }
    }

    private fun handleSetProgressFrom(progress: Int) {
        val slidingDelta = abs(progress - thumbPosition)
        if (slidingDelta > 1) {
            progressFrom = progress
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        progressFrom = this.seekBar?.progress ?: 0
        thumbPosition = NOT_INITIALIZED_THUMB_POSITION
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        handleSnapToClosestValue(true)
    }

    private fun handleSnapToClosestValue(animate: Boolean) {
        val positionToSnap = model.closestPosition(progressTo)
        animateProgressBar(positionToSnap, animate)
        invokeItemSelected(model.selectedIdx)
    }

    private fun handleSnapToIndex(idx: Int) {
        model.setIndex(idx)
        val positionToSnap = model.getPosition(idx)
        animateProgressBar(positionToSnap, false)
        invokeItemSelected(model.selectedIdx)
    }


    private fun animateProgressBar(positionTo: Float, animate: Boolean) {
        seekBar?.run {
            val anim = ProgressBarAnimation(
                    this,
                    progressFrom.toFloat(),
                    positionTo.roundToInt().toFloat()
            )
            anim.duration = if (animate) 200L else 0L
            startAnimation(anim)
        }
    }

    private fun invokeItemSelected(selectedIdx: Int) {
        listener?.onItemSelected(selectedIdx, model.getItem(selectedIdx))
    }

    fun setOnItemSelectionListener(listener: OnItemSelectedListener?) = apply {
        this.listener = listener
    }

    interface OnItemSelectedListener {
        fun onItemSelected(itemIndex: Int, item: ISeekBarItem)
    }

    companion object {
        const val NOT_INITIALIZED_THUMB_POSITION = -1
    }
}
