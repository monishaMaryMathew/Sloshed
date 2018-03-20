package com.monisha.samples.sloshed.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.fragments.CabBookingFragment;
import com.monisha.samples.sloshed.fragments.CheckRateFragment;
import com.monisha.samples.sloshed.fragments.DashboardFragment;
import com.monisha.samples.sloshed.fragments.DrinkSelectionFragment;
import com.monisha.samples.sloshed.fragments.MealFragment;
import com.monisha.samples.sloshed.fragments.MeterFragment;
import com.monisha.samples.sloshed.fragments.SettingsFragment;
import com.monisha.samples.sloshed.fragments.StartNightFragment;
import com.monisha.samples.sloshed.models.Drink;
import com.monisha.samples.sloshed.models.User;
import com.monisha.samples.sloshed.util.StageEnum;

public class MainActivity extends AppCompatActivity implements
        CheckRateFragment.OnFragmentInteractionListener,
        DashboardFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        StartNightFragment.OnFragmentInteractionListener,
        MealFragment.OnFragmentInteractionListener,
        MeterFragment.OnFragmentInteractionListener,
        CabBookingFragment.OnFragmentInteractionListener,
        DrinkSelectionFragment.OnFragmentInteractionListener

{
    private FragmentManager fragmentManager = getFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private StageEnum checkRateStage = StageEnum.START_MY_NIGHT;

    public Drink previousDrink = null;
    public User user = new User();

    private int minAfterLastMeal = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

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
                    fragment = new SettingsFragment();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_check_rate);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CheckRateFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onSettingsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDashboardFragmentInteraction(Uri uri) {

    }

    @Override
    public int onCheckRateFragmentInteractionGetMin() {
        return minAfterLastMeal;
    }

    public void setCheckRateStage(StageEnum stage){
        checkRateStage = stage;
    }

    public StageEnum getCheckRateStage(){
        return checkRateStage;
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
    public void onDrinkFragmentInteraction(int buttonCase, Drink drink) {
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
                    //if it is a correct selection
                    setCheckRateStage(StageEnum.METER);
                    setCheckRateFragment();
                    previousDrink = drink;
                } else {
                    //else if it is incomplete, show correct message
                    //Show toast
                    Toast.makeText(this, "Please select an appropriate percentage and quantity for your drink to proceed.", Toast.LENGTH_SHORT).show();
                    //Remain on the same screen
                }


                break;
        }
    }
}
