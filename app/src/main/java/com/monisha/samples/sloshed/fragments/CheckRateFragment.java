package com.monisha.samples.sloshed.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.util.StageEnum;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckRateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckRateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckRateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_STAGE = "param1";

    // TODO: Rename and change types of parameters
    private int stageParam;
    private StageEnum currStage;
    private FragmentTransaction subFragmentTransaction;
    private FragmentManager subFragmentManager;

    private OnFragmentInteractionListener mListener;

    public CheckRateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stageEnum Parameter 1.
     * @return A new instance of fragment CheckRateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckRateFragment newInstance(StageEnum stageEnum) {
        CheckRateFragment fragment = new CheckRateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STAGE, stageEnum.toInt());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stageParam = getArguments().getInt(ARG_STAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_rate, container, false);
        subFragmentManager = getChildFragmentManager();
        subFragmentTransaction = subFragmentManager.beginTransaction();
        subFragmentTransaction.replace(R.id.subfragment_container, getCurrFragment(getCurrStage(stageParam)));
        subFragmentTransaction.commit();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.onCheckRateFragmentInteractionGetMin();
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
        int onCheckRateFragmentInteractionGetMin();
    }

    private StageEnum getCurrStage(int stageInt){
        switch (stageInt) {
            case 0:
                return StageEnum.START_MY_NIGHT;
            case 1:
                return StageEnum.MEAL_DETAILS;
            case 2:
                return StageEnum.METER_WITH_DRINK;
            case 3:
                return StageEnum.METER;
        }
        return StageEnum.START_MY_NIGHT;
    }

    private Fragment getCurrFragment(StageEnum stageEnum) {
        switch (stageEnum) {
            case START_MY_NIGHT:
                return new StartNightFragment();
            case MEAL_DETAILS:
                return new MealFragment();
            case METER_WITH_DRINK:
                return new DrinkSelectionFragment();
            case METER:
                return new MeterFragment().newInstance(false, mListener.onCheckRateFragmentInteractionGetMin());
        }
        return new StartNightFragment();
    }
}
