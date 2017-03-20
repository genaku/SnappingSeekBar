package hu.mesys.snappingseekbar.library.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import hu.mesys.snappingseekbar.R;
import hu.mesys.snappingseekbar.library.models.SeekbarElement;
import hu.mesys.snappingseekbar.library.utils.SeekbarUtils;
import hu.mesys.snappingseekbar.library.utils.UiUtils;

/**
 * User: tobiasbuchholz
 * Date: 28.07.14 | Time: 14:18
 * <p>
 * Modified by József Mezei on 07.03.2017
 */

public class SnappingSeekBar extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {
    public static final int NOT_INITIALIZED_THUMB_POSITION = -1;

    protected Context mContext;
    protected float mDensity;
    protected OnItemSelectionListener mOnItemSelectionListener;
    protected int refine = 100; //TODO: Alert H@CK

    // Seek Bar ------------------------------------------------------------------------------------
    protected SeekBar mSeekBar;

    // Indicators ----------------------------------------------------------------------------------
    protected List<View> indicatorList;
    protected List<AppCompatTextView> indicatorTextList;
    protected int reachedIndicator;
    protected int indicatorCount;
    protected int indicatorColor;
    protected float indicatorSize;
    protected int indicatorDrawableId;

    protected int indicatorTextColor;
    protected float indicatorTextMarginTop;
    protected String[] indicatorItems = new String[0];
    protected float indicatorTextSize;

    // Thumb ---------------------------------------------------------------------------------------
    protected int thumbPosition = NOT_INITIALIZED_THUMB_POSITION;
    protected Drawable thumbDrawable;

    // Progress ------------------------------------------------------------------------------------
    protected Drawable progressDrawable;
    protected int fromProgress;
    protected float toProgress;

    // Objects -------------------------------------------------------------------------------------
    protected List<SeekbarElement> seekBarElementList;

    public SnappingSeekBar(final Context context) {
        super(context);
        mContext = context;

        initDensity();
        initSeekBar();
        initDefaultValues();
    }

    protected void initDensity() {
        mDensity = mContext.getResources().getDisplayMetrics().density;
    }

    protected void initDefaultValues() {
        setProgressBaseDrawable(R.drawable.progress);

        indicatorList = new ArrayList<>();
        indicatorTextList = new ArrayList<>();
        setThumbDrawable(R.drawable.apptheme_scrubber_control_selector_holo_light);
        setIndicatorDrawable(R.drawable.circle_background);
        setIndicatorTextMargin(0, Math.round(35 * mDensity), 0, 0);
        setIndicatorTextSize(12 * mDensity);
        setIndicatorSize(11.3f * mDensity);
        indicatorTextMarginTop = 15 * mDensity;

        setProgressColor(ContextCompat.getColor(getContext(), R.color.black));
        setIndicatorColor(ContextCompat.getColor(getContext(), R.color.blue));
        setIndicatorTextColor(ContextCompat.getColor(getContext(), R.color.black));
        setThumbnailColor(ContextCompat.getColor(getContext(), R.color.green));
    }

    public SnappingSeekBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initDensity();
        initSeekBar();
        handleAttributeSet(attrs);

