package com.monisha.samples.sloshed.fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.*;
import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.adapters.ChartAdapter;
import com.monisha.samples.sloshed.dbmodels.DrinkDB;
import com.monisha.samples.sloshed.util.AppDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int flag = 0;
    private Date startDate, endDate;
    View view;
    AppDatabase db;
    ArrayList<BarData> list;
    List<DrinkDB> drinksData;
    ChartAdapter cda ;
    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        db = AppDatabase.getAppDatabase(getContext());
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        final ListView lv = (ListView) view.findViewById(R.id.listView);
        list = new ArrayList<BarData>();
        cda = new ChartAdapter(getActivity(), list);
        cda.setFlag(0); //Weekly view initially
        final GetTask task1 = new GetTask();
        final ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        setStartEndDate();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                flag = (String) spinner.getSelectedItem() != "Weekly View" ? 1:0;
                try
                {
                    cda.setFlag(flag); //For the chartAdapter to know what labels to load
                    list.clear();
                    list.add(addData());
                    lv.setAdapter(cda);
                }
                catch (Exception e)
                {}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        list.add(addData());
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Weekly View");
        categories.add("Monthly View");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // Inflate the layout for this
        lv.setAdapter(cda);
        return view;//inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    private int setStartEndDate()
    {
        Calendar c = Calendar.getInstance();
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            c.add(Calendar.DATE, -1);
        startDate = c.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        c = Calendar.getInstance();
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
            c.add(Calendar.DATE, 1);
        endDate = c.getTime();
        try //this is to make the start time 12AM
        {
            startDate = df.parse(df.format(startDate));
            endDate = df.parse(df.format(endDate));
        }
        catch (Exception e)
        {}
        return 7;
    }

    private class GetTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            //There is no need to use getAll() need to get details exactly for a month or a week. Use
            //getAll() if needed later.
            drinksData = db.drinkDAO().getForStartEnd(startDate, endDate);
            return null;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSettingsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSettingsFragmentInteraction(Uri uri);
    }

    private BarData addData()
    {
        final ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        GetTask task = new GetTask();
        int days;
        if(flag == 1) //monthly
        {
            for (int i = 0; i < 12; i++)
            {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                startDate = new GregorianCalendar(year, i, 1).getTime();
                switch (i)
                {
                    case 0: //Jan
                    case 2:
                    case 4:
                    case 6:
                    case 7:
                    case 9:
                    case 11:
                        days = 31;
                        break;
                    case 1:
                        days = 28;
                        break;
                    default:
                        days = 30;
                        break;
                }
                endDate = new GregorianCalendar(year, i, days).getTime();
                try
                {
                    task = new GetTask();
                    task.execute().get();
                }
                catch (Exception e)
                {
                }
                int totalDrinkCount = 0;
                for (int j = 0; j < drinksData.size(); j++)
                    totalDrinkCount += drinksData.get(j).getDrinkCount();
                yVals.add(new BarEntry(i, totalDrinkCount));
            }
        }
        else
        {
            days = setStartEndDate();
            try
            {
                task.execute().get();
            }
            catch (Exception e)
            {
            }
            Map<String, Integer> dateMap = new HashMap<String, Integer>();
            Calendar c = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(startDate);
            dateMap.put(df.format(startDate), 0);
            int ct = 0;
            while (ct < days)
            {
                if (c.getTime().compareTo(endDate) == 1)
                    break;
                else
                {
                    c.add(Calendar.DATE, 1);
                    dateMap.put(df.format(c.getTime()), 0);
                }
            }
            if (dateMap.containsKey(df.format(c.getTime())))
                dateMap.remove(df.format(c.getTime()));
            for (DrinkDB drink : drinksData)
            {
                if (drink.drinkCount > 0)
                {
                    //dateMap.remove(drink.timestamp);
                    dateMap.remove(df.format(drink.timestamp));
                    dateMap.put(df.format(drink.timestamp), (int) drink.drinkCount);
                }
            }
            ct = 0;
            Map<Date, Integer> newDateMap = new TreeMap<>();
            for (Map.Entry<String, Integer> entry : dateMap.entrySet())
            {
                try
                {
                    newDateMap.put(df.parse(entry.getKey()), entry.getValue());
                }
                catch (Exception e)
                {
                }
            }
            Iterator<Map.Entry<Date, Integer>> itr = newDateMap.entrySet().iterator();
            while (itr.hasNext())
            {
                //DrinkDB drink = drinksData.get(i);
                Map.Entry<Date, Integer> drink = itr.next();
                yVals.add(new BarEntry(ct, drink.getValue()));
                ct++;
            }
        }

        BarDataSet dataSet = new BarDataSet(yVals, "Drinks count");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
//        data.setDrawValues(false);
        data.setValueTextSize(13f);
        data.setBarWidth(0.8f);
        return data;
    }
}
