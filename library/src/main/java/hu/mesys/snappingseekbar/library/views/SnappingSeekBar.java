package hu.mesys.snappingseekbar.library.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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

    // Question ------------------------------------------------------------------------------------
    protected AppCompatTextView mQuestion;
    protected String question;
    protected int questionColor;
    protected int questionGravity;
    protected float questionTextSize;
    protected int[] questionPadding;
    protected int[] questionMargin;

    // Seek Bar ------------------------------------------------------------------------------------
    protected SeekBar mSeekBar;
    protected int seekbarColor;
    protected int[] seekbarPadding;
    protected int[] seekbarMargin;

    protected int progressBaseDrawable;
    protected int progressColor;

    // Indicators ----------------------------------------------------------------------------------
    protected List<View> indicatorList;
    protected int reachedIndicator;
    protected int indicatorCount;
    protected int indicatorColor;
    protected float indicatorSize;
    protected int indicatorDrawableId;

    protected int indicatorTextColor;
    protected int[] indicatorTextMargin;
    protected String[] indicatorItems = new String[0];
    protected float indicatorTextSize;

    // Thumb ---------------------------------------------------------------------------------------
    protected int thumbPosition = NOT_INITIALIZED_THUMB_POSITION;
    protected Drawable thumbDrawable;
    protected int thumbDrawableId;
    protected int thumbnailColor;

    // Progress ------------------------------------------------------------------------------------
    protected Drawable progressDrawable;
    protected int fromProgress;
    protected float toProgress;

    // Boundary Texts ------------------------------------------------------------------------------
    protected int boundTextColor;
    protected int[] boundTextMargin;
    protected String boundTextStart;
    protected String boundTextEnd;
    protected float boundTextSize;
    protected int boundViewLength;

    // Objects -------------------------------------------------------------------------------------
    protected List<SeekbarElement> seekBarElementList;

    public SnappingSeekBar(final Context context) {
        super(context);
        mContext = context;

        initDensity();
        initDefaultValues();
    }

    protected void initDensity() {
        mDensity = mContext.getResources().getDisplayMetrics().density;
    }

    protected void initDefaultValues() {
        questionColor = ContextCompat.getColor(getContext(), R.color.black);
        questionTextSize = 14;
        questionGravity = Gravity.START;

        progressBaseDrawable = R.drawable.progress;
        seekbarColor = -1;

        indicatorList = new ArrayList<>();
        thumbDrawableId = R.drawable.apptheme_scrubber_control_selector_holo_light;
        indicatorDrawableId = R.drawable.circle_background;
        indicatorTextMargin = new int[]{0, Math.round(35 * mDensity), 0, 0};
        indicatorTextSize = 12 * mDensity;
        indicatorSize = 11.3f * mDensity;
        boundTextColor = ContextCompat.getColor(getContext(), R.color.black);
        boundTextSize = 11.3f * mDensity;
        boundViewLength = 1;

        progressColor = ContextCompat.getColor(getContext(), R.color.black);
        indicatorColor = ContextCompat.getColor(getContext(), R.color.blue);
        thumbnailColor = ContextCompat.getColor(getContext(), R.color.yellow);
        indicatorTextColor = ContextCompat.getColor(getContext(), R.color.black);
        thumbnailColor = ContextCompat.getColor(getContext(), R.color.green);
    }

    public SnappingSeekBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initDensity();
        handleAttributeSet(attrs);

        addQuestionToUI();
        addSnappingSeekBarToUI();
    }

    protected void handleAttributeSet(final AttributeSet attrs) {
        final TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.SnappingSeekBar, 0, 0);
        try {
            initQuestion(typedArray);
            initThumb(typedArray);
            initIndicator(typedArray);
            initSeekbar(typedArray);
            initProgress(typedArray);
            initBoundaryText(typedArray);
        } finally {
            typedArray.recycle();
        }
    }

    public SnappingSeekBar build() {
        addQuestionToUI();
        addSnappingSeekBarToUI();

        return this;
    }

    protected void initQuestion(final TypedArray typedArray) {
        question = typedArray.getString(R.styleable.SnappingSeekBar_questionText);
        questionColor = typedArray.getColor(R.styleable.SnappingSeekBar_questionColor, ContextCompat.getColor(getContext(), R.color.black));
        questionTextSize = typedArray.getInt(R.styleable.SnappingSeekBar_questionTextSize, 14);
        int gravity = typedArray.getInt(R.styleable.SnappingSeekBar_questionGravity, 0);

        if (gravity == 0) questionGravity = Gravity.START;
        else if (gravity == 1) questionGravity = Gravity.END;
        else if (gravity == 2) questionGravity = Gravity.CENTER;

        float padding = typedArray.getDimension(R.styleable.SnappingSeekBar_questionPadding, -1);
        float[] paddings = new float[]{
                typedArray.getDimension(R.styleable.SnappingSeekBar_questionPaddingStart, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_questionPaddingTop, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_questionPaddingEnd, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_questionPaddingBottom, -1)
        };
        float margin = typedArray.getDimension(R.styleable.SnappingSeekBar_questionMargin, -1);
        float[] margins = new float[]{
                typedArray.getDimension(R.styleable.SnappingSeekBar_questionMarginStart, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_questionMarginTop, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_questionMarginEnd, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_questionMarginBottom, -1)
        };

        if (padding == -1) paddings = checkMarginPadding(paddings);
        if (paddings != null)
            setQuestionPadding(
                    paddings[0] != -1 ? paddings[0] : padding,
                    paddings[1] != -1 ? paddings[1] : padding,
                    paddings[2] != -1 ? paddings[2] : padding,
                    paddings[3] != -1 ? paddings[3] : padding
            );

        if (margin == -1) margins = checkMarginPadding(margins);
        if (margins != null)
            setQuestionMargin(
                    margins[0] != -1 ? margins[0] : margin,
                    margins[1] != -1 ? margins[1] : margin,
                    margins[2] != -1 ? margins[2] : margin,
                    margins[3] != -1 ? margins[3] : margin
            );
    }

    protected void initThumb(final TypedArray typedArray) {
        thumbDrawableId = typedArray.getResourceId(R.styleable.SnappingSeekBar_thumbDrawable, R.drawable.apptheme_scrubber_control_selector_holo_light);
        thumbnailColor = typedArray.getColor(R.styleable.SnappingSeekBar_thumbnailColor, ContextCompat.getColor(getContext(), R.color.yellow));
    }

    protected void initIndicator(final TypedArray typedArray) {
        indicatorList = new ArrayList<>();
        indicatorDrawableId = typedArray.getResourceId(R.styleable.SnappingSeekBar_indicatorDrawable, R.drawable.circle_background);
        indicatorSize = typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorSize, 11.3f * mDensity);
        indicatorColor = typedArray.getColor(R.styleable.SnappingSeekBar_indicatorColor, ContextCompat.getColor(getContext(), R.color.blue));
        indicatorTextColor = typedArray.getColor(R.styleable.SnappingSeekBar_indicatorTextColor, ContextCompat.getColor(getContext(), R.color.black));
        indicatorTextSize = typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextSize, 12 * mDensity);

        final int itemsArrayId = typedArray.getResourceId(R.styleable.SnappingSeekBar_indicatorTextArrayId, 0);
        if (itemsArrayId > 0) setItems(itemsArrayId);
        else setItemsAmount(typedArray.getInteger(R.styleable.SnappingSeekBar_indicatorAmount, 10));

        float margin = typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMargin, -1);
        float[] margins = new float[]{
                typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginStart, 0),
                typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginTop, Math.round(30 * mDensity)),
                typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginEnd, 0),
                typedArray.getDimension(R.styleable.SnappingSeekBar_indicatorTextMarginBottom, 0)
        };

        if (margin != -1) setIndicatorTextMargin(margin);
        else setIndicatorTextMargin(margins);
    }

    protected void initSeekbar(final TypedArray typedArray) {
        float seekbarPadding = typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPadding, -1);
        float[] seekbarPaddings = new float[]{
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPaddingStart, -1), //Math.round(6 * mDensity)
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPaddingTop, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPaddingEnd, -1), //Math.round(6 * mDensity)
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarPaddingBottom, -1)
        };

        float seekbarMargin = typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMargin, -1);
        float[] seekbarMargins = new float[]{
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMarginStart, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMarginTop, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMarginEnd, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_seekbarMarginBottom, -1)
        };

        if (seekbarPadding == -1) seekbarPaddings = checkMarginPadding(seekbarPaddings);
        if (seekbarPaddings != null)
            setSeekBarPadding(
                    seekbarPaddings[0] != -1 ? seekbarPaddings[0] : seekbarPadding,
                    seekbarPaddings[1] != -1 ? seekbarPaddings[1] : seekbarPadding,
                    seekbarPaddings[2] != -1 ? seekbarPaddings[2] : seekbarPadding,
                    seekbarPaddings[3] != -1 ? seekbarPaddings[3] : seekbarPadding
            );

        if (seekbarMargin == -1) seekbarMargins = checkMarginPadding(seekbarMargins);
        if (seekbarMargins != null)
            setSeekBarMargin(
                    seekbarMargins[0] != -1 ? seekbarMargins[0] : seekbarMargin,
                    seekbarMargins[1] != -1 ? seekbarMargins[1] : seekbarMargin,
                    seekbarMargins[2] != -1 ? seekbarMargins[2] : seekbarMargin,
                    seekbarMargins[3] != -1 ? seekbarMargins[3] : seekbarMargin
            );
    }

    protected void initProgress(final TypedArray typedArray) {
        progressBaseDrawable = typedArray.getResourceId(R.styleable.SnappingSeekBar_progressDrawable, R.drawable.progress);
        progressColor = typedArray.getColor(R.styleable.SnappingSeekBar_progressColor, ContextCompat.getColor(getContext(), R.color.black));
    }

    protected void initBoundaryText(final TypedArray typedArray) {
        boundTextStart = typedArray.getString(R.styleable.SnappingSeekBar_boundStartText);
        boundTextEnd = typedArray.getString(R.styleable.SnappingSeekBar_boundEndText);
        questionColor = typedArray.getColor(R.styleable.SnappingSeekBar_boundTextColor, ContextCompat.getColor(getContext(), R.color.black));
        boundTextSize = typedArray.getDimensionPixelSize(R.styleable.SnappingSeekBar_boundTextSize, 14);

        float margin = typedArray.getDimension(R.styleable.SnappingSeekBar_boundTextMargin, -1);
        float[] margins = new float[]{
                typedArray.getDimension(R.styleable.SnappingSeekBar_boundTextMarginLeft, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_boundTextMarginTop, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_boundTextMarginRight, -1),
                typedArray.getDimension(R.styleable.SnappingSeekBar_boundTextMarginBottom, -1)
        };

        if (margin == -1) margins = checkMarginPadding(margins);
        if (margins != null)
            setBoundTextMargin(
                    margins[0] != 0 ? margins[0] : margin,
                    margins[1] != 0 ? margins[1] : margin,
                    margins[2] != 0 ? margins[2] : margin,
                    margins[3] != 0 ? margins[3] : margin
            );
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
        if (itemsAmount > 1) {
            indicatorCount = itemsAmount;
        } else {
            throw new IllegalStateException("SnappingSeekBar has to contain at least 2 items");
        }
    }

    protected void setDrawablesToSeekBar() {
        final Resources resources = getResources();
        progressDrawable = resources.getDrawable(progressBaseDrawable);
        thumbDrawable = resources.getDrawable(thumbDrawableId);
        UiUtils.setColor(progressDrawable, progressColor);
        UiUtils.setColor(thumbDrawable, thumbnailColor);
        mSeekBar.setProgressDrawable(progressDrawable);
        mSeekBar.setThumb(thumbDrawable);
        final int thumbnailWidth = thumbDrawable.getIntrinsicWidth();
        mSeekBar.setPadding(thumbnailWidth / 2, 0, thumbnailWidth / 2, 0);
    }

    private float[] checkMarginPadding(float[] marginsPaddings) {
        if (SeekbarUtils.isSetValue(marginsPaddings)) {
            SeekbarUtils.normalizeValues(marginsPaddings);
            return marginsPaddings;
        } else return null;
    }

    // Build UI ------------------------------------------------------------------------------------
    // Question -------------------------------------------
    protected void addQuestionToUI() {
        if (question == null || question.isEmpty()) return;

        mQuestion = new AppCompatTextView(getContext());
        mQuestion.setId(R.id.question);
        mQuestion.setText(question);
        mQuestion.setTextColor(questionColor);
        mQuestion.setTextSize(questionTextSize);
        mQuestion.setGravity(questionGravity);

        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (questionPadding != null)
            mQuestion.setPadding(questionPadding[0], questionPadding[1], questionPadding[2], questionPadding[3]);
        if (questionMargin != null)
            params.setMargins(questionMargin[0], questionMargin[1], questionMargin[2], questionMargin[3]);

        addView(mQuestion, params);
    }

    // Snapping Seek Bar ----------------------------------
    protected void addSnappingSeekBarToUI() {
        UiUtils.waitForLayoutPrepared(this, new UiUtils.LayoutPreparedListener() { //TODO:  for debug
            @Override
            public void onLayoutPrepared(final View preparedView) {
                initSeekBar();
                initIndicators();
            }
        });
    }

    protected void initSeekBar() {
        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mQuestion != null) params.addRule(RelativeLayout.BELOW, mQuestion.getId());

        mSeekBar = new SeekBar(mContext);
        mSeekBar.setOnSeekBarChangeListener(this);
