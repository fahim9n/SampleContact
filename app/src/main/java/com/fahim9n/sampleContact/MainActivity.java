package com.fahim9n.sampleContact;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewAdapter mAdapter;
    DatabaseHandler db;
    public TextView mTitle,mNumber;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            mTitle = (TextView) findViewById(R.id.tvName);
            mNumber = (TextView) findViewById(R.id.textView);
        mTitle.setText("Fahim Hossain");
        mNumber.setText("+881672356969");

         pd = new ProgressDialog(this);
        pd.setMessage("Loading");

        showContacts();
    }

    private void getContacts() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            db.addContact(new Contact(0,name,phoneNumber));
            //Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();




        }
        phones.close();
        loadRecyclerView();

    }

    private void loadRecyclerView() {
        pd.dismiss();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        List<Contact> contactList= db.getFirstTenContacts();
        mAdapter = new RecyclerViewAdapter(this,contactList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new StickyFooterItemDecoration());
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            pd.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    db = new DatabaseHandler(MainActivity.this);
                    getContacts();
                }
            },10);




        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        db.deleteAllContacts();
    }
}
