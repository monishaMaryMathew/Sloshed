package com.monisha.samples.sloshed.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.adapters.ContactsRecyclerAdapter;
import com.monisha.samples.sloshed.dbmodels.BlockedContactDB;
import com.monisha.samples.sloshed.dbmodels.EmergencyContactDB;
import com.monisha.samples.sloshed.models.Contact;
import com.monisha.samples.sloshed.util.AppDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class getContactList extends AppCompatActivity {
    private Activity context;
    //RecyclerView recyclerView;
    private RecyclerView recyclerView;
    private ContactsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button cancelBtn, submitBtn;
    private boolean blockedCaseAndNotEmergencyCase = false;
    private RelativeLayout progressLayout;
    private AppDatabase db;
    private List<BlockedContactDB> b_list = new ArrayList<>();
    private List<EmergencyContactDB> e_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contact_list);

        if (this.getIntent().hasExtra("case")) {
            String caseType = this.getIntent().getStringExtra("case");
            if (caseType.equals("blocked")) {
                blockedCaseAndNotEmergencyCase = true;
            } else if (caseType.equals("emergency")) {
                blockedCaseAndNotEmergencyCase = false;
            }
        }

        progressLayout = findViewById(R.id.progressbar_layout);
        cancelBtn = findViewById(R.id.cancel_btn);
        submitBtn = findViewById(R.id.submit_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentBackToCallingActivity();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });

        this.context = context;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            (new LoadAsync()).execute();
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                return true;
            }
        });
        return true;
    }




    private void onSubmit() {
        if (blockedCaseAndNotEmergencyCase) {
            //blocked case
            b_list = makeBlockedList(mAdapter.getCheckedList());
        } else {
            //emergency case
            e_list = makeEmergencyList(mAdapter.getCheckedList());
        }
        (new StoreAsync()).execute();
    }

    private ArrayList<Contact> createList() {
        ArrayList<Contact> mArrayList = new ArrayList<>();
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                //plus any other properties you wish to query
        };

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        } catch (SecurityException e) {
            //SecurityException can be thrown if we don't have the right permissions
        }

        if (cursor != null) {
            try {
                HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
                int indexOfNormalizedNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int indexOfDisplayName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexOfDisplayNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                while (cursor.moveToNext()) {
                    String normalizedNumber = cursor.getString(indexOfNormalizedNumber);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String displayName = cursor.getString(indexOfDisplayName);
                        String displayNumber = cursor.getString(indexOfDisplayNumber);
                        //haven't seen this number yet: do something with this contact!
                        Log.d("displayName",displayName);
                        Log.d("displayNumber",displayNumber);
                        mArrayList.add(new Contact(displayNumber, displayName));
                    } else {
                        //don't do anything with this contact because we've already found this number
                    }
                }
            } finally {
                cursor.close();
            }

        }
        Collections.sort(mArrayList);
        return mArrayList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 100:
                    (new LoadAsync()).execute();
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    break;
            }
        }

    }

    /*private void createList1() {
        Cursor cur = getContacts();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        String[] fields = new String[]{ContactsContract.Data.DISPLAY_NAME};
        ArrayList<Contact> mArrayList = new ArrayList<Contact>();
        int columnIndex_name = cur.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
        int columnIndex_id = cur.getColumnIndex(ContactsContract.Data._ID);
        int columnIndex_phone = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int columnIndex_has = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER);
        while (cur.moveToNext()) {
            mArrayList.add(cur.getString(columnIndex_name)); //add the item
            Log.d("TAG", "_ID" + cur.getString(columnIndex_id));
//            Log.d("TAG", "Phone"+  cur.getString(columnIndex_phone));
//            Log.d("TAG", "has phone"+  cur.getString(columnIndex_has));
        }
        //SimpleCursorRecyclerAdapter adapter =new SimpleCursorRecyclerAdapter(R.layout.activity_get_contact_list,cur,fields,new int[]{R.id.text_list_view});
               *//* new SimpleCursorAdapter(this,
                        R.layout.activity_get_contact_list,
                        cur,
                        fields,
                        new int[] {R.id.text_list_view});*//*
        mAdapter = new ContactsRecyclerAdapter(mArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection =
                new String[]{ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                        ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        return getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }*/

    private void intentBackToCallingActivity() {
        Intent intent = new Intent(getContactList.this, SettingsActivity.class);
        if (blockedCaseAndNotEmergencyCase) {
            //Block Contacts case

        } else {
            //Emergency Contacts case

        }
        startActivity(intent);
    }

    private ArrayList<BlockedContactDB> makeBlockedList(List<Contact> list) {
        ArrayList<BlockedContactDB> b_list = new ArrayList<>();
        for (Contact c : list) {
            b_list.add(new BlockedContactDB(c.getName(), c.getPhoneNumber()));
        }
        return b_list;
    }

    private ArrayList<EmergencyContactDB> makeEmergencyList(List<Contact> list) {
        ArrayList<EmergencyContactDB> e_list = new ArrayList<>();
        for (Contact c : list) {
            e_list.add(new EmergencyContactDB(c.getName(), c.getPhoneNumber()));
        }
        return e_list;
    }

    private List<Contact> getCheckedList() {
        List<Contact> checkedList = new ArrayList<>();
        if (blockedCaseAndNotEmergencyCase) {
            for (BlockedContactDB b : b_list) {
                checkedList.add(new Contact(b.phoneNumber, b.contactName));
            }
        } else {
            for (EmergencyContactDB e : e_list) {
                checkedList.add(new Contact(e.phoneNumber, e.contactName));
            }
        }
        return checkedList;
    }

    class LoadAsync extends AsyncTask<Void, Void, Void> {
        ArrayList<Contact> mArrayList = new ArrayList<Contact>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLayout.setVisibility(View.VISIBLE);
            recyclerView = findViewById(R.id.my_recycler_view);
            // use this setting to
            // improve performance if you know that changes
            // in content do not change the layout size
            // of the RecyclerView
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContactList.this);
            recyclerView.setLayoutManager(layoutManager);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new ContactsRecyclerAdapter(mArrayList);
            (new LoadCheckedAsync()).execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mArrayList = createList();
            return null;
        }
    }

    private class StoreAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressLayout.setVisibility(View.GONE);
            intentBackToCallingActivity();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initializeDB();
            updateOrInsert();
            return null;
        }

        private void updateOrInsert() {
            if (blockedCaseAndNotEmergencyCase) {
                updateOrInsertBlocked();
            } else {
                updateOrInsertEmergency();
            }
        }

        private void initializeDB() {
            db = AppDatabase.getAppDatabase(getContactList.this);

        }

        private void updateOrInsertEmergency() {
            List<EmergencyContactDB> contacts = db.emergencyContactDAO().getAll();
            if ((contacts != null && contacts.size() > 0)) {
                for (EmergencyContactDB d : contacts) {
                    db.emergencyContactDAO().delete(d);
                }
            }
            for (EmergencyContactDB b : e_list) {
                db.emergencyContactDAO().insertAll(b);
            }
        }

        private void updateOrInsertBlocked() {
            List<BlockedContactDB> contacts = db.blockedContactDAO().getAll();
            if ((contacts != null && contacts.size() > 0)) {
                for (BlockedContactDB d : contacts) {
                    db.blockedContactDAO().delete(d);
                }
            }
            for (BlockedContactDB b : b_list) {
                db.blockedContactDAO().insertAll(b);
            }
        }
    }

    private class LoadCheckedAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressLayout.setVisibility(View.GONE);

            mAdapter.setCheckedList(getCheckedList());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);
            progressLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initializeDB();
            readFromDB();
            return null;
        }

        private void initializeDB() {
            db = AppDatabase.getAppDatabase(getContactList.this);
        }

        private void readFromDB() {
            if (blockedCaseAndNotEmergencyCase) {
                readFromBlocked();
            } else {
                readFromEmergency();
            }
        }

        private void readFromBlocked() {
            b_list = db.blockedContactDAO().getAll();
        }

        private void readFromEmergency() {
            e_list = db.emergencyContactDAO().getAll();
        }

    }


}
