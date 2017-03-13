package com.tobishiba.snappingseekbar.sample.activities;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tobishiba.snappingseekbar.R;

import java.util.ArrayList;
import java.util.List;

import hu.mesys.snappingseekbar.library.models.SeekbarElement;
import hu.mesys.snappingseekbar.library.utils.UiUtils;
import hu.mesys.snappingseekbar.library.views.SnappingSeekBar;

/**
 * User: tobiasbuchholz
 * Date: 28.07.14 | Time: 14:18
 * <p>
 * Modified by József Mezei on 07.03.2017
 */
public class MainActivity extends Activity implements SnappingSeekBar.OnItemSelectionListener {

    private LinearLayout layout;
    private List<SeekbarElement> elements;
    private SnappingSeekBar snappingSeekBar;

    private int colorBlack;
    private int colorRed;
    private int colorGrey;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.activity_main_layout);
        snappingSeekBar = (SnappingSeekBar) findViewById(R.id.snapp);

        init();
        createSnappingSeekBarProgrammaticallyFromList();
    }

    private void init(){
        colorBlack = ContextCompat.getColor(this, R.color.black);
        colorRed = ContextCompat.getColor(this, R.color.red);
        colorGrey = ContextCompat.getColor(this, R.color.grey);
    }

    private void createSnappingSeekBarProgrammaticallyFromList() {
        elements = new ArrayList<>();
        elements.add(new SeekbarElement(R.drawable.rect_background, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "1", ContextCompat.getColor(this, R.color.green_darker)));
        elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.green), ContextCompat.getColor(this, R.color.red), "2", ContextCompat.getColor(this, R.color.green_darker)));
        elements.add(new SeekbarElement(R.drawable.circle_background_small, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "3", ContextCompat.getColor(this, R.color.green_darker)));
        elements.add(new SeekbarElement(R.drawable.rect_background, ContextCompat.getColor(this, R.color.green), ContextCompat.getColor(this, R.color.red), "4", ContextCompat.getColor(this, R.color.green_darker)));
        elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "5", ContextCompat.getColor(this, R.color.green_darker)));
        elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.blue), ContextCompat.getColor(this, R.color.red), "6", ContextCompat.getColor(this, R.color.green_darker)));

        final Resources resources = getResources();
        //final SnappingSeekBar snappingSeekBar = new SnappingSeekBar(this)
        snappingSeekBar.setItems(elements)
                .setQuestionGravity(Gravity.START)
                .setQuestion("Really hard question")
                .setQuestionColor(colorBlack)
                .setQuestionTextSize(17)
                .setQuestionPadding(0, 10, 0, 10)

                .setProgressBaseDrawable(R.drawable.progress)
                .setProgressColor(colorRed)
                .setThumbnailColor(colorRed)
                .setIndicatorTextSize(14)
                .setIndicatorSize(14)
                .setBoundTextStart("low")
                .setBoundTextEnd("hight")
                .setBoundTextMargin(0, 5, 0, 0)
                .setBoundViewLength(3)
                .setBoundTextColor(colorBlack)
                .setOnItemSelectionListener(this)
                .build();

        UiUtils.waitForLayoutPrepared(snappingSeekBar, new UiUtils.LayoutPreparedListener() {
            @Override
            public void onLayoutPrepared(final View preparedView) {
                snappingSeekBar.setProgressToIndex(2);
            }
        });

//        layout.addView(snappingSeekBar);
    }

    @Override
    public void onItemSelected(final int itemIndex, final String itemString) {
        Toast.makeText(this, getString(R.string.toast_item_selected, elements.get(itemIndex).getIndicatorText(), ""), Toast.LENGTH_SHORT).show();
    }
}
