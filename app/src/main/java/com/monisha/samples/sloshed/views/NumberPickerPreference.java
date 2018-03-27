package com.monisha.samples.sloshed.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
/**
 * Created by meghasaini on 3/26/18.
 */

/**
 * A {@link android.preference.Preference} that displays a number picker as a dialog.
 */

public class NumberPickerPreference extends DialogPreference {

    // allowed range
    public static final int MAX_VALUE = 100;
    public static final int MIN_VALUE = 0;
    // enable or disable the 'circular behavior'
    public static final boolean WRAP_SELECTOR_WHEEL = true;

    private NumberPicker picker;
    private int value;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);

        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setMinValue(MIN_VALUE);
        picker.setMaxValue(MAX_VALUE);
        picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        picker.setValue(getValue());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            picker.clearFocus();
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, MIN_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(MIN_VALUE) : (Integer) defaultValue);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
        persistInt(this.value);
    }
}


//
//
//public class NumberPickerPreference extends DialogPreference {
//    private NumberPicker mPicker;
//    private int mNumber = 0;
//
//    public NumberPickerPreference(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        setPositiveButtonText(android.R.string.ok);
//        setNegativeButtonText(android.R.string.cancel);
//    }
//
//    @Override
//    protected View onCreateDialogView() {
//        mPicker = new NumberPicker(getContext());
//        mPicker.setMinValue(1);
//        mPicker.setMaxValue(100);
//        mPicker.setValue(mNumber);
//        return mPicker;
//    }
//
//    @Override
//    protected void onDialogClosed(boolean positiveResult) {
//        if (positiveResult) {
//            mPicker.clearFocus();
//            setValue(mPicker.getValue());
//        }
//    }
//
//    @Override
//    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
//        setValue(restoreValue ? getPersistedInt(mNumber) : (Integer) defaultValue);
//    }
//
//    public void setValue(int value) {
//        if (shouldPersist()) {
//            persistInt(value);
//        }
//
//        if (value != mNumber) {
//            mNumber = value;
//            notifyChanged();
//        }
//    }
//
//    @Override
//    protected Object onGetDefaultValue(TypedArray a, int index) {
//        return a.getInt(index, 0);
//    }
//}