package com.monisha.samples.sloshed.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.monisha.samples.sloshed.R;
import com.monisha.samples.sloshed.util.RecyclerAdapter;

import java.util.ArrayList;


public class getContactList extends AppCompatActivity {
    private Activity context;
    //RecyclerView recyclerView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contact_list);
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
            Cursor cur = getContacts();
            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            // use this setting to
            // improve performance if you know that changes
            // in content do not change the layout size
            // of the RecyclerView
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            String[] fields = new String[] {ContactsContract.Data.DISPLAY_NAME};
            ArrayList<String> mArrayList = new ArrayList<String>();
            int columnIndex=cur.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
            while(cur.moveToNext()) {
                mArrayList.add(cur.getString(columnIndex)); //add the item
            }
            //SimpleCursorRecyclerAdapter adapter =new SimpleCursorRecyclerAdapter(R.layout.activity_get_contact_list,cur,fields,new int[]{R.id.text_list_view});
               /* new SimpleCursorAdapter(this,
                        R.layout.activity_get_contact_list,
                        cur,
                        fields,
                        new int[] {R.id.text_list_view});*/
            mAdapter = new RecyclerAdapter(mArrayList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);
        }

    }

    private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection =
                new String[]{ ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        return getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }


}
