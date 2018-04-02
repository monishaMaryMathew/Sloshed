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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
                .setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onTipsFragmentInteraction(Uri uri) {

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

    private class ChartDbWorkAsyncTask extends AsyncTask<Void, Void, Void> {
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

    /*private class ChartDbWorkAsyncTask1 extends AsyncTask<Void, Void, Void>
    {
        private float drinkCount;

        public void setCount(float drinkCount)
        {
            this.drinkCount = drinkCount;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            initializeDB();
//            deleteAll();
            if(bSession)
                doUpdate();
            else
                doInsert();
//            insertToDB();
            getAndDisplayAll();
            return null;
        }

        private void doInsert()
        {
            bSession = true;
            Date now = new Date();
            startTime = now;
            nSession = new Random().nextInt();
            DrinkDB drinks = new DrinkDB(nSession,now,drinkCount, now, now,0);
            updateOrInsert(drinks); //needs to insert, since it is the first time
//            getAndDisplayAll();
        }

        private void doUpdate()
        {
            Date now = new Date();
            DrinkDB drinks = new DrinkDB(nSession,startTime,drinkCount, startTime, now,0);
            updateOrInsert(drinks);
//            getAndDisplayAll();
        }

        private void initializeDB()
        {
            db = AppDatabase.getAppDatabase(MainActivity.this);
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
                contactToBeUpdated.setDrinkCount(contactToBeAdded.drinkCount + contacts.get(0).drinkCount);
                db.drinkDAO().update(contactToBeUpdated);
                Log.d("TAG", "Updated contact");
            }
        }

        private void display(List<DrinkDB> cs)
        {
            if ((cs == null) || (cs != null && cs.size() == 0))
            {
                Log.d("TAG", "ouch");
            }
            for (DrinkDB bc : cs)
            {
                Log.d("TAG", "timestamp:" + bc.timestamp.toString() + "count:" + bc.drinkCount + "timestart:" + bc.start_time + "end:" + bc.end_time + "bac:" + bc.bac);
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
    }*/

    class LoadDrinkDBTask extends AsyncTask<Void, Void, Void> {

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
            List<DrinkDB> entries = db.drinkDAO().getAll();
            TreeMap<Date, DrinkDB> dateTreeMap = new TreeMap(new Comparator<Map.Entry<Date, DrinkDB>>() {
                @Override
                public int compare(Map.Entry<Date, DrinkDB> t1, Map.Entry<Date, DrinkDB> t2) {
                    Date t1_date = t1.getKey();
                    Date t2_date = t2.getKey();
                    //Ascending order
                    //latest entry last
                    if (t1_date.after(t2_date)) {
                        return 1;
                    } else if (t1_date.before(t2_date)) {
                        return -1;
                    } else if (t1_date.equals(t2_date)) {
                        return 0;
                    }
                    return 0;
                }
            });
            for (DrinkDB entry : entries) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date d = sdf.parse(entry.getStart_time().toString());
                    dateTreeMap.put(d, entry);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }//end of for
            //get the latest entry
            if (dateTreeMap.size() > 0) {
                DrinkDB latestEntry = (DrinkDB) dateTreeMap.lastEntry();
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
                    } else {
                        //new day
                        createNewDrinkDB(latestSession + 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                //No entries in DB
                createNewDrinkDB(1); //first session
            }
        }

        private void createNewDrinkDB(int session) {
//            public DrinkDB(@NonNull int session, @NonNull Date timestamp, float drinkCount, Date start_time, Date end_time, float bac)
            float drinkCount = 0;
            float bac = 0;
            Date now = new Date();
            drinkDBObj = new DrinkDB(session,
                    now, //timestamp
                    drinkCount,
                    now, //start_time
                    now, //end_time
                    bac);
            db.drinkDAO().insertAll(drinkDBObj);
        }
    }
}
