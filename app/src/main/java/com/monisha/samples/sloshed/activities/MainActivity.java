package com.monisha.samples.sloshed.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.dbmodels.BlockedContactDB;
import com.monisha.samples.sloshed.dbmodels.DrinkDB;
import com.monisha.samples.sloshed.dbmodels.EmergencyContactDB;
import com.monisha.samples.sloshed.dbmodels.UserDB;
import com.monisha.samples.sloshed.fragments.CabBookingFragment;
import com.monisha.samples.sloshed.fragments.CheckRateFragment;
import com.monisha.samples.sloshed.fragments.DashboardFragment;
import com.monisha.samples.sloshed.fragments.DrinkSelectionFragment;
import com.monisha.samples.sloshed.fragments.MealFragment;
import com.monisha.samples.sloshed.fragments.MeterFragment;
import com.monisha.samples.sloshed.fragments.StartNightFragment;
import com.monisha.samples.sloshed.fragments.TipsFragment;
import com.monisha.samples.sloshed.models.Drink;
import com.monisha.samples.sloshed.models.User;
import com.monisha.samples.sloshed.util.AppDatabase;
import com.monisha.samples.sloshed.util.StageEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

//import com.monisha.samples.sloshed.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements
        CheckRateFragment.OnFragmentInteractionListener,
        DashboardFragment.OnFragmentInteractionListener,
        TipsFragment.OnFragmentInteractionListener,
        StartNightFragment.OnFragmentInteractionListener,
        MealFragment.OnFragmentInteractionListener,
        MeterFragment.OnFragmentInteractionListener,
        CabBookingFragment.OnFragmentInteractionListener,
        DrinkSelectionFragment.OnFragmentInteractionListener

{
   /* public boolean bSession = false;
    private Date startTime ;
    private int nSession;*/

    public Drink previousDrink = null;
    public User user = new User();
    private DrinkDB drinkDBObj = null;
    private FragmentManager fragmentManager = getFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private StageEnum checkRateStage = StageEnum.START_MY_NIGHT;

    //public Drink previousDrink = null;
    //public User user = new User();

    private int minAfterLastMeal = 0;
    private RelativeLayout progressLayout;
    private AppDatabase db;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_check_rate:
                    fragment = new CheckRateFragment().newInstance(checkRateStage);
                    break;
                case R.id.navigation_settings:
                    fragment = new TipsFragment();
                    break;
            }
            if(fragment!=null){
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                return true;
            }
            return false;
        }
    };

    //@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upper_nav_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressLayout = findViewById(R.id.progressbar_layout_main);
        setProgressLayout(false);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.upper_nav_settings);
