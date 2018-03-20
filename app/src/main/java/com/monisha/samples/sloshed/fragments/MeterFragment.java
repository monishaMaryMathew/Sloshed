package com.monisha.samples.sloshed.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;
import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DIALOGUE = "param1";
    private static final String ARG_MIN = "param2";

    // TODO: Rename and change types of parameters
    private boolean showDialog;
    private int minAfterLastMeal;

    private OnFragmentInteractionListener mListener;

    private TextView percentageTV, messageTV;
    private SpeedView meter;

    private LinearLayout drunkModeLayout;
    private Button endMyNightBtn;
    private LinearLayout addNewDrinkBtn, addPrevDrinkBtn;
    

    public MeterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dialogShown Parameter 1.
     * @return A new instance of fragment MeterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeterFragment newInstance(boolean dialogShown, int min) {
        MeterFragment fragment = new MeterFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_DIALOGUE, dialogShown);
        args.putInt(ARG_MIN, min);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showDialog = getArguments().getBoolean(ARG_DIALOGUE);
            minAfterLastMeal = getArguments().getInt(ARG_MIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meter, container, false);

        meter = (SpeedView) view.findViewById(R.id.meter);
        meter.setSpeedTextColor(ContextCompat.getColor(getContext(), R.color.colorTransparent));
        setLevels(); //TODO compute as per the user profile

        percentageTV = (TextView) view.findViewById(R.id.percentage_info);
        messageTV = (TextView) view.findViewById(R.id.message_info);
        drunkModeLayout = (LinearLayout) view.findViewById(R.id.drunk_mode_layout);
        endMyNightBtn = (Button) view.findViewById(R.id.end_my_night_btn);
        endMyNightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed();
            }
        });

        addNewDrinkBtn = (LinearLayout) view.findViewById(R.id.add_new_drink_btn);
        addPrevDrinkBtn = (LinearLayout) view.findViewById(R.id.repeat_drink_btn);

        if (((MainActivity) getActivity()).previousDrink != null && ((MainActivity) getActivity()).previousDrink.getQuantity() != 0 && ((MainActivity) getActivity()).previousDrink.getAlcoholPercentage() != 0) {
            addPrevDrinkBtn.setVisibility(View.VISIBLE);
        } else {
            addPrevDrinkBtn.setVisibility(View.GONE);
        }

        addPrevDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add code to update meter
                Toast.makeText(getActivity(), "Drink has been added!", Toast.LENGTH_LONG).show();
            }
        });

        addNewDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.getDrinksListing();
                }
            }
        });

        initiateDrunkMode(false);

        setMeter(6); //TODO compute as per the drink intake
        return view;
    }

    private void setLevels() {
        meter.setLowSpeedPercent(((MainActivity) getActivity()).user.lowLevel);
        meter.setMediumSpeedPercent(((MainActivity) getActivity()).user.mediumLevel);
    }

    private void initiateDrunkMode(boolean initiate) {
        if (initiate) {
            drunkModeLayout.setVisibility(View.VISIBLE);
        } else {
            drunkModeLayout.setVisibility(View.GONE);
        }
    }

    private void setMeter(int percentage) {
        String message = "";
        if (percentage < ((MainActivity) getActivity()).user.lowLevel) {
            message = "Happy drinking!";
        } else if (percentage < ((MainActivity) getActivity()).user.mediumLevel) {
            message = "You might want to slow down now";
        } else {
            message = "Warning! Auto-initiating drunk mode.";
            initiateDrunkMode(true);
        }
        meter.setSpeedAt(percentage);
        messageTV.setText(message);
        percentageTV.setText(percentage + "%");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onMeterFragmentInteraction();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMeterFragmentInteraction();

        void getDrinksListing();
    }
}
