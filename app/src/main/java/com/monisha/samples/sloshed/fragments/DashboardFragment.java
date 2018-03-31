package com.monisha.samples.sloshed.fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.*;
import com.github.mikephil.charting.components.*;
import com.google.api.client.util.DateTime;
import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.adapters.ChartAdapter;
import com.monisha.samples.sloshed.dbmodels.DrinkDB;
import com.monisha.samples.sloshed.util.AppDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import retrofit2.http.GET;

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
    List<DrinkDB> drinksData;
    final String item = "Weekly";
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
        ListView lv = (ListView) view.findViewById(R.id.listView);
        final DbWorkAsyncTask task = new DbWorkAsyncTask();
        ArrayList<BarData> list = new ArrayList<BarData>();
        ChartAdapter cda = new ChartAdapter(getActivity(), list);
        final GetTask task1 = new GetTask();
        //final BarChart barChart = (BarChart) view.findViewById(R.id.chart);
        final ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        setStartEndDateForWeek();
        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                flag = ((String) spinner.getSelectedItem())!= "Weekly View" ? 0:1;
                try
                {
                    task.execute().get();
                    task1.execute().get();
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
//                try
//                {
//                    task.execute().get();
//                    task1.execute().get();
//                }
//                catch (Exception e)
//                {
//
//                }
            }
        });
        try
        {
            task.execute().get();
//            task1.execute().get();
        }
        catch (Exception e)
        {

        }
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

        //BarData data = addData();
        //barChart.setData(data);
        lv.setAdapter(cda);
        //taskget.execute(); // set the data and list of lables into chart

        Log.d("vals","size="+yVals.size());
        return view;//inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    private void setStartEndDateForWeek()
    {
        Calendar c = Calendar.getInstance();
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            c.add(Calendar.DATE, -1);
        startDate = c.getTime();
        c = Calendar.getInstance();
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
            c.add(Calendar.DATE, 1);
        endDate = c.getTime();
    }

    private class GetTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            if(flag == 0) {
//                drinksData = db.drinkDAO().getAll();
                drinksData = db.drinkDAO().getForStartEnd(startDate, endDate);
            }
            else
                drinksData = db.drinkDAO().getAll();
            Log.d("doinbackground","size:" + drinksData.size());
            //emergencyContacts = db.emergencyContactDAO().getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSettingsFragmentInteraction(uri);
        }
    }

    private class DbWorkAsyncTask extends AsyncTask<Void, Void, Void>
    {
        Date now = new Date();
        Date now1 = new Date(118,2,25,22,21);
        Date now2 = new Date(118,2,28,22,23);
        Date now3 = new Date(118,2,3,22,24);
        Date future = new Date(118,2,1,22,20);
        @Override
        protected Void doInBackground(Void... voids) {
            initializeDB();
            deleteAll();
            //testInsertUpdate();
            insertToDB();
            getAndDisplayAll();
            return null;
        }

        private void testInsertUpdate() {
            DrinkDB drinks = new DrinkDB(5,now,3, now,future,0);
            updateOrInsert(drinks); //needs to insert, since it is the first time
            getAndDisplayAll();

            drinks.setDrinkCount(2);
            updateOrInsert(drinks); //needs to update, since it is a new number
            getAndDisplayAll();

            drinks.setBac(0.04f);
            updateOrInsert(drinks); //needs to insert, since it is new drinks
            getAndDisplayAll();
        }

        private void initializeDB(){
            db = Room.databaseBuilder(getContext(),
                    AppDatabase.class, "DrinkDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        private void insertToDB(){
            DrinkDB bc1 = new DrinkDB(11,now,3, now,future,0);
            DrinkDB bc2 = new DrinkDB(22,now1,2, now1,future,0);
            DrinkDB bc3 = new DrinkDB(33,now2,0, now2,future,0);
            DrinkDB bc4 = new DrinkDB(44,now3,1, now3,future,0);
            db.drinkDAO().insertAll(bc1, bc2, bc3, bc4);
        }

        private void readFromDB(){
            /*List<DrinkDB> bcs = db.blockedContactDAO().getAll();
            */
            List<DrinkDB> bcs = db.drinkDAO().getForStartEnd(now, future);
            if((bcs==null)||(bcs!=null && bcs.size()==0)){
                Log.d("TAG", "ouch");
            }
            for(DrinkDB bc: bcs){
                Log.d("TAG", "timestamp:"+bc.timestamp.toString() + "count:" + bc.drinkCount + "timestart:" + bc.start_time + "end:" + bc.end_time + "bac:" + bc.bac);
            }
        }

        private void deleteAll(){
            List<DrinkDB> list = getAndDisplayAll();
            for (DrinkDB b : list){
                db.drinkDAO().delete(b);
            }
            Log.d("TAG", "deleted all");
            List<DrinkDB> list1 = getAndDisplayAll();
        }

        private List<DrinkDB> getAndDisplayAll() {
            List<DrinkDB> list = db.drinkDAO().getAll();
            display(list);
            return list;
        }

        private void updateOrInsert(DrinkDB contact){
            DrinkDB contactToBeAdded = contact;
            List<DrinkDB> contacts = db.drinkDAO().getForThisSession(contactToBeAdded.timestamp);
            if((contacts==null)||(contacts!=null && contacts.size()==0)){
                //No such contact already exists in the database
                db.drinkDAO().insertAll(contactToBeAdded);
                Log.d("TAG", "Inserted contact");
            } else {
                //This contact already exists in the database
                DrinkDB contactToBeUpdated = contacts.get(0);
                contactToBeUpdated.setDrinkCount(contactToBeAdded.drinkCount);
                db.drinkDAO().update(contactToBeUpdated);
                Log.d("TAG", "Updated contact");
            }
        }

        private void display(List<DrinkDB> cs){
            if((cs==null)||(cs!=null && cs.size()==0)){
                Log.d("TAG", "ouch");
            }
            for(DrinkDB bc: cs){
                Log.d("TAG", "timestamp:"+bc.timestamp.toString() + "count:" + bc.drinkCount + "timestart:" + bc.start_time + "end:" + bc.end_time + "bac:" + bc.bac);
            }
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
        try
        {
            task.execute().get();
        }
        catch (Exception e)
        {

        }
        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Monday");
        labels.add("Tuesday");
        labels.add("Wednesday");
        labels.add("Thursday");
        labels.add("Friday");
        labels.add("Saturday");
        labels.add("Sunday");

        Map<Date, Integer> dateMap = new TreeMap<Date,Integer>();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        dateMap.put(startDate,0);
        while (((c.getTime()).compareTo(endDate)) == -1)
        {
            c.add(Calendar.DATE,1);
            if(c.getTime() == endDate)
                break;
            dateMap.put(c.getTime(),0);
        }
        //TODO Fix this date removal thing
        for (DrinkDB drink:drinksData)
        {
            if(drink.drinkCount > 0)
            {
                //dateMap.remove(drink.timestamp);
                dateMap.remove(drink.timestamp);
                dateMap.put(drink.timestamp, (int)drink.drinkCount);
            }
        }
        int ct = 0;
        Iterator<Map.Entry<Date, Integer>> itr = dateMap.entrySet().iterator();
        while (itr.hasNext())
        {
            //DrinkDB drink = drinksData.get(i);
            Map.Entry<Date,Integer> drink = itr.next();
            yVals.add(new BarEntry(ct,drink.getValue()));
            ct++;
        }

        BarDataSet dataSet = new BarDataSet(yVals, "Drinks count");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        data.setValueTextSize(13f);
        data.setBarWidth(0.8f);
        return data;
    }
}
