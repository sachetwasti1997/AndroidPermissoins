package com.sachet.contentproviderexample1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int REQUEST_CODE_READ_CONTACTS = 1;
    private ListView contact_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final int hasPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS);
        contact_list = findViewById(R.id.contact_list);

        if(hasPermission == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "onCreate: the permission is granted");
//            READ_CONTACTS_GRANTED = true;
        }else{
            Log.d(TAG, "onCreate: requesting permission first");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CODE_READ_CONTACTS);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starts");
                if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                    String[] projection = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
                    ContentResolver contentResolver = getContentResolver();
                    Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                            projection,
                            null,
                            null,
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
                    if (cursor != null) {
                        List<String> lst = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            lst.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
                        }
                        ContentAdapter arrayAdapter = new ContentAdapter(MainActivity.this, lst);
                        contact_list.setAdapter(arrayAdapter);
                    }
                }
                else{
                    Snackbar.make(view,"Allow the permission first",Snackbar.LENGTH_INDEFINITE).
                            setAction("Action", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_CONTACTS)){
                                        Log.d(TAG, "Snackbar onClick: requesting again");
                                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},
                                                REQUEST_CODE_READ_CONTACTS);
                                    }else{
                                        Log.d(TAG, "Snackbar onClick: opening the settings");
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                                        Log.d(TAG, "Snackbar onClick: the URI is "+uri.toString());
                                        intent.setData(uri);
                                        MainActivity.this.startActivity(intent);
                                    }
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: starts");

        switch (requestCode){

            case REQUEST_CODE_READ_CONTACTS:{

                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
//                    READ_CONTACTS_GRANTED = true;

                }else {

                    Log.d(TAG, "onRequestPermissionsResult: permission denied");

                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
