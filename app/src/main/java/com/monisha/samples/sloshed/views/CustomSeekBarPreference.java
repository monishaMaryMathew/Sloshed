package com.monisha.samples.sloshed.views;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.monisha.samples.sloshed.R;

/**
 * Created by Monisha on 3/23/2018.
 */

public class CustomSeekBarPreference extends Preference {
    public int value = 0;
    private SeekBar seekBar;
    private EditText seekbarValue;
    private Context context;
    private TextView textView_summary;
    private AlertDialog dialog = null;

    public CustomSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        getAttrs(attrs);
    }

    public CustomSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttrs(attrs);
    }

    public CustomSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getAttrs(attrs);
    }

    public CustomSeekBarPreference(Context context) {
        super(context);
        this.context = context;
    }

    private void getAttrs(AttributeSet attrs) {
        if (attrs != null && attrs.getAttributeCount() > 0) {
            value = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "defaultValue", 0);
        }
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.custom_seekbar_pref_layout, parent, false);
        textView_summary = view.findViewById(android.R.id.summary);
        seekbarValue = view.findViewById(R.id.seekbar_value);
        seekBar = view.findViewById(R.id.seekbar);

        seekBar.setProgress(value);
        seekbarValue.setText(value + "");
        textView_summary.setText(value + "");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i; //update global variable
                String i_s = String.valueOf(i); //create temporary string value
                textView_summary.setText(i_s); //set summary // it's currently invisible
                if (!seekbarValue.getText().equals(i_s)) { //if this new value is not already set in the edittext
                    seekbarValue.setText(i_s);//update edittext
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbarValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        return view;
    }

    private void createDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.edit_text_layout, null);
        final EditText editText = view.findViewById(R.id.edit_text);
        editText.setText(String.valueOf(value));
        alertDialogBuilder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            value = Integer.parseInt(editText.getText().toString());
                            seekBar.setProgress(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true)
                .setTitle("Please enter your age");
        dialog = alertDialogBuilder.create();
        dialog.show();
    }
}
