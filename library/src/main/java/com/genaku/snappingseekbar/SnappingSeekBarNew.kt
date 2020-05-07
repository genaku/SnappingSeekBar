package com.genaku.snappingseekbar

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import com.genaku.snappingseekbar.model.*
import com.genaku.snappingseekbar.utils.setColor
import kotlin.math.abs
import kotlin.math.roundToInt

class SnappingSeekBarNew @JvmOverloads constructor(
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
            setOnSeekBarChangeListener(this@SnappingSeekBarNew)
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
            setIndicatorTextMargin(0f, 16 * density, 0f, 0f)
            indicatorTextSize = 12f
            indicatorSize = 11.3f * density
            indicatorColor = ContextCompat.getColor(context, R.color.blue)
            indicatorTextColor = ContextCompat.getColor(context, R.color.black)
        }
    }

    private fun handleAttributes(attrs: AttributeSet) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.SnappingSeekBar, 0, 0)
        try {
            initThumb(attributes)
            initIndicator(attributes)
            initProgress(attributes)
        } finally {
            attributes.recycle()
        }
    }

    private fun initThumb(attributes: TypedArray) {
        setThumbDrawable(attributes.getResourceId(R.styleable.SnappingSeekBar_thumbDrawable, R.drawable.apptheme_scrubber_control_selector_holo_light))
        setThumbColor(attributes.getColor(R.styleable.SnappingSeekBar_thumbnailColor, ContextCompat.getColor(context, R.color.yellow)))
    }

    private fun initIndicator(attributes: TypedArray) {
        with(indicators) {
            indicatorDrawableId = attributes.getResourceId(R.styleable.SnappingSeekBar_indicatorDrawable, R.drawable.circle_background)
            indicatorSize = attributes.getDimension(R.styleable.SnappingSeekBar_indicatorSize, 11.3f * density)
            indicatorColor = attributes.getColor(R.styleable.SnappingSeekBar_indicatorColor, ContextCompat.getColor(context, R.color.white))
            indicatorReachedColor = attributes.getColor(R.styleable.SnappingSeekBar_indicatorReachedColor, ContextCompat.getColor(context, R.color.blue))
            indicatorTextColor = attributes.getColor(R.styleable.SnappingSeekBar_indicatorTextColor, ContextCompat.getColor(context, R.color.black))
            indicatorTextSize = attributes.getDimension(R.styleable.SnappingSeekBar_indicatorTextSize, 12f)
            val margin = attributes.getDimension(R.styleable.SnappingSeekBar_indicatorTextMargin, -1f)
            if (margin != -1f)
                setIndicatorTextMargin(margin)
            else
                setIndicatorTextMargin(
                        marginLeft = 0f,
                        marginTop = attributes.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginTop, 16 * density),
                        marginRight = 0f,
                        marginBottom = attributes.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginBottom, 0f)
                )

            val itemsArrayId = attributes.getResourceId(R.styleable.SnappingSeekBar_indicatorTextArrayId, 0)
            if (itemsArrayId > 0) {
                val items = resources.getStringArray(itemsArrayId)
                setItems(items.mapTo(mutableListOf()) { SimpleSeekBarItem(it) })
            }
        }
    }

    private fun initProgress(attributes: TypedArray) {
        setProgressBaseDrawable(attributes.getResourceId(R.styleable.SnappingSeekBar_progressDrawable, R.drawable.progress))
        setProgressColor(attributes.getColor(R.styleable.SnappingSeekBar_progressColor, ContextCompat.getColor(context, R.color.black)))
    }

    fun setItems(items: List<ISeekBarItem>): SnappingSeekBarNew = apply {
        model.setItems(items)
        seekBar?.run { indicators.initIndicators(this) }
    }

    fun setThumbDrawable(thumbDrawableId: Int): SnappingSeekBarNew = apply {
        val thumb = resources.getDrawable(thumbDrawableId)
        seekBar?.thumb = thumb
        val thumbPadding = thumb.intrinsicWidth / 2
        seekBar?.setPadding(thumbPadding, 0, thumbPadding, 0)
    }

    fun setThumbColor(thumbColor: Int): SnappingSeekBarNew = apply {
        seekBar?.thumb?.setColor(thumbColor)
    }

    fun setProgressBaseDrawable(progressBaseDrawable: Int): SnappingSeekBarNew = apply {
        seekBar?.progressDrawable = resources.getDrawable(progressBaseDrawable)
    }

    fun setProgressToIndex(index: Int) {
        progressTo = model.getPosition(index)
        handleSnapToClosestValue(false)
        indicators.checkIndicatorColor(progressTo)
    }

    fun setProgressToIndexWithAnimation(index: Int) {
        progressTo = model.getPosition(index)
        animateProgressBar(progressTo, true)
    }

    fun setProgressColor(progressColor: Int): SnappingSeekBarNew {
        seekBar?.progressDrawable?.setColor(progressColor)
        return this
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

    private fun animateProgressBar(positionTo: Float, animate: Boolean) {
        seekBar?.run {
            val anim = ProgressBarAnimation(this, progressFrom.toFloat(), positionTo.roundToInt().toFloat())
            anim.duration = if (animate) 200L else 0L
            startAnimation(anim)
        }
    }

    private fun invokeItemSelected(selectedIdx: Int) {
        listener?.onItemSelected(selectedIdx, model.getItem(selectedIdx))
    }

    interface OnItemSelectedListener {
        fun onItemSelected(itemIndex: Int, item: ISeekBarItem)
    }

    companion object {
        const val NOT_INITIALIZED_THUMB_POSITION = -1
    }
}
