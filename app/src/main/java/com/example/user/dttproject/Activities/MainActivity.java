package com.example.user.dttproject.Activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.dttproject.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainAvtivity";

    private LinearLayout locationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        //set Main title
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.mainTitle);

        findWidgets();

        //Check google play Services and continue
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internetKontrol()) {
                    Log.i("no","internet");
                    if (isServiceOk(MainActivity.this))
                        startActivity(new Intent(MainActivity.this, mapsActivity.class));
                } else
                    wifiScreen();
            }
        });

    }

    //grabbing the widgets
    public void findWidgets() {
        locationButton = (LinearLayout) findViewById(R.id.location_button);
    }

    //Google play service checck
    public boolean isServiceOk(Context context) {
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (availability == ConnectionResult.SUCCESS) {
            Log.i(TAG, "isServiceOk:Google play Service Successfull");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availability)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, availability, 9001);
            Log.i(TAG, "isServiceOk: Fixable error");
            dialog.show();
        } else
            Toast.makeText(MainActivity.this, "Google Service Error", Toast.LENGTH_LONG).show();

        return false;
    }

    //check if internet available

    public boolean internetKontrol() {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            Toast.makeText(MainActivity.this, "Please Check your internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (info.isRoaming()) {
            return false;
        }
        return true;
    }

    //open wifi screen

    public void wifiScreen() {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

    }


    //About Icon on the Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_menu) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        dialog.show();
        builder.setTitle("Warning");
        builder.setMessage("Do you want to leave?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.cancel();
            }
        });

    }


}
