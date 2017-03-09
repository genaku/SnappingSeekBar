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

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.activity_main_layout);

        createSnappingSeekBarProgrammaticallyFromList();
    }

    private void createSnappingSeekBarProgrammaticallyFromList() {
        List<SeekbarElement> elements = new ArrayList<>();
        elements.add(new SeekbarElement(R.drawable.rect_background, ContextCompat.getColor(this, R.color.blue), "1", ContextCompat.getColor(this, R.color.green_darker)));
        elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.green), "2", ContextCompat.getColor(this, R.color.green_darker)));
        elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.blue), "3", ContextCompat.getColor(this, R.color.green_darker)));
        elements.add(new SeekbarElement(R.drawable.rect_background, ContextCompat.getColor(this, R.color.green), "4", ContextCompat.getColor(this, R.color.green_darker)));
        elements.add(new SeekbarElement(R.drawable.circle_background, ContextCompat.getColor(this, R.color.blue), "5", ContextCompat.getColor(this, R.color.green_darker)));


        final Resources resources = getResources();
        final SnappingSeekBar snappingSeekBar = new SnappingSeekBar(this)
                .setItems(elements)
                .setQuestionGravity(Gravity.CENTER)
                .setProgressBaseDrawable(R.drawable.progress)
                .setProgressColor(resources.getColor(R.color.green_darker))
                .setThumbnailColor(resources.getColor(R.color.yellow_light))
                .setIndicatorTextSize(14)
                .setIndicatorSize(14)
                .setOnItemSelectionListener(this)
                .setBoundTextStart("Bad")
                .setBoundTextEnd("Perfect")
                .setBoundTextMargin(0, 5, 0, 0)
                .setBoundTextColor(ContextCompat.getColor(this, R.color.black))
                .setQuestion("Really really good question")
                .setQuestionColor(ContextCompat.getColor(this, R.color.yellow))
                .setQuestionTextSize(12)
                .build();

        UiUtils.waitForLayoutPrepared(snappingSeekBar, new UiUtils.LayoutPreparedListener() {
            @Override
            public void onLayoutPrepared(final View preparedView) {
                snappingSeekBar.setProgressToIndex(2);
            }
        });

        layout.addView(snappingSeekBar);
    }

    @Override
    public void onItemSelected(final int itemIndex, final String itemString) {
        Toast.makeText(this, getString(R.string.toast_item_selected, String.valueOf(itemIndex), itemString), Toast.LENGTH_SHORT).show();
    }
}