//        mSeekBar.setLayoutParams(params);
        mSeekBar.setMax(100 * refine);
        setDrawablesToSeekBar();

        if (seekbarPadding != null)
            mSeekBar.setPadding(seekbarPadding[0], seekbarPadding[1], seekbarPadding[2], seekbarPadding[3]);
        if (seekbarMargin != null)
            params.setMargins(seekbarMargin[0], seekbarMargin[1], seekbarMargin[2], seekbarMargin[3]);
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
        for (int i = 0; i < indicatorCount; i++) {
            addIndicator(seekBarWidth, i);
            addIndicatorTextIfNeeded(seekBarWidth, i);
        }
    }

    protected void addIndicator(final int seekBarWidth, final int index) {
        final float sectionFactor = SeekbarUtils.sectionFactor(indicatorCount);
        final float seekBarWidthWithoutThumbOffset = seekBarWidth - getThumbnailWidth();

        final LayoutParams indicatorParams = new LayoutParams(Math.round(indicatorSize), Math.round(indicatorSize));
        if (mQuestion != null) indicatorParams.addRule(RelativeLayout.BELOW, mQuestion.getId());
        indicatorParams.leftMargin = Math.round(seekBarWidthWithoutThumbOffset / 100 * index * sectionFactor + (getThumbnailWidth() / 2 - indicatorSize / 2));
        indicatorParams.topMargin = thumbDrawable.getIntrinsicHeight() / 2 - Math.round(indicatorSize / 2);

        View indicator = createIndicator(index);
        addView(indicator, indicatorParams);
        indicatorList.add(indicator);
    }

    protected View createIndicator(int index) {
        final View indicator = new View(mContext);

        checkIndicatorDrawable(index);
        indicator.setBackgroundResource(indicatorDrawableId);
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
        if (mQuestion != null) textParams.addRule(RelativeLayout.BELOW, mQuestion.getId());

        final int numberLeftMargin = Math.round(seekBarWidthWithoutThumbOffset / 100 * index * sectionFactor + thumbnailWidth / 2);

        checkIndicatorText(index);
        View view = createIndicatorText(index);
        textParams.setMargins(indicatorTextMargin[0], indicatorTextMargin[1], indicatorTextMargin[2], indicatorTextMargin[3]);
        addView(view, textParams);
        UiUtils.waitForLayoutPrepared(view, createTextIndicatorLayoutPreparedListener(numberLeftMargin));

        if (index == indicatorCount - 1) view.post(new Runnable() {
            @Override
            public void run() {
                addBoundsText();
            }
        });
    }

    protected View createIndicatorText(int index) {
        final TextView textIndicator = new TextView(mContext);
        textIndicator.setText(seekBarElementList != null && seekBarElementList.size() > 0 ?
                seekBarElementList.get(index).getIndicatorText() : indicatorItems[index]);
        textIndicator.setTextSize(indicatorTextSize / mDensity);
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

    // Bounds Text----------------------------------------------------------------------------------
    protected void addBoundsText() {
        if ((boundTextStart == null || boundTextStart.isEmpty()) && (boundTextEnd == null || boundTextEnd.isEmpty()))
            return;
        int bound = 0;

        // FIXME no question
        for (int i = 2; (i - 2) < Math.pow(2, boundViewLength); i += 2) { //sequence 1: question, 2: seekbar, 3: indicator, 4: indicatorText, 5: indicator...
            View view = getChildAt(i);
            bound += UiUtils.getXPositionOfView(view);
        }

        addTextView(bound, boundTextStart, RelativeLayout.ALIGN_PARENT_LEFT);
        addTextView(bound, boundTextEnd, RelativeLayout.ALIGN_PARENT_RIGHT);
    }

    protected void addTextView(int bound, String text, int verb) {
        final LayoutParams textParams = new LayoutParams(bound, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mQuestion != null) textParams.addRule(RelativeLayout.BELOW, mQuestion.getId());
        final TextView textBound = new TextView(mContext);

        textBound.setText(text);
        textBound.setTextSize(boundTextSize / mDensity);
        textBound.setTextColor(boundTextColor);
//        textBound.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellow));
        textBound.setGravity(verb == RelativeLayout.ALIGN_PARENT_RIGHT ? Gravity.END : Gravity.START);

        if (boundTextMargin != null) {
            int topMargin = indicatorTextMargin[1] + Math.round(boundTextSize) + UiUtils.getDPinPixel(getContext(), boundTextMargin[1]);
            textParams.setMargins(boundTextMargin[0], topMargin, boundTextMargin[2], boundTextMargin[3]);
        }
        textParams.addRule(verb);
        addView(textBound, textParams);
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

    public void setProgress(final int progress) {
        toProgress = progress;
        handleSnapToClosestValue(true);
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

    // Setters -------------------------------------------------------------------------------------
    // Question -------------------------------------------
    public SnappingSeekBar setQuestion(String question) {
        this.question = question;
        return this;
    }

    public SnappingSeekBar setQuestionPadding(float[] padding) {
        return setQuestionPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    public SnappingSeekBar setQuestionPadding(float padding) {
        return setQuestionPadding(padding, padding, padding, padding);
    }

    public SnappingSeekBar setQuestionPadding(float paddingLeft, float paddingTop, float paddingRight, float paddingBottom) {
        questionPadding = new int[]{
                UiUtils.getDPinPixel(getContext(), paddingLeft),
                UiUtils.getDPinPixel(getContext(), paddingTop),
                UiUtils.getDPinPixel(getContext(), paddingRight),
                UiUtils.getDPinPixel(getContext(), paddingBottom)
        };
        return this;
    }

    public SnappingSeekBar setQuestionMargin(float margin) {
        return setQuestionMargin(margin, margin, margin, margin);
    }

    public SnappingSeekBar setQuestionMargin(float marginLeft, float marginTop, float marginRight, float marginBottom) {
        questionMargin = new int[]{
                UiUtils.getDPinPixel(getContext(), marginLeft),
                UiUtils.getDPinPixel(getContext(), marginTop),
                UiUtils.getDPinPixel(getContext(), marginRight),
                UiUtils.getDPinPixel(getContext(), marginBottom)
        };
        return this;
    }

    public SnappingSeekBar setQuestionColor(int questionColor) {
        this.questionColor = questionColor;
        return this;
    }

    public SnappingSeekBar setQuestionTextSize(float questionTextSize) {
        this.questionTextSize = questionTextSize;
        return this;
    }

    public SnappingSeekBar setQuestionGravity(int questionGravity) {
        this.questionGravity = questionGravity;
        return this;
    }

    // SeekBar ---------------------------------------------
    public SnappingSeekBar setSeekbarColor(int seekbarColor) {
        this.seekbarColor = seekbarColor;
        return this;
    }

    private SnappingSeekBar setSeekBarPadding(float[] padding) {
        return setSeekBarPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    public SnappingSeekBar setSeekBarPadding(float padding) {
        return setSeekBarPadding(padding, padding, padding, padding);
    }

    public SnappingSeekBar setSeekBarPadding(float paddingLeft, float paddingTop, float paddingRight, float paddingBottom) {
        this.seekbarPadding = new int[]{
                UiUtils.getDPinPixel(getContext(), paddingLeft),
                UiUtils.getDPinPixel(getContext(), paddingTop),
                UiUtils.getDPinPixel(getContext(), paddingRight),
                UiUtils.getDPinPixel(getContext(), paddingBottom)
        };
        return this;
    }

    private SnappingSeekBar setSeekBarMargin(float margin[]) {
        return setSeekBarMargin(margin[0], margin[1], margin[2], margin[3]);
    }

    public SnappingSeekBar setSeekBarMargin(float margin) {
        return setSeekBarMargin(margin, margin, margin, margin);
    }

    public SnappingSeekBar setSeekBarMargin(float marginLeft, float marginTop, float marginRight, float marginBottom) {
        this.seekbarMargin = new int[]{
                UiUtils.getDPinPixel(getContext(), marginLeft),
                UiUtils.getDPinPixel(getContext(), marginTop),
                UiUtils.getDPinPixel(getContext(), marginRight),
                UiUtils.getDPinPixel(getContext(), marginBottom)
        };
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

    public SnappingSeekBar setIndicatorSize(final int indicatorSize) {
        this.indicatorSize = mDensity * indicatorSize;
        return this;
    }

    public SnappingSeekBar setIndicatorTextColor(final int indicatorTextColor) {
        this.indicatorTextColor = indicatorTextColor;
        return this;
    }

    private void setIndicatorTextMargin(float[] margin) {
        indicatorTextMargin = new int[]{
                (int) margin[0],
                (int) margin[1],
                (int) margin[2],
                (int) margin[3]
        };
    }

    public SnappingSeekBar setIndicatorTextMargin(float margin) {
        return setIndicatorTextMargin(margin, margin, margin, margin);
    }

    public SnappingSeekBar setIndicatorTextMargin(float marginLeft, float marginTop, float marginRight, float marginBottom) {
        indicatorTextMargin = new int[]{
                UiUtils.getDPinPixel(getContext(), marginLeft),
                UiUtils.getDPinPixel(getContext(), marginTop),
                UiUtils.getDPinPixel(getContext(), marginRight),
                UiUtils.getDPinPixel(getContext(), marginBottom)
        };
        return this;
    }

    public SnappingSeekBar setIndicatorTextSize(final int textSize) {
        indicatorTextSize = mDensity * textSize;
        return this;
    }

    // Progress --------------------------------------------
    public SnappingSeekBar setProgressBaseDrawable(int progressBaseDrawable) {
        this.progressBaseDrawable = progressBaseDrawable;
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
        this.progressColor = progressColor;
        return this;
    }

    // Thumbnail --------------------------------------------
    public SnappingSeekBar setThumbDrawable(final int thumbDrawableId) {
        this.thumbDrawableId = thumbDrawableId;
        return this;
    }

    public SnappingSeekBar setThumbnailColor(final int thumbnailColor) {
        this.thumbnailColor = thumbnailColor;
        return this;
    }

    // Bound Text --------------------------------------------
    public SnappingSeekBar setBoundTextStart(String boundTextStart) {
        this.boundTextStart = boundTextStart;
        return this;
    }

    public SnappingSeekBar setBoundTextEnd(String boundTextEnd) {
        this.boundTextEnd = boundTextEnd;
        return this;
    }

    public SnappingSeekBar setBoundTextMargin(float[] margin) {
        return setBoundTextMargin(margin[0], margin[1], margin[2], margin[3]);
    }

    public SnappingSeekBar setBoundTextMargin(float margin) {
        return setBoundTextMargin(margin, margin, margin, margin);
    }

    public SnappingSeekBar setBoundTextMargin(float marginLeft, float marginTop, float marginRight, float marginBottom) {
        this.boundTextMargin = new int[]{
                UiUtils.getDPinPixel(getContext(), marginLeft),
                UiUtils.getDPinPixel(getContext(), marginTop),
                UiUtils.getDPinPixel(getContext(), marginRight),
                UiUtils.getDPinPixel(getContext(), marginBottom)
        };
        return this;
    }

    public SnappingSeekBar setBoundTextColor(int boundTextColor) {
        this.boundTextColor = boundTextColor;
        return this;
    }

    public SnappingSeekBar setBoundViewLength(int boundViewLength) {
        this.boundViewLength = boundViewLength;
        return this;
    }

    // Object -------------------------------------------------
    public SnappingSeekBar setItems(List<SeekbarElement> list) {
        if (list == null || list.size() < 1) return this;

        this.seekBarElementList = list;
        indicatorCount = list.size();

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