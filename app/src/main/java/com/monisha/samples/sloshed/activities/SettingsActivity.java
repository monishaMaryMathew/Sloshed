package com.monisha.samples.sloshed.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.dbmodels.UserDB;
import com.monisha.samples.sloshed.util.AppDatabase;
import com.monisha.samples.sloshed.views.NumberPickerPreference;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {


    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

    private static boolean isSaving = false;
    private static boolean isSaved = true;
    private static boolean isHeader = false;
    private static UserDB userDBobj = new UserDB();

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

//            if(preference.getKey()=="seekbar_age"){
//
//                preference.setSummary(value.toString());
//            }

            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            }

//            } else if (preference instanceof RingtonePreference) {
//                // For ringtone preferences, look up the correct display value
//                // using RingtoneManager.
//                if (TextUtils.isEmpty(stringValue)) {
//                    // Empty values correspond to 'silent' (no ringtone).
//                    preference.setSummary(R.string.pref_ringtone_silent);
//
//                } else {
//                    Ringtone ringtone = RingtoneManager.getRingtone(
//                            preference.getContext(), Uri.parse(stringValue));
//
//                    if (ringtone == null) {
//                        // Clear the summary if there was a lookup error.
//                        preference.setSummary(null);
//                    } else {
//                        // Set the summary to reflect the new ringtone display
//                        // name.
//                        String name = ringtone.getTitle(preference.getContext());
//                        preference.setSummary(name);
//                    }
//                }
//
//            }
            else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }

            if (preference.getKey().equals("list_preference_gender")) {
                userDBobj.setGender(stringValue);
                isSaved = false;
            } else if (preference.getKey().equals("list_preference_weight")) {
                ListPreference listPreference1 = (ListPreference) preference;
                int pos = -1;
                if (value != null && listPreference1.getEntries() != null) {
                    for (int i = listPreference1.getEntries().length - 1; i >= 0; i--) {
                        if (listPreference1.getEntries()[i].equals(value)) {
                            pos = i;
                            break;
                        }
                    }
                }
                if (pos != -1)
                    userDBobj.setWeight(Float.parseFloat((listPreference1.getEntryValues()[pos]).toString()));
                isSaved = false;
            } else if (preference.getKey().equals("list_preference_age123")) {
                userDBobj.setAge(Integer.parseInt(stringValue));
                isSaved = false;
            } else if (preference.getKey().equals("edit_text_preference_address")) {
                userDBobj.setAddressLine1(stringValue);
                isSaved = false;
            } else if (preference.getKey().equals("edit_text_preference_address_zip")) {
                userDBobj.setZipCode(stringValue);
                isSaved = false;
            } else if (preference.getKey().equals("edit_text_preference_drunk_message")) {
                userDBobj.setMessage(stringValue);
                isSaved = false;
            } else if (preference.getKey().equals("edit_text_preference_ThresholdBAC")) {
                userDBobj.setBacThreshold(Float.parseFloat(stringValue));
                isSaved = false;
            } else if (preference.getKey().equals("list_preference_block_time")) {
                userDBobj.setBlockedForHour(Integer.parseInt(stringValue));
                isSaved = false;
            } else {
            }

            //DBtaskObj.getStatus();
            //(new DBUserASyncTask()).execute();
            return true;
        }
    };

    private AppDatabase db;

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
//        if (preference.getKey().equals("switch_preference_shareBAC")) {
//            if (preferenc.equals("true")) {
//                userDBobj.setIsBacAllowed(1);
//            } else
//                userDBobj.setIsBacAllowed(0);
//        }
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.navigation_save) {
            (new DBUserASyncTask()).execute();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_nav_menu, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //DBtaskObj2 = new DBUserASyncTask2();
        (new DBUserASyncTask2()).execute();
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
        isHeader = true;
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onBackPressed() {
        if (isSaving) {
            Toast.makeText(this, "We need a couple of moments to save data", Toast.LENGTH_SHORT).show();
        } else if (isHeader && !isSaved) {
            (new DBUserASyncTask()).execute();
        } else if (isHeader) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "Please use to the top arrow on this screen", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        private static final String PREFERENCE_NS =
                "http://schemas.android.com/apk/res/com.mnm.seekbarpreference";
        private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
        private static final String ATTR_DEFAULT_VALUE = "defaultValue";
        private static final String ATTR_MIN_VALUE = "minValue";
        private static final String ATTR_MAX_VALUE = "maxValue";
        private NumberPickerPreference numPickAgeObj;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            isHeader = false;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("list_preference_gender"));
            //bindPreferenceSummaryToValue(findPreference("list_preference_age123"));
            bindPreferenceSummaryToValue(findPreference("edit_text_preference_address"));
            bindPreferenceSummaryToValue(findPreference("edit_text_preference_address_zip"));
            bindPreferenceSummaryToValue(findPreference("list_preference_weight"));
            //bindPreferenceSummaryToValue(findPreference("seekbar_age"));
            numPickAgeObj = (NumberPickerPreference) findPreference("list_preference_age123");

            numPickAgeObj.picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    userDBobj.setAge(numPickAgeObj.getValueCustom());
                    isSaved = false;
                }
            });

//            numPickAgeObj.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object o) {
//                    userDBobj.setAge(numPickAgeObj.getValueCustom());
//                    return false;
//                }
//            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            isHeader = true;
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        SwitchPreference shareBACBool;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            isHeader = false;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
            bindPreferenceSummaryToValue(findPreference("edit_text_preference_drunk_message"));
            //bindPreferenceSummaryToValue(findPreference("switch_preference_shareBAC"));
            bindPreferenceSummaryToValue(findPreference("edit_text_preference_ThresholdBAC"));

            shareBACBool = (SwitchPreference) findPreference("switch_preference_shareBAC");

            shareBACBool.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {

                    if (shareBACBool.getSharedPreferences().getBoolean("switch_preference_shareBAC", true)) {
                        userDBobj.setIsBacAllowed(1);
                    } else {
                        userDBobj.setIsBacAllowed(0);
                    }
                    isSaved = false;
                    return false;
                }
            });

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            isHeader = true;
        }
    }

    // Database connection ke lie

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            isHeader = false;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValue(findPreference("sync_frequency"));
            bindPreferenceSummaryToValue(findPreference("list_preference_block_time"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            isHeader = true;
        }
    }

    private class DBUserASyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isSaving = true;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(SettingsActivity.this, "Saved changes successfully!", Toast.LENGTH_SHORT).show();
            isSaved = true;
            isSaving = false;
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initializeDB();
            updateOrInsert();
            return null;
        }

        private void initializeDB() {
            db = AppDatabase.getAppDatabase(SettingsActivity.this);

        }

        private void updateOrInsert() {
            List<UserDB> contacts = db.userDAO().getAll();
            if ((contacts == null) || (contacts != null && contacts.size() == 0)) {
                //No such contact already exists in the database
                db.userDAO().insertAll(userDBobj);
                Log.d("TAG", "Inserted user");
            } else {
                //This contact already exists in the database
                UserDB userToBeUpdates = contacts.get(0);
                userToBeUpdates.updateUserObj(userDBobj);
                db.userDAO().update(userToBeUpdates);
                Log.d("TAG", "Updated user");
            }
        }
    }

    private class DBUserASyncTask2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(SettingsActivity.this, "Loaded data from storage", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initializeDB();
            readFromDB();
            return null;
        }

        private void initializeDB() {
            db = AppDatabase.getAppDatabase(SettingsActivity.this);

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

}