//        setSupportActionBar(myToolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_check_rate);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CheckRateFragment());
        fragmentTransaction.commit();

        (new LoadUserDBTask()).execute();
    }

    // @Override
    public void onSettingsFragmentInteraction(Uri uri) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void onDashboardFragmentInteraction(Uri uri) {
//
//    }

    @Override
    public int onCheckRateFragmentInteractionGetMin() {
        return minAfterLastMeal;
    }

    public StageEnum getCheckRateStage(){
        return checkRateStage;
    }

    public void setCheckRateStage(StageEnum stage) {
        checkRateStage = stage;
    }

    @Override
    public void onStartNightFragmentInteraction() {
        setCheckRateStage(StageEnum.START_MY_NIGHT.getNext());
        setCheckRateFragment();
    }

    @Override
    public void onMealFragmentInteractionBackBtnPressed() {
        setCheckRateStage(StageEnum.MEAL_DETAILS.getPrevious());
        setCheckRateFragment();
    }

    @Override
    public void onMealFragmentInteractionNextBtnPressed(int minAfterLastMeal) {
        this.minAfterLastMeal = minAfterLastMeal;
        createNewDrinkDB();
        setCheckRateStage(StageEnum.METER_WITH_DRINK);
        setCheckRateFragment();
    }

    private void setCheckRateFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CheckRateFragment().newInstance(getCheckRateStage()));
        fragmentTransaction.commit();
    }

    private void setCabFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CabBookingFragment());
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //TODO
        //Add exit app function
    }

    @Override
    public void onMeterFragmentInteraction() {
        setCabFragment();
    }

    @Override
    public void getDrinksListing() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new DrinkSelectionFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onCabBookingFragmentInteraction() {

    }

    @Override
    public void onDrinkFragmentInteraction(int buttonCase, Drink drink)
    {
        switch (buttonCase) {
            case 0:
                //Back/Cancel button
                setCheckRateStage(StageEnum.METER);
                setCheckRateFragment();
                break;
            case 1:
                //Add button
                //check if there is a valid selection
                if (drink != null && drink.getAlcoholPercentage() != 0 && drink.getQuantity() != 0) {
                    previousDrink = drink; //update previous drink
                    user.addDrink(previousDrink);//add it to the list in the user object
                    (new ChartDbWorkAsyncTask()).execute();
                    setCheckRateStage(StageEnum.METER);
                    setCheckRateFragment();
                } else {
                    //else if it is incomplete, show correct message
                    //Show toast
                    Toast.makeText(this, "Please select an appropriate percentage and quantity for your drink to proceed.", Toast.LENGTH_SHORT).show();
                    //Remain on the same screen
                }
                break;
        }
    }

    public void btnOpenSettings_onClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void setProgressLayout(boolean isSet) {
        if (isSet) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please complete the settings before proceeding")
                .setTitle("Settings Required")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                })
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onTipsFragmentInteraction(Uri uri) {

    }

    public void createNewDrinkDB() {
        (new CreateNewDrinkDBTask()).execute();
    }

    class LoadUserDBTask extends AsyncTask<Void, Void, Void> {
        UserDB userDBobj = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressLayout(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (userDBobj != null) {
                user.setUserFromDB(userDBobj);
                (new LoadBlockedDBTask()).execute();
            } else {
                createDialog();
            }
            setProgressLayout(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initialize();
            readFromDB();
            return null;
        }

        private void initialize() {
            db = AppDatabase.getAppDatabase(MainActivity.this);
        }

        private void readFromDB() {
            /*List<BlockedContactDB> bcs = db.blockedContactDAO().getAll();
            */
            List<UserDB> bcs = db.userDAO().getAll();
            if ((bcs == null) || (bcs != null && bcs.size() == 0)) {
                Log.d("TAG", "ouch");
            }
            for (UserDB bc : bcs) {
                userDBobj = bc;
                Log.d("TAG", "age:" + bc.age + ", phone:" + bc.weight);
            }

        }
    }

    class LoadBlockedDBTask extends AsyncTask<Void, Void, Void> {
        List<BlockedContactDB> blockedDBList = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressLayout(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (blockedDBList != null && blockedDBList.size() > 0) {
                user.setBlockedContacts(blockedDBList);
            }
            (new LoadEmergencyDBTask()).execute();
            setProgressLayout(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initialize();
            readFromDB();
            return null;
        }

        private void initialize() {
            db = AppDatabase.getAppDatabase(MainActivity.this);
        }

        private void readFromDB() {
            blockedDBList = db.blockedContactDAO().getAll();
        }
    }

    class LoadEmergencyDBTask extends AsyncTask<Void, Void, Void> {
        List<EmergencyContactDB> emergencyDBList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressLayout(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (emergencyDBList != null && emergencyDBList.size() > 0) {
                user.setEmergencyContacts(emergencyDBList);
            }
            (new LoadDrinkDBTask()).execute();
            setProgressLayout(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initialize();
            readFromDB();
            return null;
        }

        private void initialize() {
            db = AppDatabase.getAppDatabase(MainActivity.this);
        }

        private void readFromDB() {
            emergencyDBList = db.emergencyContactDAO().getAll();
        }
    }

    public class ChartDbWorkAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressLayout(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setProgressLayout(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initializeDB();
            updateDrinkDB();
            return null;
        }

        private void initializeDB() {
            db = AppDatabase.getAppDatabase(MainActivity.this);
        }

        private void updateDrinkDB() {
            Date now = new Date();
            drinkDBObj.setEnd_time(now);
            float count = drinkDBObj.getDrinkCount();
            drinkDBObj.setDrinkCount(count + previousDrink.getDrinkCount());
            db.drinkDAO().update(drinkDBObj);
        }
    }

    class LoadDrinkDBTask extends AsyncTask<Void, Void, Void> {
        private boolean isContinuation = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressLayout(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (user.getWeight() == 0) {
                createDialog();
            } else if (isContinuation) {
                //directly move to meter fragment
                setCheckRateStage(StageEnum.METER);
                setCheckRateFragment();
            }
            setProgressLayout(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initializeDB();
            loadFromDrinkDB();
            return null;
        }

        private void initializeDB() {
            db = AppDatabase.getAppDatabase(MainActivity.this);
        }

        private void loadFromDrinkDB() {
            drinkDBObj = new DrinkDB(0, new Date(), 0, null, null, 0); //dummy object creation
            List<DrinkDB> entries = db.drinkDAO().getAll();
            TreeMap<Date, DrinkDB> dateTreeMap = new TreeMap();
            for (DrinkDB entry : entries) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String d = sdf.format(entry.getStart_time());
                    Date date = sdf.parse(d);
                    dateTreeMap.put(date, entry);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }//end of for
            //get the latest entry
            if (dateTreeMap.size() > 0) {
                DrinkDB latestEntry = dateTreeMap.lastEntry().getValue();
                try {
                    int latestSession = latestEntry.getSession();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date latestDate = latestEntry.getStart_time();
                    String formattedLatestDate = sdf.format(latestDate);
                    Date today = new Date();
                    String formattedToday = sdf.format(today);
                    if (formattedLatestDate.equals(formattedToday)) {
                        //same day
                        drinkDBObj = latestEntry;
                        user.setCurrentDrinkCount(drinkDBObj.getDrinkCount());
                        isContinuation = true;
                    } else {
                        //new day
                        drinkDBObj.setSession(latestSession + 1); //next session
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                //No entries in DB
                drinkDBObj.setSession(1); //first session
            }
        }
    }

    class CreateNewDrinkDBTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            db = AppDatabase.getAppDatabase(MainActivity.this);
            float drinkCount = 0;
            float bac = 0;
            Date now = new Date();
            drinkDBObj = new DrinkDB(drinkDBObj.getSession(),
                    now, //timestamp
                    drinkCount,
                    now, //start_time
                    now, //end_time
                    bac);
            db.drinkDAO().insertAll(drinkDBObj);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressLayout(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setProgressLayout(false);
        }
    }
}
