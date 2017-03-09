package hu.mesys.snappingseekbar.library.models;

/**
 * Created by József Mezei on 2017. 03. 06..
 */

public class SeekbarElement {

    protected int indicatorDrawableId;
    protected int indicatorDrawableSize = 10;
    protected int indicatorColor = 0;
    protected String indicatorText;
    protected int indicatorTextColor;
    protected int indicatorTextSize = 12;
    protected float indicatorPadding = 0;

    public SeekbarElement(int indicatorDrawableId, int indicatorDrawableSize, int indicatorColor, String indicatorText, int indicatorTextSize, String indicatorTextColor) {
        this.indicatorDrawableId = indicatorDrawableId;
        this.indicatorDrawableSize = indicatorDrawableSize;
        this.indicatorColor = indicatorColor;
        this.indicatorText = indicatorText;
        this.indicatorTextSize = indicatorTextSize;
    }

    public SeekbarElement(int indicatorDrawableId, int indicatorColor, String indicatorText, int indicatorTextColor) {
        this.indicatorDrawableId = indicatorDrawableId;
        this.indicatorText = indicatorText;
        this.indicatorColor = indicatorColor;
        this.indicatorTextColor = indicatorTextColor;
    }

    public SeekbarElement(int indicatorDrawableId, int indicatorColor, String indicatorText, int indicatorTextColor, float indicatorPadding) {
        this.indicatorDrawableId = indicatorDrawableId;
        this.indicatorText = indicatorText;
        this.indicatorColor = indicatorColor;
        this.indicatorTextColor = indicatorTextColor;
        this.indicatorPadding = indicatorPadding;
    }

    // Getter --------------------------------------------------------------------------------------
    public int getIndicatorDrawableId() {
        return indicatorDrawableId;
    }

    public int getIndicatorDrawableSize() {
        return indicatorDrawableSize;
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public String getIndicatorText() {
        return indicatorText;
    }

    public int getIndicatorTextSize() {
        return indicatorTextSize;
    }

    public int getIndicatorTextColor() {
        return indicatorTextColor;
    }

    public float getIndicatorPadding() {
        return indicatorPadding;
    }
}
