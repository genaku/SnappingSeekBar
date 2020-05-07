package com.genaku.snappingseekbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.genaku.snappingseekbar.model.VariableSeekBarItem
import com.genaku.snappingseekbar.utils.LayoutPreparedListener
import com.genaku.snappingseekbar.utils.getDPinPixel
import com.genaku.snappingseekbar.utils.setColor
import com.genaku.snappingseekbar.utils.setLeftMargin
import com.genaku.snappingseekbar.utils.waitForLayoutPrepared
import java.util.*
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
class SnappingSeekBarOld : RelativeLayout, OnSeekBarChangeListener {

    private var _context: Context
    private var density = 0f
    private var onItemSelectionListener: OnItemSelectionListener? = null
    private val refine = 100f

    // Seek Bar ------------------------------------------------------------------------------------
    private var seekBar: SeekBar? = null

    // Indicators ----------------------------------------------------------------------------------
    private var indicatorList: MutableList<View> = mutableListOf()
    private var indicatorTextList: MutableList<AppCompatTextView> = mutableListOf()
    private var reachedIndicator = 0
    private var indicatorCount = 0
        set(value) {
            field = value
            sectionLength = 100f / (indicatorCount - 1f)
        }
    private var sectionLength: Float = 0f
    private var indicatorColor = 0
    private var indicatorSize = 0f
    private var indicatorDrawableId = 0
    private var indicatorTextColor = 0
    private var indicatorTextMargin = FloatArray(4)
    private var indicatorItems: Array<String?> = arrayOfNulls(0)
    private var indicatorTextSize = 0f

    // Thumb ---------------------------------------------------------------------------------------
    private var thumbPosition = NOT_INITIALIZED_THUMB_POSITION
    private lateinit var thumb: Drawable

    // Progress ------------------------------------------------------------------------------------
    private var progressDrawable: Drawable? = null
    private var progressFrom = 0
    private var progressTo = 0f

    // Objects -------------------------------------------------------------------------------------
    private var seekBarElementList: List<VariableSeekBarItem> = emptyList()

    constructor(context: Context) : super(context) {
        _context = context
        initDensity()
        initSeekBar()
        initDefaultValues()
    }

    private fun initDensity() {
        density = _context.resources.displayMetrics.density
    }

    private fun initDefaultValues() {
        setProgressBaseDrawable(R.drawable.progress)
        indicatorList = ArrayList()
        indicatorTextList = ArrayList()
        setThumbDrawable(R.drawable.apptheme_scrubber_control_selector_holo_light)
        setIndicatorDrawable(R.drawable.circle_background)
        setIndicatorTextMargin(0f, (35 * density).roundToInt().toFloat(), 0f, 0f)
        setIndicatorTextSize(12 * density)
        setIndicatorSize(11.3f * density)
        indicatorTextMargin[1] = 15 * density
        setProgressColor(ContextCompat.getColor(context, R.color.black))
        setIndicatorColor(ContextCompat.getColor(context, R.color.blue))
        setIndicatorTextColor(ContextCompat.getColor(context, R.color.black))
        setThumbnailColor(ContextCompat.getColor(context, R.color.green))
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        _context = context
        initDensity()
        initSeekBar()
        handleAttributeSet(attrs)
        if (indicatorCount == 0) return
        initIndicators()
    }

    private fun handleAttributeSet(attrs: AttributeSet?) {
        val typedArray = _context.theme.obtainStyledAttributes(attrs, R.styleable.SnappingSeekBar, 0, 0)
        try {
            initThumb(typedArray)
            initIndicator(typedArray)
            initProgress(typedArray)
        } finally {
            typedArray.recycle()
        }
    }

    private fun initThumb(typedArray: TypedArray) {
        setThumbDrawable(typedArray.getResourceId(R.styleable.SnappingSeekBar_thumbDrawable, R.drawable.apptheme_scrubber_control_selector_holo_light))
        setThumbnailColor(typedArray.getColor(R.styleable.SnappingSeekBar_thumbnailColor, ContextCompat.getColor(context, R.color.yellow)))
    }

