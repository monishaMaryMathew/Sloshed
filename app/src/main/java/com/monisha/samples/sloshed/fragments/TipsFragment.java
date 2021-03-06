package com.monisha.samples.sloshed.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.activities.MainActivity;
import com.monisha.samples.sloshed.adapters.TipsListAdapter;
import com.monisha.samples.sloshed.models.Tips;
import com.monisha.samples.sloshed.util.APICallsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//TODO
//import com.monisha.samples.sloshed.adapters.TipsListAdapter;
//import com.monisha.samples.sloshed.models.Tips;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TipsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TipsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TipsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int PERMISSION_REQUEST_INTERNET = 0;
    ListView listView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public TipsFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TipsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TipsFragment newInstance(String param1, String param2) {
        TipsFragment fragment = new TipsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        //setContentView(R.layout.activity_main);
        View view = inflater.inflate(R.layout.fragment_tips, container, false);
        GetTask runner = new GetTask();
        runner.execute();
        listView = view.findViewById(R.id.tipsList);
        return view;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onTipsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    private void populateList(List<Tips> tipsList) {
        Tips[] tipsArray = tipsList.toArray(new Tips[tipsList.size()]);
        TipsListAdapter listAdapter = new TipsListAdapter(getActivity(), tipsList.toArray(new Tips[tipsList.size()]));
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tips selectedTip = (Tips) listView.getItemAtPosition(position);
//                WebView webView = new WebView(getActivity());
                //setContentView(webView);
//                webView.loadUrl(selectedTip.url);
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedTip.url));
                startActivity(myIntent);
                //return webView;

            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTipsFragmentInteraction(Uri uri);
    }

    public class GetTask extends AsyncTask<String, Void, String>
    {
        private static final int REQUEST_INTERNET = 0;
        private Exception exception;
        private  List<Tips> tipsArray = new ArrayList<Tips>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MainActivity) getActivity()).setProgressLayout(true);
        }

        protected String doInBackground(String... urls)
        {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET)
                    == PackageManager.PERMISSION_GRANTED)
            {
                // Permission is already available
                try
                {
                    String resp = APICallsUtil.getResponse("http://notsloshed-env.us-east-1.elasticbeanstalk.com/tips");
                        JSONObject jObject = new JSONObject(resp);
                        JSONArray jArray = jObject.getJSONObject("_embedded").getJSONArray("tips");
                        for (int i = 0; i < jArray.length(); i++)
                        {
                            JSONObject oneObject = jArray.getJSONObject(i);
                            // Pulling items from the array
                            String oneObjectsItem = oneObject.getString("tipname");
                            String oneObjectsItem2 = oneObject.getString("tiplink");
                            tipsArray.add(new Tips(oneObjectsItem, oneObjectsItem2));
                        }
                        // Do normal input or output stream reading
//                        populateList(tipsArray);

                }
                catch (Exception e)
                {
                    String ex = e.getMessage();
                }
            }
            else
            {
                // Permission is missing and must be requested.
                requestPermission();
            }
            return null;
        }

        private void requestPermission()
        {
            // Permission has not been granted and must be requested.
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.INTERNET))
            {
                // Request the permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
            }
            else
            {
                // Request the permission. The result will be received in onRequestPermissionResult().
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
            }
        }

        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
        {
            // BEGIN_INCLUDE(onRequestPermissionsResult)
            if (requestCode == REQUEST_INTERNET)
            {
                // Request for camera permission.
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Permission has been granted. Start camera preview Activity.
                    String resp = APICallsUtil.getResponse("http://notsloshed-env.us-east-1.elasticbeanstalk.com/tips");
                }
                else
                {
                    // Permission request was denied.

                }
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            populateList(tipsArray);
            ((MainActivity) getActivity()).setProgressLayout(false);
        }
    }
}

