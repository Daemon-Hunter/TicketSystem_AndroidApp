package com.example.aneurinc.prcs_app.UI.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_views.CustomInfoWindow;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final float ZOOM_VAL = 15.0f;
    private LatLng mLocation;
    private TextView mTextViewAddress;

    public static String LOCATION_ADDRESS;
    
    // // TODO: 23/04/2016 get intent!! 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpToolbar();

        mTextViewAddress = (TextView) findViewById(R.id.venue_address);

        Log.d(MainActivity.DEBUG_TAG, "onCreate: location address = " + LOCATION_ADDRESS);

        mLocation = geocodeAddress();

        Log.d(MainActivity.DEBUG_TAG, "onCreate: mLocation = " + mLocation);

        initMapFragment();

    }

    private LatLng geocodeAddress() {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(LOCATION_ADDRESS, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private void initMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText(R.string.venue_location);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.tb_search).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.tb_home:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker to location and move the camera
        googleMap.addMarker(new MarkerOptions().position(mLocation).title("Location Address:")
                .snippet(LOCATION_ADDRESS));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocation, ZOOM_VAL));
        googleMap.setInfoWindowAdapter(new CustomInfoWindow(this, LOCATION_ADDRESS));
        mTextViewAddress.setText(LOCATION_ADDRESS);
    }
}