    private fun initIndicator(typedArray: TypedArray) {
        indicatorList = ArrayList()
        indicatorTextList = ArrayList()
        setIndicatorDrawable(typedArray.getResourceId(R.styleable.SnappingSeekBar_indicatorDrawable, R.drawable.circle_background))
        setIndicatorSize(typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorSize, 11.3f * density))
        setIndicatorColor(typedArray.getColor(R.styleable.SnappingSeekBar_indicatorColor, ContextCompat.getColor(context, R.color.blue)))
        setIndicatorTextColor(typedArray.getColor(R.styleable.SnappingSeekBar_indicatorTextColor, ContextCompat.getColor(context, R.color.black)))
        setIndicatorTextSize(typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextSize, 12 * density))
        val itemsArrayId = typedArray.getResourceId(R.styleable.SnappingSeekBar_indicatorTextArrayId, 0)
        if (itemsArrayId > 0) setItems(itemsArrayId) else setItemsAmount(typedArray.getInteger(R.styleable.SnappingSeekBar_indicatorAmount, 0))
        indicatorTextMargin[1] = typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginTop, 15 * density)
        indicatorTextMargin[3] = typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginBottom, 0f)
        val margin = typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMargin, -1f)
        if (margin != -1f) setIndicatorTextMargin(margin)
    }

    private fun initProgress(typedArray: TypedArray) {
        setProgressBaseDrawable(typedArray.getResourceId(R.styleable.SnappingSeekBar_progressDrawable, R.drawable.progress))
        setProgressColor(typedArray.getColor(R.styleable.SnappingSeekBar_progressColor, ContextCompat.getColor(context, R.color.black)))
    }

    fun setItems(itemsArrayId: Int) {
        setItems(_context.resources.getStringArray(itemsArrayId))
    }

    fun setItems(items: Array<String?>) {
        if (items.size > 1) {
            indicatorItems = items
            indicatorCount = indicatorItems.size
            initIndicators()
        } else {
            throw IllegalStateException("SnappingSeekBar has to contain at least 2 items")
        }
    }

    fun setItemsAmount(itemsAmount: Int) {
        indicatorCount = itemsAmount
    }

    // Build UI ------------------------------------------------------------------------------------
    // Snapping Seek Bar ----------------------------------
    private fun initSeekBar() {
        removeView(seekBar)
        val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        seekBar = AppCompatSeekBar(context).apply {
            setOnSeekBarChangeListener(this@SnappingSeekBarOld)
            max = 100 * refine.toInt()
        }
        addView(seekBar, params)
    }

    // Indicators -------------------------------------------
    private fun initIndicators() {
        waitForLayoutPrepared(seekBar, object : LayoutPreparedListener {
            override fun onLayoutPrepared(preparedView: View) {
                val seekBarWidth = preparedView.width
                initIndicators(seekBarWidth)
            }
        })
    }

    private fun initIndicators(seekBarWidth: Int) {
        removeIndicator()
        for (i in 0 until indicatorCount) {
            addIndicator(seekBarWidth, i)
        }
        initTextLabels(seekBarWidth)
    }

    private fun initTextLabels(seekBarWidth: Int) {
        if (seekBarElementList.isEmpty() && indicatorItems.isEmpty()) {
            return
        }
        if (getAllIndicatorTextWidth() > seekBarWidth) {
            showBoundIndicatorTexts(seekBarWidth)
        } else {
            showAllIndicatorTexts(seekBarWidth)
        }
    }

    private fun getAllIndicatorTextWidth(): Int {
        var textWidth = 0
        val margin = 2 * indicatorTextMargin[0].roundToInt()
        for (i in 0 until indicatorCount) {
            val size = measureTextViewSize(getIndicatorText(i), indicatorTextSize)
            textWidth += size + margin
        }
        return textWidth
    }

    private fun showBoundIndicatorTexts(seekBarWidth: Int) {
        addIndicatorTextIfNeeded(seekBarWidth, 0)
        addIndicatorTextIfNeeded(seekBarWidth, indicatorCount - 1)
    }

    private fun showAllIndicatorTexts(seekBarWidth: Int) {
        for (i in 0 until indicatorCount) {
            addIndicatorTextIfNeeded(seekBarWidth, i)
        }
    }

    private fun measureTextViewSize(s: String, textSize: Float): Int {
        val p = Paint()
        val bounds = Rect()
        p.textSize = textSize
        p.getTextBounds(s, 0, s.length, bounds)
        val mt: Float = p.measureText(s)
        return mt.roundToInt()
    }

    private fun addIndicator(seekBarWidth: Int, index: Int) {
        val seekBarWidthWithoutThumbOffset = seekBarWidth - thumbnailWidth
        val size = indicatorSize.roundToInt()
        val indicatorParams = LayoutParams(size, size)
        indicatorParams.leftMargin = (seekBarWidthWithoutThumbOffset / 100f * index * sectionLength + ((thumbnailWidth - indicatorSize) / 2f)).roundToInt()
        indicatorParams.topMargin = thumb.intrinsicHeight / 2 - (indicatorSize / 2f).roundToInt()
        val indicator = createIndicator(index)
        addView(indicator, indicatorParams)
        indicatorList.add(indicator)
    }

    private fun removeIndicator() {
        for (i in indicatorList.indices) {
            removeView(indicatorList[i])
            removeView(indicatorTextList[i])
        }
        indicatorList.clear()
    }

    private fun createIndicator(index: Int): View {
        val indicator = View(_context)
        checkIndicatorDrawable(index)
        val drawable = resources.getDrawable(indicatorDrawableId)
        indicator.setBackgroundDrawable(drawable)
        indicator.background?.setColor(indicatorColor)
        return indicator
    }

    // Change indicator drawable and/or color from list
    private fun checkIndicatorDrawable(index: Int) {
        if (seekBarElementList.isEmpty()) return
        val element = seekBarElementList[index]
        indicatorDrawableId = element.indicatorDrawableId!!
        indicatorColor = if (element.indicatorReachedColor != null && progressTo >= indicatorList.size * (100 / indicatorCount)) element.indicatorReachedColor!! else element.indicatorColor!!
    }

    private fun checkIndicatorColor() {
        val reach = (progressTo / sectionLength).roundToInt()
        if (reachedIndicator == reach || indicatorList.isEmpty() || seekBarElementList.isEmpty()) return
        if (reachedIndicator > reach) changeIndicatorColor(reachedIndicator, reach + 1, true) else changeIndicatorColor(reach, reachedIndicator, false)
        reachedIndicator = reach
    }

    private fun changeIndicatorColor(reach: Int, from: Int, back: Boolean) {
        for (i in from..reach) {
            val view = indicatorList[i]
            val element = seekBarElementList[i]
            if (element.indicatorReachedColor == -1) return
            val color = if (back) element.indicatorColor else element.indicatorReachedColor
            view.background?.setColor(color!!)
        }
    }

    private fun addIndicatorTextIfNeeded(completeSeekBarWidth: Int, index: Int) {
        if (indicatorItems.size == indicatorCount || seekBarElementList.size == indicatorCount) {
            addTextIndicator(completeSeekBarWidth, index)
        }
    }

    private fun addTextIndicator(completeSeekBarWidth: Int, index: Int) {
        val thumbnailWidth = thumb.intrinsicWidth
        val seekBarWidthWithoutThumbOffset = completeSeekBarWidth - thumbnailWidth
        val textParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val numberLeftMargin = (seekBarWidthWithoutThumbOffset / 100f * index * sectionLength + thumbnailWidth / 2f).roundToInt()
        val numberTopMargin = (thumb.intrinsicHeight / 2 - (indicatorSize / 2f).roundToInt() + indicatorTextMargin[1]).roundToInt()
        Log.d("TAG", "addText $index, $thumbnailWidth, $numberLeftMargin")
        checkIndicatorText(index)
        val view = createIndicatorText(index)
        textParams.setMargins(
                getDPinPixel(context, indicatorTextMargin[0]),
                numberTopMargin,
                getDPinPixel(context, indicatorTextMargin[2]),
                getDPinPixel(context, indicatorTextMargin[3]))
        textParams.topMargin = numberTopMargin
        addView(view, textParams)
        indicatorTextList.add(view)
        waitForLayoutPrepared(view, createTextIndicatorLayoutPreparedListener(numberLeftMargin))
    }

    private fun createIndicatorText(index: Int): AppCompatTextView {
        val textIndicator = AppCompatTextView(_context)
        textIndicator.text = getIndicatorText(index)
        textIndicator.setTextSize(TypedValue.COMPLEX_UNIT_SP, indicatorTextSize / density)
        textIndicator.setTextColor(indicatorTextColor)
        return textIndicator
    }

    private fun getIndicatorText(index: Int): String {
        return if (seekBarElementList.isNotEmpty())
            seekBarElementList[index].name
        else
            indicatorItems[index] ?: ""
    }

    private fun createTextIndicatorLayoutPreparedListener(numberLeftMargin: Int): LayoutPreparedListener {
        return object : LayoutPreparedListener {
            override fun onLayoutPrepared(preparedView: View) {
                val layoutWidth = width - paddingRight
                val viewWidth = preparedView.width
                val leftMargin = numberLeftMargin - viewWidth / 2
                val paddingLeft = paddingLeft
                val finalMargin = if (leftMargin < paddingLeft) paddingLeft else if (leftMargin + viewWidth > layoutWidth) layoutWidth - viewWidth else leftMargin
                setLeftMargin(preparedView, finalMargin)
            }
        }
    }

    private fun checkIndicatorText(index: Int): Boolean {
        if (seekBarElementList.isNullOrEmpty()) return false
        val element = seekBarElementList[index]
        indicatorTextColor = element.indicatorTextColor!!
        return true
    }

    // Others --------------------------------------------------------------------------------------
    private fun initThumbPosition(progress: Int, fromUser: Boolean) {
        if (thumbPosition == NOT_INITIALIZED_THUMB_POSITION && fromUser) {
            thumbPosition = progress
        }
    }

    private fun handleSetFromProgress(progress: Int) {
        val slidingDelta = abs(progress - thumbPosition)
        if (slidingDelta > 1) {
            progressFrom = progress
        }
    }

    private fun handleSnapToClosestValue(animate: Boolean) {
        val selectedSection = (progressTo / sectionLength).roundToInt()
        val valueToSnap = selectedSection * sectionLength
        animateProgressBar(valueToSnap, animate)
        invokeItemSelected(selectedSection.toFloat())
    }

    private fun animateProgressBar(toProgress: Float, animate: Boolean) {
        seekBar?.run {
            val anim = ProgressBarAnimation(this, progressFrom.toFloat(), (toProgress * refine).roundToInt().toFloat())
            anim.duration = if (animate) 200 else 0L
            startAnimation(anim)
        }
    }

    private fun invokeItemSelected(selectedSection: Float) {
        onItemSelectionListener?.onItemSelected(selectedSection.toInt(), getItemString(selectedSection.toInt()))
    }

    private fun getItemString(index: Int): String? {
        return if (indicatorItems.size > index) {
            indicatorItems[index]
        } else ""
    }

    // Progress --------------------------------------------
    var progress: Int
        get() = seekBar?.progress ?: 0
        set(progress) {
            progressTo = progress.toFloat()
            handleSnapToClosestValue(true)
        }

    private fun getProgressForIndex(index: Int): Float = index * sectionLength

    val selectedItemIndex: Int
        get() = (progressTo / sectionLength).roundToInt()

    private val thumbnailWidth: Int
        get() = thumb.intrinsicWidth

    private val indicatorTextTopMargin: Int
        get() {
            if (indicatorTextList.isEmpty()) return 0
            val tv = indicatorTextList[0]
            val params = tv.layoutParams as LayoutParams
            return params.topMargin
        }

    // Setters ------------------------------------------------------------------------------------
    // SeekBar ---------------------------------------------
    fun setSeekbarColor(seekbarColor: Int): SnappingSeekBarOld {
        seekBar?.setBackgroundColor(seekbarColor)
        return this
    }

    fun setSeekBarPadding(padding: FloatArray): SnappingSeekBarOld =
            setSeekBarPadding(padding[0], padding[1], padding[2], padding[3])

    fun setSeekBarPadding(padding: Float): SnappingSeekBarOld =
            setSeekBarPadding(padding, padding, padding, padding)

    fun setSeekBarPadding(paddingLeft: Float, paddingTop: Float, paddingRight: Float, paddingBottom: Float): SnappingSeekBarOld {
        seekBar?.setPadding(
                getDPinPixel(context, paddingLeft),
                getDPinPixel(context, paddingTop),
                getDPinPixel(context, paddingRight),
                getDPinPixel(context, paddingBottom)
        )
        return this
    }

    fun setSeekBarMargin(margin: FloatArray): SnappingSeekBarOld =
            setSeekBarMargin(margin[0], margin[1], margin[2], margin[3])

    fun setSeekBarMargin(margin: Float): SnappingSeekBarOld =
            setSeekBarMargin(margin, margin, margin, margin)

    fun setSeekBarMargin(marginLeft: Float, marginTop: Float, marginRight: Float, marginBottom: Float): SnappingSeekBarOld {
        seekBar?.layoutParams?.run {
            this as LayoutParams
            setMargins(
                    getDPinPixel(context, marginLeft),
                    getDPinPixel(context, marginTop),
                    getDPinPixel(context, marginRight),
                    getDPinPixel(context, marginBottom)
            )
        }
        return this
    }

    // Indicators ------------------------------------------
    fun setIndicatorDrawable(indicatorDrawableId: Int): SnappingSeekBarOld {
        this.indicatorDrawableId = indicatorDrawableId
        return this
    }

    fun setIndicatorColor(indicatorColor: Int): SnappingSeekBarOld {
        this.indicatorColor = indicatorColor
        return this
    }

    fun setIndicatorSize(indicatorSize: Float): SnappingSeekBarOld {
        this.indicatorSize = indicatorSize
        return this
    }

    fun setIndicatorTextColor(indicatorTextColor: Int): SnappingSeekBarOld {
        this.indicatorTextColor = indicatorTextColor
        return this
    }

    fun setIndicatorTextMargin(margin: Float): SnappingSeekBarOld {
        return setIndicatorTextMargin(margin, margin, margin, margin)
    }

    fun setIndicatorTextMargin(marginLeft: Float, marginTop: Float, marginRight: Float, marginBottom: Float): SnappingSeekBarOld {
        indicatorTextMargin[0] = marginLeft
        indicatorTextMargin[1] = marginTop
        indicatorTextMargin[2] = marginRight
        indicatorTextMargin[3] = marginBottom
        return this
    }

    fun setIndicatorTextSize(textSize: Float): SnappingSeekBarOld {
        indicatorTextSize = textSize
        return this
    }

    fun setProgressBaseDrawable(progressBaseDrawable: Int): SnappingSeekBarOld {
        progressDrawable = resources.getDrawable(progressBaseDrawable)
        seekBar?.progressDrawable = progressDrawable
        return this
    }

    fun setProgressToIndex(index: Int): SnappingSeekBarOld {
        progressTo = getProgressForIndex(index)
        reachedIndicator = index
        handleSnapToClosestValue(false)
        return this
    }

    fun setProgressToIndexWithAnimation(index: Int): SnappingSeekBarOld {
        progressTo = getProgressForIndex(index)
        animateProgressBar(progressTo, true)
        return this
    }

    fun setProgressColor(progressColor: Int): SnappingSeekBarOld {
        progressDrawable?.setColor(progressColor)
        return this
    }

    // Thumbnail --------------------------------------------
    fun setThumbDrawable(thumbDrawableId: Int): SnappingSeekBarOld {
        thumb = resources.getDrawable(thumbDrawableId)
        seekBar?.thumb = thumb
        val thumbnailWidth = thumb.intrinsicWidth
        seekBar?.setPadding(thumbnailWidth / 2, 0, thumbnailWidth / 2, 0)
        return this
    }

    fun setThumbnailColor(thumbnailColor: Int): SnappingSeekBarOld {
        thumb.setColor(thumbnailColor)
        return this
    }

    // Object -------------------------------------------------
    fun setItems(list: List<VariableSeekBarItem>?): SnappingSeekBarOld {
        if (list.isNullOrEmpty()) return this
        seekBarElementList = list
        indicatorCount = list.size
        initIndicators()
        return this
    }

    // Listener -----------------------------------------------
    fun setOnItemSelectionListener(listener: OnItemSelectionListener?): SnappingSeekBarOld {
        onItemSelectionListener = listener
        return this
    }

    // Listeners -----------------------------------------------------------------------------------
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        progressTo = progress / refine
        initThumbPosition(progress, fromUser)
        handleSetFromProgress(progress)
        if (fromUser) checkIndicatorColor()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        progressFrom = this.seekBar?.progress ?: 0
        thumbPosition = NOT_INITIALIZED_THUMB_POSITION
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        handleSnapToClosestValue(true)
    }

    interface OnItemSelectionListener {
        fun onItemSelected(itemIndex: Int, itemString: String?)
    }

    companion object {
        const val NOT_INITIALIZED_THUMB_POSITION = -1
    }
}