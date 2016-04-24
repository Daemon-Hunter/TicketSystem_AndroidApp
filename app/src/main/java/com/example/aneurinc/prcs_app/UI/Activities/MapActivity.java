package com.example.aneurinc.prcs_app.UI.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener {

    private final float ZOOM_VAL = 15.0f;
    private LatLng mLocation;
    private String mAddress;
    private GoogleMap mGoogleMap;

    public static String LOCATION_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpToolbar();

        // get string address from intent
        mAddress = getIntent().getStringExtra(LOCATION_ADDRESS);

        // set text view to string address
        TextView mTextViewAddress = (TextView) findViewById(R.id.venue_address);
        mTextViewAddress.setText(mAddress);

        // geocode string address to get latitude and longitude
        mLocation = geocodeAddress(mAddress);

        // initialise map fragment
        initMapFragment();

        // add onclick listeners
        addListeners();

    }

    private void addListeners() {
        Button roadMap = (Button) findViewById(R.id.btn_road_map);
        Button hybrid = (Button) findViewById(R.id.btn_hybrid);
        Button satellite = (Button) findViewById(R.id.btn_satellite);
        roadMap.setOnClickListener(this);
        hybrid.setOnClickListener(this);
        satellite.setOnClickListener(this);
    }



    private LatLng geocodeAddress(String addressStr) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(addressStr, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private void initMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.venue_map);
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

        mGoogleMap = googleMap;

        // Add a marker to location and move the camera
        googleMap.addMarker(new MarkerOptions()
                .position(mLocation)
                .title(getString(R.string.venue_address))
                .snippet(mAddress))
                .showInfoWindow();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocation, ZOOM_VAL));
        googleMap.setInfoWindowAdapter(new CustomInfoWindow(this, mAddress));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_road_map:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.btn_hybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.btn_satellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }
    }
}
