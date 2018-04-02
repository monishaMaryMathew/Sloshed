package com.monisha.samples.sloshed.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.monisha.samples.sloshed.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Divya on 26-03-2018.
 */

public class ChartAdapter extends ArrayAdapter<BarData>
{
    int flag = 0;
    public ChartAdapter(Context context, List<BarData> objects)
    {
        super(context, 0, objects);
    }

    public void setFlag(int val)
    {
        flag = val;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BarData data = getItem(position);
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_barchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.chart);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Utils.init(getContext());
        // apply styling
        //data.setValueTypeface(mTfLight);
//        data.setValueTextColor(Color.BLACK);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        final ArrayList<String> labels = new ArrayList<String>();
        if(flag == 0)
        {
            labels.add("Mon");
            labels.add("Tue");
            labels.add("Wed");
            labels.add("Thur");
            labels.add("Fri");
            labels.add("Sat");
            labels.add("Sun");
        }
        else
        {
            labels.add("Jan");
            labels.add("Feb");
            labels.add("Mar");
            labels.add("Apr");
            labels.add("May");
            labels.add("Jun");
            labels.add("Jul");
            labels.add("Aug");
            labels.add("Sep");
            labels.add("Oct");
            labels.add("Nov");
            labels.add("Dec");
//            labels.add(" ");
            xAxis.setTextSize(10f);
        }
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        if(flag == 1)
            xAxis.setLabelCount(12, true);

        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            int ct = 0;
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
//                if(flag == 1 && ct < 12)
//                {
//                    value = ct;
//                    ct++;
////                    return labels.get(ct++);
//                }
//                else
                if(flag == 1)
                {
                    if (ct < 12)
                        return labels.get(ct++);//(int) value);
                    else
                    {
                        ct = 0;
                        return labels.get(ct++);
                    }
                }
                else
                    return labels.get((int) value);
            }
        });
        xAxis.setTextColor(Color.WHITE);
        data.setValueFormatter(new IValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
            {
                if(value == 0.0)
                    return "";
                else
                    return Integer.toString((int) value);
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = holder.chart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
//        leftAxis.setLabelCount(5, false);
//        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = holder.chart.getAxisRight();
        //rightAxis.setTypeface(mTfLight);
//        rightAxis.setLabelCount(5, false);
//        rightAxis.setSpaceTop(15f);
        rightAxis.setDrawGridLines(false);
        // set data
        holder.chart.setFitBars(true);
        rightAxis.removeAllLimitLines();
        rightAxis.setDrawLabels(false);

        // do not forget to refresh the chart
//            holder.chart.invalidate();
        holder.chart.animateY(700);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setPinchZoom(false);
        YAxis left = holder.chart.getAxisLeft();
        left.setDrawLabels(false);
//        left.setSpaceTop(25f);
//        left.setSpaceBottom(25f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(false); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
//        left.setZeroLineWidth(0.7f);
        holder.chart.getLegend().setEnabled(false);
        data.setValueTextColor(Color.WHITE);

        holder.chart.setData(data);
        return convertView;
    }

    private class ViewHolder
    {
        BarChart chart;
    }
}
