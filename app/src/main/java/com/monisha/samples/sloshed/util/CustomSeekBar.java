package com.monisha.samples.sloshed.util;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by meghasaini on 3/20/18.
 */

public class CustomSeekBar extends Preference implements SeekBar.OnSeekBarChangeListener {
    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //Preference pref = findPreference();
    public CustomSeekBar(Context context) {
        super(context);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        //((Preference)this).setSummary(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
