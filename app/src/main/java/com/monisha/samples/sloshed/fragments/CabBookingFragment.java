package com.monisha.samples.sloshed.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.lyftbutton.RideTypeEnum;
import com.lyft.networking.ApiConfig;
import com.lyft.networking.LyftApiFactory;
import com.lyft.networking.apiObjects.RideType;
import com.lyft.networking.apiObjects.RideTypesResponse;
import com.lyft.networking.apis.LyftPublicApi;
import com.monisha.samples.sloshed.R;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.android.rides.RideRequestButton;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CabBookingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CabBookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CabBookingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RideRequestButton requestButton;
    private static final int PERMISSION_REQUEST_CODE = 1336;
    private static final long LOCATION_UPDATE_INTERVAL_MS = 15000;
    private static final float LOCATION_MIN_DISPLACEMENT_METERS = 10;
    private final Set<Call> callSet = new HashSet<>();
    private Spinner rideTypeSpinner;
    private ArrayAdapter<String> adapter;
    private LyftPublicApi lyftPublicApi;
    private LyftButton lyftButton;
    private GoogleApiClient googleApiClient;
    private double currentLat = 0.0;
    private double currentLng = 0.0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    public CabBookingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CabBookingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CabBookingFragment newInstance(String param1, String param2) {
        CabBookingFragment fragment = new CabBookingFragment();
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
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cab_booking, container, false);
        lyftButton = (LyftButton) view.findViewById(R.id.lyft_button);
        rideTypeSpinner = (Spinner) view.findViewById(R.id.ride_type_dropdown);
        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("ewYMzHD8cEkGMccDoEHJ598rorec_AtC")
                .setServerToken("oRG6FONYmNq1axBbINoQbb_cCsuMEMe5lcjppMGs")
                .setRedirectUri("https://com.monisha.samples.sloshed")
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS))
                .build();
        UberSdk.initialize(config);

        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            uberCall(config);
            lyftCall();
        }


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCabBookingFragmentInteraction();
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
        void onCabBookingFragmentInteraction();
    }

    private void uberCall(SessionConfiguration config) {



        rideRequest();
        ServerTokenSession session = new ServerTokenSession(config);
        //requestButton = new RideRequestButton(this);
        //requestButton.setSession(session);
        //requestButton.loadRideInformation();


    }

    private void rideRequest() {
        RideParameters rideParams = new RideParameters.Builder()
                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
                .setDropoffLocation(
                        Double.valueOf(37.775304f), -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
                .setPickupLocation((double) 37.775304f, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                .build();
        requestButton = new RideRequestButton(this.getContext());
        requestButton.setRideParameters(rideParams);


    }

    private void lyftCall() {

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("5HOldz5wTBSf")
                .setClientToken("DX9b/VXqyDZup+icMV0km12nJs4vkkSTfXj6qh8+tO8QWSCJ6GlvyztezDVHEQ+ucTPNZDUK9i0diOY6Nhd/cgn+lDlPIAHtrJ0sW7VSBe5DhkG0PpYA/pY=")
                .build();

        lyftButton.setApiConfig(apiConfig);
        lyftPublicApi = new LyftApiFactory(apiConfig).getLyftPublicApi();
        initializeRideTypeSpinner();
        setupLocationApi();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(googleApiClient!=null) {
            googleApiClient.disconnect();
        }
        for (Call call : callSet) {
            call.cancel();
        }
        callSet.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && isLocationPermissionGranted()) {
            googleApiClient.connect();
        }
    }

    private void initializeRideTypeSpinner() {

        adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add(getResources().getString(R.string.loading_text));
        rideTypeSpinner.setAdapter(adapter);
        rideTypeSpinner.setSelection(0, false);
        rideTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshButton((String) rideTypeSpinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void setupLocationApi() {
        googleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL_MS);
                        locationRequest.setSmallestDisplacement(LOCATION_MIN_DISPLACEMENT_METERS);
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,
                                new com.google.android.gms.location.LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        currentLat = location.getLatitude();
                                        currentLng = location.getLongitude();
                                        getRideTypesAtCurrentLocation();
                                    }
                                });
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .build();

        if (isLocationPermissionGranted()) {
            googleApiClient.connect();
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_REQUEST_CODE);
        }
    }

    private void getRideTypesAtCurrentLocation() {

        Call<RideTypesResponse> call = lyftPublicApi.getRidetypes(currentLat, currentLng, RideTypeEnum.ALL.toString());
        callSet.add(call);
        call.enqueue(new Callback<RideTypesResponse>() {
            @Override
            public void onResponse(Call<RideTypesResponse> call, Response<RideTypesResponse> response) {
                callSet.remove(call);
                adapter.clear();
                RideTypesResponse rideTypesResponse = response.body();
                if (isLyftAvailable(rideTypesResponse)) {
                    List<RideType> rideTypes = rideTypesResponse.ride_types;
                    for (RideType rideType : rideTypes) {
                        adapter.add(rideType.display_name);
                    }
                    rideTypeSpinner.setSelection(adapter.getPosition(RideTypeEnum.CLASSIC.getDisplayName()));
                } else {
                    adapter.add("LYFT N/A");
                }
            }

            @Override
            public void onFailure(Call<RideTypesResponse> call, Throwable t) {
                callSet.remove(call);
            }
        });
    }

    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void refreshButton(String selectedRideType) {
        RideTypeEnum rideTypeEnum = RideTypeEnum.CLASSIC;

        for (RideTypeEnum rte : RideTypeEnum.values()) {
            if (rte.getDisplayName().equals(selectedRideType)) {
                rideTypeEnum = rte;
            }
        }

        RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                .setPickupLocation(currentLat, currentLng);
                //.setDropoffLocation(37.759234, -122.4135125);
        rideParamsBuilder.setRideTypeEnum(rideTypeEnum);

        lyftButton.setRideParams(rideParamsBuilder.build());
        lyftButton.load();
    }

    private static boolean isLyftAvailable(RideTypesResponse rideTypesResponse) {
        return rideTypesResponse != null && rideTypesResponse.ride_types != null && !rideTypesResponse.ride_types.isEmpty();
    }







}
