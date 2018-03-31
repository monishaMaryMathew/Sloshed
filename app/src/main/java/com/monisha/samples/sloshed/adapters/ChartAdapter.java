package com.monisha.samples.sloshed.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.monisha.samples.sloshed.R;

import java.util.List;

/**
 * Created by Divya on 26-03-2018.
 */

public class ChartAdapter extends ArrayAdapter<BarData>
{

    public ChartAdapter(Context context, List<BarData> objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        BarData data = getItem(position);
        ViewHolder holder = null;

        if (convertView == null) {

        holder = new ViewHolder();

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_barchart, null);
        holder.chart = (BarChart) convertView.findViewById(R.id.chart);

        convertView.setTag(holder);

        } else {
        holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        //data.setValueTypeface(mTfLight);
        data.setValueTextColor(Color.BLACK);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = holder.chart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = holder.chart.getAxisRight();
        //rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);

        // set data
        holder.chart.setData(data);
        holder.chart.setFitBars(true);

        // do not forget to refresh the chart
//            holder.chart.invalidate();
        holder.chart.animateY(700);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setPinchZoom(false);
        YAxis left = holder.chart.getAxisLeft();
        left.setDrawLabels(false);
        left.setSpaceTop(25f);
        left.setSpaceBottom(25f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        holder.chart.getLegend().setEnabled(false);
        return convertView;
    }

    private class ViewHolder
    {
        BarChart chart;
    }
}
