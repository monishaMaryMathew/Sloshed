package com.monisha.samples.sloshed.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.activities.MainActivity;
import com.monisha.samples.sloshed.dbmodels.BlockedContactDB;
import com.monisha.samples.sloshed.dbmodels.EmergencyContactDB;
import com.monisha.samples.sloshed.models.User;
import com.monisha.samples.sloshed.util.AppDatabase;
import com.monisha.samples.sloshed.util.BlockOutgoing;

import java.util.Calendar;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeterFragment extends Fragment {
    public static final String ABORT_PHONE_NUMBER = "+13134245612";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DIALOGUE = "param1";
    private static final String ARG_MIN = "param2";
    private static final String OUTGOING_CALL_ACTION = "android.intent.action.NEW_OUTGOING_CALL";
    private static final String INTENT_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
    protected GeoDataClient mGeoDataClient;
    AppDatabase db;
    List<BlockedContactDB> blockedContacts;
    List<EmergencyContactDB> emergencyContacts;
    boolean isRegistered = false;
    // TODO: Rename and change types of parameters
    private boolean showDialog;
    private int minAfterLastMeal;
    private OnFragmentInteractionListener mListener;
    private TextView percentageTV, messageTV;
    private SpeedView meter;
    private LinearLayout drunkModeLayout;
    private Button endMyNightBtn;
    private LinearLayout addNewDrinkBtn, addPrevDrinkBtn;
    private RelativeLayout drunkDialBlock;
    private RelativeLayout sosLayout;
    private PlaceDetectionClient mPlaceDetectionClient;
    private ComponentName component;
    private Switch initiateDrunkModeSwitch;
    private float drinkCount = 0;
    

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
        mGeoDataClient = Places.getGeoDataClient(this.getActivity());
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this.getActivity());
        MainActivity mainActivity = (MainActivity)getActivity();
        User user = mainActivity.user;
        db = AppDatabase.getAppDatabase(getContext());
        if(user.getBlockedContacts().size()==0 || user.getEmergencyContacts().size()==0){
            //GetBlockedEmergencyContacts blocked = new G {
            AsyncTask task1 = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                   // AppDatabase db =  AppDatabase.getAppDatabase(getContext());
                    blockedContacts = db.blockedContactDAO().getAll();
                    //emergencyContacts = db.emergencyContactDAO().getAll();
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {

                    for(BlockedContactDB emergency: blockedContacts){
                        Log.d(emergency.getContactName(),emergency.getPhoneNumber());
                    }
                    super.onPostExecute(o);
                }


            };
            AsyncTask task2 = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                   // AppDatabase db =  AppDatabase.getAppDatabase(getContext());
                    emergencyContacts = db.emergencyContactDAO().getAll();
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {



                    for(EmergencyContactDB emergency: emergencyContacts){
                        Log.d(emergency.getContactName(),emergency.getPhoneNumber());
                    }
                    super.onPostExecute(o);
                }


            };
            //GetBlockedEmergencyContacts contact = new GetBlockedEmergencyContacts(this.getContext());
            //contact.execute();
            task1.execute();
            task2.execute();
        }
        else{
            blockedContacts= user.getBlockedContacts();
            emergencyContacts = user.getEmergencyContacts();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meter, container, false);

        meter = view.findViewById(R.id.meter);
        meter.setSpeedTextColor(ContextCompat.getColor(getContext(), R.color.colorTransparent));
        setLevels(); //TODO compute as per the user profile
        percentageTV = view.findViewById(R.id.percentage_info);
        messageTV = view.findViewById(R.id.message_info);
        drunkModeLayout = view.findViewById(R.id.drunk_mode_layout);
        sosLayout = view.findViewById(R.id.sos_layout);
        drunkDialBlock = view.findViewById(R.id.drunk_dial_layout);
        endMyNightBtn = view.findViewById(R.id.end_my_night_btn);
        component = new ComponentName(getContext(), BlockOutgoing.class);
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.SEND_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.SEND_SMS},
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            sosLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPlaces();

                }
            });
        }
        endMyNightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed();
            }
        });

        addNewDrinkBtn = view.findViewById(R.id.add_new_drink_btn);
        addPrevDrinkBtn = view.findViewById(R.id.repeat_drink_btn);

        if (((MainActivity) getActivity()).previousDrink != null && ((MainActivity) getActivity()).previousDrink.getQuantity() != 0 && ((MainActivity) getActivity()).previousDrink.getAlcoholPercentage() != 0) {
            addPrevDrinkBtn.setVisibility(View.VISIBLE);
        } else {
            addPrevDrinkBtn.setVisibility(View.GONE);
        }

        addPrevDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add code to update meter
                //udate the time stamp
                ((MainActivity) getActivity()).previousDrink.setTimestamp(Calendar.getInstance().getTimeInMillis());
                ((MainActivity) getActivity()).user.addDrink(((MainActivity) getActivity()).previousDrink);
                setMeter();//update meter
                makeText(getActivity(), "Drink has been added!", LENGTH_LONG).show();
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

        initiateDrunkModeSwitch = view.findViewById(R.id.drunk_mode_switch);
        initiateDrunkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                initiateDrunkMode(b);
            }
        });

        initiateDrunkMode(false);

        setMeter(); //TODO compute as per the drink intake
        return view;
    }

    private void setLevels() {
        int low = ((MainActivity) getActivity()).user.getLowLevel();
        Log.d("Tag", "Low:" + low);
        meter.setLowSpeedPercent(low);
        int med = ((MainActivity) getActivity()).user.getMediumLevel();
        Log.d("Tag", "medium:" + med);
        if (med >= 100) {
            med = 90;
        }
        meter.setMediumSpeedPercent(med);
    }

    private void initiateDrunkMode(final boolean initiate) {

        //final List<BlockedContactDB> BlockedContacts = data.loadBlockedContacts();

        final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        final BroadcastReceiver blockCall = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("out", "OutgoingCallReceiver onReceive");
                if (intent.getAction().equals(OUTGOING_CALL_ACTION)) {
                    Log.d("out", "OutgoingCallReceiver NEW_OUTGOING_CALL received");

                    // get phone number from bundle
                    String phoneNumber = intent.getExtras().getString(INTENT_PHONE_NUMBER);
                  //for(BlockedContactDB block: BlockedContacts) {
                     // Log.d(block.getContactName(),block.getPhoneNumber());
                    for(BlockedContactDB block:blockedContacts) {
                        if ((phoneNumber != null) && phoneNumber.trim().equals(block.getPhoneNumber().trim())) {
                            setResultData(null);
                            makeText(context, "Outgoing Call Blocked", LENGTH_SHORT).show();
                            //}
                        }
                    }
                }
            }
        };
        if (initiate) {
            drunkModeLayout.setVisibility(View.VISIBLE);

            if (ContextCompat.checkSelfPermission(this.getActivity(),
                    Manifest.permission.PROCESS_OUTGOING_CALLS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                        Manifest.permission.PROCESS_OUTGOING_CALLS)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this.getActivity(),
                            new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                            100);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {

                drunkDialBlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getContext().registerReceiver(blockCall,intentFilter);
                      // getContext().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED , PackageManager.DONT_KILL_APP);
                        isRegistered = true;
                        makeText(getContext(),"You will not be able to call the contacts you have blocked", LENGTH_LONG).show();
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                if (isRegistered) {
                                    getContext().unregisterReceiver(blockCall);
                                    isRegistered = false;
                                }
                                makeText(getContext(),"unblocking", LENGTH_LONG).show();
                            }

                        }, 60000L);
                    }
                });
            }

        } else {
            drunkModeLayout.setVisibility(View.GONE);

            //getContext().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);
        }
    }

    private void setMeter() {
        int drinkCount = (((MainActivity) getActivity()).user.getDrinkPercentage());
        String message = "";
        if (drinkCount < ((MainActivity) getActivity()).user.getLowLevel()) {
            message = "Happy drinking!";
        } else if (drinkCount < ((MainActivity) getActivity()).user.getMediumLevel()) {
            message = "You might want to slow down now";
        } else {
            message = "Warning! Auto-initiating drunk mode.";
            initiateDrunkMode(true);
            initiateDrunkModeSwitch.setEnabled(false);
        }
        meter.setSpeedAt(drinkCount);
        messageTV.setText(message);
        percentageTV.setText(((int) ((MainActivity) getActivity()).user.getDrinkCount()) + "");
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

    private void getPlaces() {
        if (ContextCompat.checkSelfPermission(this.getActivity(),
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
        }else {

            Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    StringBuffer myLoc = new StringBuffer();
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        Log.i("lo", String.format("Place '%s' has likelihood: %g",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                        if(placeLikelihood.getLikelihood()>0.10) {
                            myLoc.append("Location Name: ").append(placeLikelihood.getPlace().getName().toString()+" ").append(", Location Address: ").append(placeLikelihood.getPlace().getAddress().toString()).append(" ");
                        }

                    }
                    likelyPlaces.release();
                    for(EmergencyContactDB emergencyContact:emergencyContacts) {
                        Log.d("test",emergencyContact.getPhoneNumber());
                        makeText(getContext(),"Sending message to:"+emergencyContact.getPhoneNumber(), LENGTH_LONG).show();
                        sendSMS(emergencyContact.getPhoneNumber(), "I need HELP!.I am currently around these locations:" + myLoc.toString());
                    }

                }
            });
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        PendingIntent sentPI;
        String SENT = "SMS_SENT";

        sentPI = PendingIntent.getBroadcast(getContext(), 0,new Intent(SENT), 0);

        //sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
        //SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
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
