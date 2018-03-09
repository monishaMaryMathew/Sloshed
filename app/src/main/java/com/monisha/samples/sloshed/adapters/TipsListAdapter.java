package com.monisha.samples.sloshed.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.models.Tips;

/**
 * Created by Divya on 09-03-2018.
 */

public class TipsListAdapter extends ArrayAdapter
{
    private final Activity context;
    private final Tips[] tipsArray;

    public TipsListAdapter(Activity context, Tips[] tipsArray)
    {
        super(context, R.layout.listview_row, tipsArray);
        this.context = context;
        this.tipsArray = tipsArray;
    }
    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView titleTextField = (TextView) rowView.findViewById(R.id.titleTextView);

        //this code sets the values of the objects to values from the arrays
        titleTextField.setText(tipsArray[position].title);

        return rowView;
    }
}
