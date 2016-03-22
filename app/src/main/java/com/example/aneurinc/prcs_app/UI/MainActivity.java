package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.aneurinc.prcs_app.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // debug tag
    public static final String DEBUG_TAG = "ASC";

    // fragment tags
    private static final String E_TAG = "EVENTS";
    private static final String A_TAG = "ARTISTS";
    private static final String T_TAG = "TICKETS";
    private static final String F_TAG = "FOLLOWING";

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        initButtonListeners();

        createFragment(new EventFragment(), E_TAG);

        fragmentManager = getSupportFragmentManager();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationOnClickListener(toolbar);
    }

    private void setNavigationOnClickListener(Toolbar t) {
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initButtonListeners() {

        // get buttons
        Button btnEvents = (Button) findViewById(R.id.btn_event);
        Button btnArtists = (Button) findViewById(R.id.btn_artist);
        Button btnTickets = (Button) findViewById(R.id.btn_tickets);
        Button btnFollowing = (Button) findViewById(R.id.btn_venue);

        // set listeners
        btnEvents.setOnClickListener(this);
        btnTickets.setOnClickListener(this);
        btnArtists.setOnClickListener(this);
        btnFollowing.setOnClickListener(this);

    }

    private void createFragment(Fragment fragment, String tag) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.contentFragmentMain, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {


        if (fragmentManager.getBackStackEntryCount() > 1) {
            super.onBackPressed();
            updateCurrentFragment();
        } else {
            // exit app if back stack == 1
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    private void updateCurrentFragment() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(E_TAG);

        if (fragment != null && fragment.isVisible()) {
            updateButtons(R.id.btn_event);
            return;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(T_TAG);

        if (fragment != null && fragment.isVisible()) {
            updateButtons(R.id.btn_tickets);
            return;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(A_TAG);

        if (fragment != null && fragment.isVisible()) {
            updateButtons(R.id.btn_artist);
            return;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(F_TAG);

        if (fragment != null && fragment.isVisible()) {
            updateButtons(R.id.btn_venue);
            return;
        }
    }

    private void updateButtons(int btnID) {

        Button btnEvents = (Button) findViewById(R.id.btn_event);
        Button btnArtists = (Button) findViewById(R.id.btn_artist);
        Button btnTickets = (Button) findViewById(R.id.btn_tickets);
        Button btnFollowing = (Button) findViewById(R.id.btn_venue);

        // set 4 buttons to clickable
        btnEvents.setClickable(true);
        btnArtists.setClickable(true);
        btnTickets.setClickable(true);
        btnFollowing.setClickable(true);

        // set 4 buttons text to black
        btnEvents.setTextColor(Color.BLACK);
        btnArtists.setTextColor(Color.BLACK);
        btnTickets.setTextColor(Color.BLACK);
        btnFollowing.setTextColor(Color.BLACK);

        // get selected button
        Button selectedBtn = (Button) findViewById(btnID);

        // set to non-clickable and grey
        selectedBtn.setClickable(false);
        selectedBtn.setTextColor(Color.GRAY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.ab_search:
                Log.d(DEBUG_TAG, "Action Bar: Search");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_event:
                createFragment(new EventFragment(), E_TAG);
                updateButtons(R.id.btn_event);
                break;

            case R.id.btn_artist:
                createFragment(new ArtistFragment(), A_TAG);
                updateButtons(R.id.btn_artist);
                break;

            case R.id.btn_tickets:
                createFragment(new TicketFragment(), T_TAG);
                updateButtons(R.id.btn_tickets);
                break;

            case R.id.btn_venue:
                createFragment(new VenueFragment(), F_TAG);
                updateButtons(R.id.btn_venue);
                break;

            default:
                break;

        }
    }
}
