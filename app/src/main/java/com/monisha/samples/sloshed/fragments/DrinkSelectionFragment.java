package com.monisha.samples.sloshed.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.models.Drink;
import com.monisha.samples.sloshed.util.DrinkEnum;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkSelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrinkSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinkSelectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private static final int BACK_CANCEL_BTN = 0;
    private static final int ADD_BTN = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private RelativeLayout backBtn;
    private Button cancelBtn, addBtn;

    private Drink selectedDrink = new Drink();

    //Selection elements
    private RelativeLayout beerHeader, wineHeader, spiritsHeader;
    private LinearLayout beerDetails, wineDetails, spiritsDetails;
    //Beer
    private RelativeLayout beer5percent, beer7percent;
    private RelativeLayout beer12oz, beer16oz, beer22oz, beer40oz;
    //Wine
    private RelativeLayout wine12percent;
    private RelativeLayout wine5oz, wine25oz;
    //Spirits
    private RelativeLayout spirits40percent;
    private RelativeLayout spirits1oz, spirits6oz, spirits12oz, spirits25oz;

    private ArrayList<RelativeLayout> percentageOptions = new ArrayList<RelativeLayout>();
    private ArrayList<RelativeLayout> quantityOptions = new ArrayList<RelativeLayout>();

    private OnFragmentInteractionListener mListener;

    public DrinkSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DrinkSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrinkSelectionFragment newInstance(String param1) {
        DrinkSelectionFragment fragment = new DrinkSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drink_selection, container, false);
        backBtn = (RelativeLayout) view.findViewById(R.id.drink_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackButtonPressed();
            }
        });
        cancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackButtonPressed();
            }
        });
        addBtn = (Button) view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonPressed();
            }
        });
        beerHeader = (RelativeLayout) view.findViewById(R.id.beer_header);
        beerDetails = (LinearLayout) view.findViewById(R.id.beer_details);
        wineHeader = (RelativeLayout) view.findViewById(R.id.wine_header);
        wineDetails = (LinearLayout) view.findViewById(R.id.wine_details);
        spiritsHeader = (RelativeLayout) view.findViewById(R.id.spirits_header);
        spiritsDetails = (LinearLayout) view.findViewById(R.id.spirits_details);
        collapseAll();
        setSectionsOnClickListeners();
        //Beer
        beer5percent = (RelativeLayout) view.findViewById(R.id.beer_5_percent);
        beer7percent = (RelativeLayout) view.findViewById(R.id.beer_7_percent);
        beer12oz = (RelativeLayout) view.findViewById(R.id.beer_12_oz);
        beer16oz = (RelativeLayout) view.findViewById(R.id.beer_16_oz);
        beer22oz = (RelativeLayout) view.findViewById(R.id.beer_22_oz);
        beer40oz = (RelativeLayout) view.findViewById(R.id.beer_40_oz);

        //wine
        wine12percent = (RelativeLayout) view.findViewById(R.id.wine_12_percent);
        wine5oz = (RelativeLayout) view.findViewById(R.id.wine_5_oz);
        wine25oz = (RelativeLayout) view.findViewById(R.id.wine_25_oz);

        //spirits
        spirits40percent = (RelativeLayout) view.findViewById(R.id.spirits_40_percent);
        spirits1oz = (RelativeLayout) view.findViewById(R.id.spirits_1_oz);
        spirits6oz = (RelativeLayout) view.findViewById(R.id.spirits_6_oz);
        spirits12oz = (RelativeLayout) view.findViewById(R.id.spirits_12_oz);
        spirits25oz = (RelativeLayout) view.findViewById(R.id.spirits_25_oz);

        makeArrays();

        deselectAllPerecentageOptions();
        deselectAllQuantityOptions();

        //TODO create listeners for all
        createListeners();

        return view;
    }

    private void manageQuantitySelection(DrinkEnum type, float quantity) {
        if (selectedDrink.getDrinkType() == null) {
            //No previous selection
            //First Selection
            selectedDrink.setDrinkType(type);
            selectedDrink.setQuantity(quantity);
        } else if (selectedDrink.getDrinkType() != null && selectedDrink.getDrinkType() == type) {
            //Same type of selection as the previous
            selectedDrink.setQuantity(quantity);
        } else {
            //Different selection compared to the previous type
            //deselect all the previous selections
            deselectAllQuantityOptions();
            deselectAllPerecentageOptions();
            //make new selections
            selectedDrink.setDrinkType(type);
            selectedDrink.setQuantity(quantity);
            selectedDrink.setAlcoholPercentage(0);//default
        }
    }

    private void managePercentageSelection(DrinkEnum type, float percentage) {
        if (selectedDrink.getDrinkType() == null) {
            //No previous selection
            //First Selection
            selectedDrink.setDrinkType(type);
            selectedDrink.setAlcoholPercentage(percentage);
        } else if (selectedDrink.getDrinkType() != null && selectedDrink.getDrinkType() == type) {
            //Same type of selection as the previous
            selectedDrink.setAlcoholPercentage(percentage);
        } else {
            //deselect all the previous selections
            deselectAllQuantityOptions();
            deselectAllPerecentageOptions();
            //make new selections
            selectedDrink.setDrinkType(type);
            selectedDrink.setAlcoholPercentage(percentage);
            selectedDrink.setQuantity(0);//default
        }
    }

    private void deselectAllPerecentageOptions() {
        for (RelativeLayout layout : percentageOptions) {
            layout.setBackground(getActivity().getDrawable(R.drawable.rounded_border));
        }
    }

    private void deselectAllQuantityOptions() {
        for (RelativeLayout layout : quantityOptions) {
            layout.setBackground(getActivity().getDrawable(R.drawable.rounded_border));
        }
    }

    private void createListeners() {
        beer5percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllPerecentageOptions();//deselect other percentage options
                managePercentageSelection(DrinkEnum.BEER, 5);
                beer5percent.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        beer7percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllPerecentageOptions();//deselect other percentage options
                managePercentageSelection(DrinkEnum.BEER, 7);
                beer7percent.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        wine12percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllPerecentageOptions();//deselect other percentage options
                managePercentageSelection(DrinkEnum.WINE, 12);
                wine12percent.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        spirits40percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllPerecentageOptions();//deselect other percentage options
                managePercentageSelection(DrinkEnum.SPIRITS, 40);
                spirits40percent.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        beer12oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.BEER, 12);
                beer12oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        beer16oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.BEER, 16);
                beer16oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        beer22oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.BEER, 22);
                beer22oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        beer40oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.BEER, 40);
                beer40oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        wine5oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.WINE, 5);
                wine5oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        wine25oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.WINE, (float) 25.36);
                wine25oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        spirits1oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.SPIRITS, (float) 1.5);
                spirits1oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        spirits6oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.SPIRITS, (float) 6.76);
                spirits6oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        spirits12oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.SPIRITS, (float) 12.68);
                spirits12oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
        spirits25oz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllQuantityOptions();//deselect other quantity options
                manageQuantitySelection(DrinkEnum.SPIRITS, (float) 25.36);
                spirits25oz.setBackground(getActivity().getDrawable(R.drawable.rounded_border_selected));
            }
        });
    }

    private void makeArrays() {
        percentageOptions.add(beer5percent);
        percentageOptions.add(beer7percent);
        percentageOptions.add(wine12percent);
        percentageOptions.add(spirits40percent);

        quantityOptions.add(beer12oz);
        quantityOptions.add(beer16oz);
        quantityOptions.add(beer22oz);
        quantityOptions.add(beer40oz);
        quantityOptions.add(wine5oz);
        quantityOptions.add(wine25oz);
        quantityOptions.add(spirits1oz);
        quantityOptions.add(spirits6oz);
        quantityOptions.add(spirits12oz);
        quantityOptions.add(spirits25oz);
    }

    private void setSectionsOnClickListeners() {
        beerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (beerDetails.getVisibility() == View.VISIBLE) {
                    collapseAll();
                } else {
                    collapseAll();
                    beerDetails.setVisibility(View.VISIBLE);
                }
            }
        });
        wineHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wineDetails.getVisibility() == View.VISIBLE) {
                    collapseAll();
                } else {
                    collapseAll();
                    wineDetails.setVisibility(View.VISIBLE);
                }
            }
        });
        spiritsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spiritsDetails.getVisibility() == View.VISIBLE) {
                    collapseAll();
                } else {
                    collapseAll();
                    spiritsDetails.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void collapseAll() {
        beerDetails.setVisibility(View.GONE);
        wineDetails.setVisibility(View.GONE);
        spiritsDetails.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onBackButtonPressed() {
        if (mListener != null) {
            mListener.onDrinkFragmentInteraction(BACK_CANCEL_BTN, null);
        }
    }

    public void onAddButtonPressed() {
        //TODO add code to get the selectedDrink selected
        if (mListener != null) {
            mListener.onDrinkFragmentInteraction(ADD_BTN, selectedDrink);
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
        //Button Case:
        //case 0 - back button, or cancel button
        //cae 1 - add button
        void onDrinkFragmentInteraction(int buttonCase, Drink drink);
    }
}
