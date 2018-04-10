package com.example.user.dttproject.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.dttproject.Adapters.customInfoWindowAdapter;
import com.example.user.dttproject.Fragments.PopupFragment;
import com.example.user.dttproject.R;
import com.example.user.dttproject.models.PlaceInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class mapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = "mapsActivity";
    private final int LOCATION_PERMISSION_CODE = 1234;
    boolean locationPermissionGranted = false;
    GoogleMap googleMap;
    // Construct a GeoDataClient.
    GeoDataClient mGeoDataClient;
    LinearLayout mcallButton;

    private PlaceInfo mPlace;


    // Construct a PlaceDetectionClient.
    PlaceDetectionClient mPlaceDetectionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //set title on the Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("RSR Revalidatieservice");

        //back button on the Action Bar
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mcallButton = findViewById(R.id.callbutton);
        mGeoDataClient = Places.getGeoDataClient(this, null);

        mcallButton.setOnClickListener(this);
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        getLocationPermission();
    }

    public void moveCamera(LatLng latLng, float zoom, String address) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        googleMap.setInfoWindowAdapter(new customInfoWindowAdapter(this));
        MarkerOptions markerOptions = (new MarkerOptions().position(latLng).title(String.valueOf(R.string.markerTitle)).snippet(address));
        googleMap.addMarker(markerOptions).showInfoWindow();
    }


    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapsActivity.this);
    }


    public void getLocationPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET, Manifest.permission.CHANGE_NETWORK_STATE

        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                            locationPermissionGranted = true;
                            initMap();
                        } else
                            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
                    } else
                        ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);

                } else
                    ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
            } else ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);
        } else ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSION_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.i(TAG, "Permissin Failed");
                            locationPermissionGranted = false;
                        }

                    }
                    Log.i(TAG, "Permission Granted");
                    locationPermissionGranted = true;
                    initMap();
                }
            }


        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        Log.i(TAG, "MAp is Ready");
        Log.i("granted", locationPermissionGranted + "");
        getInfoWindow();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

    }

    @Override
    public void onClick(View view) {
        if (view == mcallButton) {
            mcallButton.setVisibility(View.GONE);
            PopupFragment fragment = new PopupFragment();
            FragmentManager manager = getSupportFragmentManager();
            fragment.show(getSupportFragmentManager(), "Fragmnet");

        }

    }

    public void getInfoWindow() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        Task<PlaceLikelihoodBufferResponse> placeResult =
                mPlaceDetectionClient.getCurrentPlace(null);
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                List<Place> placeList = new ArrayList<>();

                try {
                    PlaceLikelihoodBufferResponse likelihoodBufferResponse = task.getResult();

                    for (PlaceLikelihood placeLikelihood : likelihoodBufferResponse) {
                        placeList.add(placeLikelihood.getPlace().freeze());
                    }
                    likelihoodBufferResponse.release();

                    final Place place = placeList.get(0);

                    try {
                        mPlace = new PlaceInfo();
                        mPlace.setAddress(place.getAddress().toString());
                        Log.d(TAG, "onResult: address: " + place.getAddress());
                    } catch (NullPointerException e) {
                        Log.e(TAG, "onResult: NullPointerException: " + e.getMessage());
                    }

                    moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                            place.getViewport().getCenter().longitude), 25f, mPlace.getAddress());
                    Log.i("places", mPlace.getAddress());
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mapsActivity.this,"NETWORK ERROR",Toast.LENGTH_LONG).show();
                    Log.i("No", "connection");
                }

            }

        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}