        if (indicatorCount == 0) return;
        initIndicators();
    }

    protected void handleAttributeSet(final AttributeSet attrs) {
        final TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.SnappingSeekBar, 0, 0);
        try {
            initThumb(typedArray);
            initIndicator(typedArray);
            initSeekbar(typedArray);
            initProgress(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    protected void initThumb(final TypedArray typedArray) {
        setThumbDrawable(typedArray.getResourceId(R.styleable.SnappingSeekBar_thumbDrawable, R.drawable.apptheme_scrubber_control_selector_holo_light));
        setThumbnailColor(typedArray.getColor(R.styleable.SnappingSeekBar_thumbnailColor, ContextCompat.getColor(getContext(), R.color.yellow)));
    }

    protected void initIndicator(final TypedArray typedArray) {
        indicatorList = new ArrayList<>();
        indicatorTextList = new ArrayList<>();
        setIndicatorDrawable(typedArray.getResourceId(R.styleable.SnappingSeekBar_indicatorDrawable, R.drawable.circle_background));
        setIndicatorSize(typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorSize, 11.3f * mDensity));
        setIndicatorColor(typedArray.getColor(R.styleable.SnappingSeekBar_indicatorColor, ContextCompat.getColor(getContext(), R.color.blue)));
        setIndicatorTextColor(typedArray.getColor(R.styleable.SnappingSeekBar_indicatorTextColor, ContextCompat.getColor(getContext(), R.color.black)));
        setIndicatorTextSize(typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextSize, 12 * mDensity));

        final int itemsArrayId = typedArray.getResourceId(R.styleable.SnappingSeekBar_indicatorTextArrayId, 0);
        if (itemsArrayId > 0) setItems(itemsArrayId);
        else setItemsAmount(typedArray.getInteger(R.styleable.SnappingSeekBar_indicatorAmount, 0));

        setIndicatorTextMargin(
                typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginStart, 0),
                typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginTop, Math.round(30 * mDensity)),
                typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginEnd, 0),
                typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginBottom, 0)
        );
        float margin = typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMargin, -1);
        if (margin != -1) setIndicatorTextMargin(margin);
        indicatorTextMarginTop = 15 * mDensity;
    }

    protected void initSeekbar(final TypedArray typedArray) {

        setSeekBarPadding(
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPaddingStart, mSeekBar.getPaddingLeft() / mDensity), //Math.round(6 * mDensity)
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPaddingTop, 0),
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPaddingEnd, mSeekBar.getPaddingRight() / mDensity), //Math.round(6 * mDensity)
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPaddingBottom, 0));

        float seekbarPadding = typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPadding, -1);
        if (seekbarPadding != -1) setSeekBarPadding(seekbarPadding);


        setSeekBarMargin(
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMarginStart, 0),
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMarginTop, 0),
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMarginEnd, 0),
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMarginBottom, 0));
        float seekbarMargin = typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMargin, -1);
        if (seekbarMargin != -1) setSeekBarMargin(seekbarMargin);
    }

    protected void initProgress(final TypedArray typedArray) {
        setProgressBaseDrawable(typedArray.getResourceId(R.styleable.SnappingSeekBar_progressDrawable, R.drawable.progress));
        setProgressColor(typedArray.getColor(R.styleable.SnappingSeekBar_progressColor, ContextCompat.getColor(getContext(), R.color.black)));
    }

    public void setItems(final int itemsArrayId) {
        setItems(mContext.getResources().getStringArray(itemsArrayId));
    }

    public void setItems(final String[] items) {
        if (items.length > 1) {
            indicatorItems = items;
            indicatorCount = indicatorItems.length;
        } else {
            throw new IllegalStateException("SnappingSeekBar has to contain at least 2 items");
        }
    }

    public void setItemsAmount(final int itemsAmount) {
        indicatorCount = itemsAmount;
    }

    private float[] checkMarginPadding(float[] marginsPaddings) {
        if (SeekbarUtils.isSetValue(marginsPaddings)) {
            SeekbarUtils.normalizeValues(marginsPaddings);
            return marginsPaddings;
        } else return null;
    }

    // Build UI ------------------------------------------------------------------------------------
    // Snapping Seek Bar ----------------------------------
    protected void initSeekBar() {
        removeView(mSeekBar);
        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSeekBar = new AppCompatSeekBar(getContext());
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setMax(100 * refine);
        addView(mSeekBar, params);
    }

    // Indicators -------------------------------------------
    protected void initIndicators() {
        UiUtils.waitForLayoutPrepared(mSeekBar, new UiUtils.LayoutPreparedListener() {
            @Override
            public void onLayoutPrepared(final View preparedView) {
                final int seekBarWidth = preparedView.getWidth();
                initIndicators(seekBarWidth);
            }
        });
    }

    protected void initIndicators(final int seekBarWidth) {
        removeIndicator();
        for (int i = 0; i < indicatorCount; i++) {
            addIndicator(seekBarWidth, i);
            addIndicatorTextIfNeeded(seekBarWidth, i);
        }
    }

    protected void addIndicator(final int seekBarWidth, final int index) {
        final float sectionFactor = SeekbarUtils.sectionFactor(indicatorCount);
        final float seekBarWidthWithoutThumbOffset = seekBarWidth - getThumbnailWidth();

        final LayoutParams indicatorParams = new LayoutParams(Math.round(indicatorSize), Math.round(indicatorSize));
        indicatorParams.leftMargin = Math.round(seekBarWidthWithoutThumbOffset / 100 * index * sectionFactor + (getThumbnailWidth() / 2 - indicatorSize / 2));
        indicatorParams.topMargin = thumbDrawable.getIntrinsicHeight() / 2 - Math.round(indicatorSize / 2);

        View indicator = createIndicator(index);
        addView(indicator, indicatorParams);
        indicatorList.add(indicator);
    }

    protected void removeIndicator() {
        if (indicatorList == null) return;
        for (int i = 0; i < indicatorList.size(); i++) {
            removeView(indicatorList.get(i));
            removeView(indicatorTextList.get(i));
        }
        indicatorList.clear();
    }

    protected View createIndicator(int index) {
        final View indicator = new View(mContext);

        checkIndicatorDrawable(index);
        Drawable drawable = getResources().getDrawable(indicatorDrawableId);
        indicator.setBackgroundDrawable(drawable);
        UiUtils.setColor(indicator.getBackground(), indicatorColor);

        return indicator;
    }

    protected boolean checkIndicatorDrawable(int index) { // Change indicator drawable and/ or color from list
        if (seekBarElementList == null || seekBarElementList.size() < 1) return false;

        SeekbarElement element = seekBarElementList.get(index);
        indicatorDrawableId = element.getIndicatorDrawableId();
        if (element.getIndicatorReachedColor() == -1 || toProgress >= indicatorList.size() * (100 / indicatorCount))
            indicatorColor = element.getIndicatorReachedColor();
        else indicatorColor = element.getIndicatorColor();

        return true;
    }

    protected void checkIndicatorColor() {
        final float sectionLength = SeekbarUtils.sectionFactor(indicatorCount);
        final int reach = Math.round(toProgress / sectionLength);

        if (reachedIndicator == reach || indicatorList == null || indicatorList.size() == 0 || seekBarElementList == null)
            return;

        if (reachedIndicator > reach) changeIndicatorColor(reachedIndicator, reach + 1, true);
        else changeIndicatorColor(reach, reachedIndicator, false);
        reachedIndicator = reach;
    }

    protected void changeIndicatorColor(int reach, int from, boolean back) {
        for (int i = from; i <= reach; i++) {
            View view = indicatorList.get(i);
            int color = back ? seekBarElementList.get(i).getIndicatorColor() : seekBarElementList.get(i).getIndicatorReachedColor();
            UiUtils.setColor(view.getBackground(), color);
        }

    }

    protected void addIndicatorTextIfNeeded(final int completeSeekBarWidth, final int index) {
        if ((indicatorItems != null && indicatorItems.length == indicatorCount) || (seekBarElementList != null && seekBarElementList.size() == indicatorCount)) {
            addTextIndicator(completeSeekBarWidth, index);
        }
    }

    protected void addTextIndicator(final int completeSeekBarWidth, final int index) {
        final int thumbnailWidth = thumbDrawable.getIntrinsicWidth();
        final float sectionFactor = SeekbarUtils.sectionFactor(indicatorCount);
        final float seekBarWidthWithoutThumbOffset = completeSeekBarWidth - thumbnailWidth;
        final LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final int numberLeftMargin = Math.round(seekBarWidthWithoutThumbOffset / 100 * index * sectionFactor + thumbnailWidth / 2);
        final int numberTopMargin = Math.round((thumbDrawable.getIntrinsicHeight() / 2 - Math.round(indicatorSize / 2)) + indicatorTextMarginTop);
        checkIndicatorText(index);
        AppCompatTextView view = createIndicatorText(index);
        textParams.topMargin = numberTopMargin;
        addView(view, textParams);
        indicatorTextList.add(view);
        UiUtils.waitForLayoutPrepared(view, createTextIndicatorLayoutPreparedListener(numberLeftMargin));
    }

    protected AppCompatTextView createIndicatorText(int index) {
        final AppCompatTextView textIndicator = new AppCompatTextView(mContext);
        textIndicator.setText(seekBarElementList != null && seekBarElementList.size() > 0 ?
                seekBarElementList.get(index).getIndicatorText() : indicatorItems[index]);
        textIndicator.setTextSize(TypedValue.COMPLEX_UNIT_SP, indicatorTextSize / mDensity);
        textIndicator.setTextColor(indicatorTextColor);

        return textIndicator;
    }

    protected UiUtils.LayoutPreparedListener createTextIndicatorLayoutPreparedListener(final int numberLeftMargin) {
        return new UiUtils.LayoutPreparedListener() {
            @Override
            public void onLayoutPrepared(final View preparedView) {
                final int layoutWidth = getWidth() - getPaddingRight();
                final int viewWidth = preparedView.getWidth();
                final int leftMargin = numberLeftMargin - viewWidth / 2;
                final int paddingLeft = getPaddingLeft();
                final int finalMargin = leftMargin < paddingLeft ? paddingLeft : leftMargin + viewWidth > layoutWidth ? layoutWidth - viewWidth : leftMargin;
                UiUtils.setLeftMargin(preparedView, finalMargin);
            }
        };
    }


    protected boolean checkIndicatorText(int index) {
        if (seekBarElementList == null || seekBarElementList.size() < 1) return false;

        SeekbarElement element = seekBarElementList.get(index);
        indicatorTextColor = element.getIndicatorTextColor();
        return true;
    }

    // Others --------------------------------------------------------------------------------------
    protected void initThumbPosition(final int progress, final boolean fromUser) {
        if (thumbPosition == NOT_INITIALIZED_THUMB_POSITION && fromUser) {
            thumbPosition = progress;
        }
    }

    protected void handleSetFromProgress(final int progress) {
        final int slidingDelta = progress - thumbPosition;
        if (slidingDelta > 1 || slidingDelta < -1) {
            fromProgress = progress;
        }
    }

    protected void handleSnapToClosestValue(boolean animate) {
        final float sectionLength = SeekbarUtils.sectionFactor(indicatorCount);
        final int selectedSection = Math.round(toProgress / sectionLength);
        final float valueToSnap = selectedSection * sectionLength;
        animateProgressBar(valueToSnap, animate);
        invokeItemSelected(selectedSection);
    }

    protected void animateProgressBar(final float toProgress, boolean animate) {
        final ProgressBarAnimation anim = new ProgressBarAnimation(mSeekBar, fromProgress, Math.round(toProgress * refine));
        anim.setDuration(animate ? 200 : 0);
        startAnimation(anim);
    }

    protected void invokeItemSelected(final float selectedSection) {
        if (mOnItemSelectionListener != null) {
            mOnItemSelectionListener.onItemSelected((int) selectedSection, getItemString((int) selectedSection));
        }
    }

    protected String getItemString(final int index) {
        if (indicatorItems.length > index) {
            return indicatorItems[index];
        }
        return "";
    }

    // Getters -------------------------------------------------------------------------------------
    public Drawable getProgressDrawable() {
        return progressDrawable;
    }

    public Drawable getThumb() {
        return thumbDrawable;
    }

    public int getProgress() {
        return mSeekBar.getProgress();
    }

    protected float getProgressForIndex(final int index) {
        final float sectionLength = 100 / (indicatorCount - 1);
        return index * sectionLength;
    }

    public int getSelectedItemIndex() {
        final float sectionLength = 100 / (indicatorCount - 1);
        return (int) ((toProgress / sectionLength) + 0.5);
    }

    private int getThumbnailWidth() {
        return thumbDrawable.getIntrinsicWidth();
    }

    private int getIndicatorTextTopMargin() {
        if (indicatorTextList == null || indicatorTextList.size() == 0) return 0;
        AppCompatTextView tv = indicatorTextList.get(0);
        RelativeLayout.LayoutParams params = (LayoutParams) tv.getLayoutParams();
        return params.topMargin;
    }

    // Setters ------------------------------------------------------------------------------------
    // SeekBar ---------------------------------------------
    public SnappingSeekBar setSeekbarColor(int seekbarColor) {
        mSeekBar.setBackgroundColor(seekbarColor);
        return this;
    }

    private SnappingSeekBar setSeekBarPadding(float[] padding) {
        return setSeekBarPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    public SnappingSeekBar setSeekBarPadding(float padding) {
        return setSeekBarPadding(padding, padding, padding, padding);
    }

    public SnappingSeekBar setSeekBarPadding(float paddingLeft, float paddingTop, float paddingRight, float paddingBottom) {
        mSeekBar.setPadding(
                UiUtils.getDPinPixel(getContext(), paddingLeft),
                UiUtils.getDPinPixel(getContext(), paddingTop),
                UiUtils.getDPinPixel(getContext(), paddingRight),
                UiUtils.getDPinPixel(getContext(), paddingBottom)
        );
        return this;
    }

    private SnappingSeekBar setSeekBarMargin(float margin[]) {
        return setSeekBarMargin(margin[0], margin[1], margin[2], margin[3]);
    }

    public SnappingSeekBar setSeekBarMargin(float margin) {
        return setSeekBarMargin(margin, margin, margin, margin);
    }

    public SnappingSeekBar setSeekBarMargin(float marginLeft, float marginTop, float marginRight, float marginBottom) {
        RelativeLayout.LayoutParams params = (LayoutParams) mSeekBar.getLayoutParams();
        params.setMargins(
                UiUtils.getDPinPixel(getContext(), marginLeft),
                UiUtils.getDPinPixel(getContext(), marginTop),
                UiUtils.getDPinPixel(getContext(), marginRight),
                UiUtils.getDPinPixel(getContext(), marginBottom)
        );
        return this;
    }

    // Indicators ------------------------------------------
    public SnappingSeekBar setIndicatorDrawable(final int indicatorDrawableId) {
        this.indicatorDrawableId = indicatorDrawableId;
        return this;
    }

    public SnappingSeekBar setIndicatorColor(final int indicatorColor) {
        this.indicatorColor = indicatorColor;
        return this;
    }

    private SnappingSeekBar setIndicatorSize(final float indicatorSize) {
        this.indicatorSize = indicatorSize;
        return this;
    }

    public SnappingSeekBar setIndicatorTextColor(final int indicatorTextColor) {
        this.indicatorTextColor = indicatorTextColor;
        return this;
    }

    public SnappingSeekBar setIndicatorTextMargin(float margin) {
        return setIndicatorTextMargin(margin, margin, margin, margin);
    }

    public SnappingSeekBar setIndicatorTextMargin(float marginLeft, float marginTop, float marginRight, float marginBottom) {
        for (AppCompatTextView tv : indicatorTextList) {
            RelativeLayout.LayoutParams params = (LayoutParams) tv.getLayoutParams();
            params.setMargins(UiUtils.getDPinPixel(getContext(), marginLeft),
                    UiUtils.getDPinPixel(getContext(), marginTop),
                    UiUtils.getDPinPixel(getContext(), marginRight),
                    UiUtils.getDPinPixel(getContext(), marginBottom));
        }
        return this;
    }

    private SnappingSeekBar setIndicatorTextSize(final float textSize) {
        this.indicatorTextSize = textSize;
        return this;
    }

    // Progress --------------------------------------------
    public void setProgress(final int progress) {
        toProgress = progress;
        handleSnapToClosestValue(true);
    }

    public SnappingSeekBar setProgressBaseDrawable(int progressBaseDrawable) {
        progressDrawable = getResources().getDrawable(progressBaseDrawable);
        mSeekBar.setProgressDrawable(progressDrawable);
        return this;
    }

    public SnappingSeekBar setProgressToIndex(final int index) {
        toProgress = getProgressForIndex(index);
        reachedIndicator = index;
        handleSnapToClosestValue(false);
        //mSeekBar.setProgress((int) toProgress * refine);
        return this;
    }

    public SnappingSeekBar setProgressToIndexWithAnimation(final int index) {
        toProgress = getProgressForIndex(index);
        animateProgressBar(toProgress, true);
        return this;
    }

    public SnappingSeekBar setProgressColor(final int progressColor) {
        UiUtils.setColor(progressDrawable, progressColor);
        return this;
    }

    // Thumbnail --------------------------------------------
    public SnappingSeekBar setThumbDrawable(final int thumbDrawableId) {
        thumbDrawable = getResources().getDrawable(thumbDrawableId);
        mSeekBar.setThumb(thumbDrawable);

        final int thumbnailWidth = thumbDrawable.getIntrinsicWidth();
        mSeekBar.setPadding(thumbnailWidth / 2, 0, thumbnailWidth / 2, 0);
        return this;
    }

    public SnappingSeekBar setThumbnailColor(final int thumbnailColor) {
        UiUtils.setColor(thumbDrawable, thumbnailColor);
        return this;
    }

    // Object -------------------------------------------------
    public SnappingSeekBar setItems(List<SeekbarElement> list) {
        if (list == null || list.size() < 1) return this;

        this.seekBarElementList = list;
        indicatorCount = list.size();
        initIndicators();
        return this;
    }

    // Listener -----------------------------------------------
    public SnappingSeekBar setOnItemSelectionListener(final OnItemSelectionListener listener) {
        mOnItemSelectionListener = listener;
        return this;
    }

    // Listeners -----------------------------------------------------------------------------------
    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        toProgress = progress / refine;
        initThumbPosition(progress, fromUser);
        handleSetFromProgress(progress);

        if (fromUser) checkIndicatorColor();
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
        fromProgress = mSeekBar.getProgress();
        thumbPosition = NOT_INITIALIZED_THUMB_POSITION;
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
        handleSnapToClosestValue(true);
    }

    public interface OnItemSelectionListener {
        void onItemSelected(final int itemIndex, final String itemString);
    }
}