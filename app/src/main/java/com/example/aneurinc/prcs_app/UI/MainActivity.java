package com.example.aneurinc.prcs_app.UI;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // debug tag
    public static final String DEBUG_TAG = "PRCS";

    // fragment tags
    private static final String E_TAG = "EVENTS";
    private static final String A_TAG = "ARTISTS";
    private static final String T_TAG = "TICKETS";
    private static final String V_TAG = "VENUE";

    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        setOnClickListeners();

        Button events = (Button) findViewById(R.id.btn_event);
        createFragment(new EventFragment(), E_TAG, events);

        fragmentManager = getSupportFragmentManager();
    }

    public void createFragment(Fragment fragment, String tag, View v) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        setCustomAnimation(transaction, v);
        transaction.replace(R.id.contentFragmentMain, fragment, tag);
        transaction.commit();

    }

    private void setCustomAnimation(FragmentTransaction t, View v) {

        int id = v.getId();

        switch (getFragmentTag()) {

            case E_TAG:
                t.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            case A_TAG:
                if (id == R.id.btn_venue || id == R.id.btn_tickets) {
                    t.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    t.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                break;

            case V_TAG:
                if (id == R.id.btn_tickets) {
                    t.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    t.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                break;

            case T_TAG:
                t.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                break;

            default:
                break;
        }

    }

    private String getFragmentTag() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(E_TAG);

        if (fragment != null && fragment.isVisible()) {
            return E_TAG;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(T_TAG);

        if (fragment != null && fragment.isVisible()) {
            return T_TAG;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(A_TAG);

        if (fragment != null && fragment.isVisible()) {
            return A_TAG;
        }

        return V_TAG;
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbarTitle.setText(R.string.current_events);
    }

    private void setOnClickListeners() {

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

        switch (getFragmentTag()) {
            case A_TAG:
                updateButtons(R.id.btn_artist);
                break;
            case V_TAG:
                updateButtons(R.id.btn_venue);
                break;
            case T_TAG:
                updateButtons(R.id.btn_tickets);
                break;
            case E_TAG:
                updateButtons(R.id.btn_event);
                break;
            default:
                break;
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

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.tb_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setGravity(Gravity.RIGHT);

        menu.findItem(R.id.tb_home).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.tb_search:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        switch (v.getId()) {

            case R.id.btn_event:
                createFragment(new EventFragment(), E_TAG, v);
                updateButtons(R.id.btn_event);
                toolbarTitle.setText(R.string.current_events);
                break;

            case R.id.btn_artist:
                createFragment(new ArtistFragment(), A_TAG, v);
                updateButtons(R.id.btn_artist);
                toolbarTitle.setText(R.string.current_artists);
                break;

            case R.id.btn_tickets:
                createFragment(new TicketFragment(),T_TAG, v);
                updateButtons(R.id.btn_tickets);
                toolbarTitle.setText(R.string.my_tickets);
                break;

            case R.id.btn_venue:
                createFragment(new VenueFragment(), V_TAG, v);
                updateButtons(R.id.btn_venue);
                toolbarTitle.setText(R.string.current_venues);
                break;

            default:
                break;

        }
    }
}
