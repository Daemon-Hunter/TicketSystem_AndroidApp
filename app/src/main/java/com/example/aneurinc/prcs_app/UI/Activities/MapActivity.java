package com.example.aneurinc.prcs_app.UI.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_views.CustomInfoWindow;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final float ZOOM_VAL = 16.0f;
    private LatLng mLocation;
    private String mAddress;
    private IVenue mVenue;
    private GoogleMap mGoogleMap;

    private Button mRoadMapBtn;
    private Button mHybridBtn;
    private Button mSatelliteBtn;

    public static String VENUE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpToolbar();

        try {
            mVenue = UserWrapper.getInstance().getVenue(getIntent().getExtras().getInt(VENUE_ID));
        } catch (IOException e) {
            // TODO: 03/05/2016 handle
        }
        mAddress = String.format("%s %s %s", mVenue.getAddress(), mVenue.getCity(), mVenue
                .getPostcode());

        // geocode string address to get latitude and longitude
        mLocation = geocodeAddress(mAddress);

        // initialise map fragment
        initMapFragment();

        mRoadMapBtn = (Button) findViewById(R.id.btn_road_map);
        mHybridBtn = (Button) findViewById(R.id.btn_hybrid);
        mSatelliteBtn = (Button) findViewById(R.id.btn_satellite);

        // add onclick listeners
        addListeners();

    }

    private void addListeners() {
        mRoadMapBtn.setOnClickListener(this);
        mHybridBtn.setOnClickListener(this);
        mSatelliteBtn.setOnClickListener(this);
    }


    @Nullable
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.venue_map);
        mapFragment.getMapAsync(this);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.venue_location);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.tb_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null && mLocation != null) {
            mGoogleMap = googleMap;
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocation, ZOOM_VAL));
            googleMap.setInfoWindowAdapter(new CustomInfoWindow(this, mVenue));
            googleMap.addMarker(new MarkerOptions().position(mLocation)).showInfoWindow();
            updateUI();
        }

    }

    private void updateUI() {
        TextView title = (TextView) findViewById(R.id.venue_title);
        TextView phoneNo = (TextView) findViewById(R.id.venue_phone_no);
        TextView email = (TextView) findViewById(R.id.venue_email);
        TextView parking = (TextView) findViewById(R.id.venue_parking);
        ImageView image = (ImageView) findViewById(R.id.venue_image);

        title.setText(mVenue.getName());
        phoneNo.setText(mVenue.getPhoneNumber());
        email.setText(mVenue.getEmail());
        parking.setText(String.format("%s spaces", mVenue.getParking()));

        int xy = Utilities.getScreenWidth(this) / 4 + 30;
        Bitmap scaledImage = Utilities.scaleDown(mVenue.getImage(0), xy, xy);

        image.setImageBitmap(scaledImage);
    }

    private void updateButtons(Button b) {

        Resources resources = getResources();

        // set map type buttons to grey text
        mRoadMapBtn.setTextColor(resources.getColor(R.color.colorGrey400));
        mHybridBtn.setTextColor(resources.getColor(R.color.colorGrey400));
        mSatelliteBtn.setTextColor(resources.getColor(R.color.colorGrey400));

        // get reference to button underline
        View roadMapUnderline = findViewById(R.id.btn_road_map_underline);
        View hybridUnderline = findViewById(R.id.btn_hybrid_underline);
        View satelliteUnderline = findViewById(R.id.btn_satellite_underline);

        // get reference to containers
        LinearLayout roadMapContainer = (LinearLayout) findViewById(R.id.road_map_container);
        LinearLayout hybridContainer = (LinearLayout) findViewById(R.id.hybrid_container);
        LinearLayout satelliteContainer = (LinearLayout) findViewById(R.id.satellite_container);

        roadMapContainer.setBackgroundColor(resources.getColor(R.color.colorGrey200));
        hybridContainer.setBackgroundColor(resources.getColor(R.color.colorGrey200));
        satelliteContainer.setBackgroundColor(resources.getColor(R.color.colorGrey200));

        // set all underlines to invisible
        roadMapUnderline.setVisibility(View.INVISIBLE);
        hybridUnderline.setVisibility(View.INVISIBLE);
        satelliteUnderline.setVisibility(View.INVISIBLE);

        switch (b.getId()) {
            case R.id.btn_road_map:
                mRoadMapBtn.setTextColor(resources.getColor(R.color.colorGrey600));
                roadMapUnderline.setVisibility(View.VISIBLE);
                roadMapContainer.setBackgroundColor(resources.getColor(R.color.colorGrey300));
                break;
            case R.id.btn_hybrid:
                mHybridBtn.setTextColor(resources.getColor(R.color.colorGrey600));
                hybridUnderline.setVisibility(View.VISIBLE);
                hybridContainer.setBackgroundColor(resources.getColor(R.color.colorGrey300));
                break;
            case R.id.btn_satellite:
                mSatelliteBtn.setTextColor(resources.getColor(R.color.colorGrey600));
                satelliteUnderline.setVisibility(View.VISIBLE);
                satelliteContainer.setBackgroundColor(resources.getColor(R.color.colorGrey300));
                break;
        }
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

        updateButtons((Button) v);
    }
}
