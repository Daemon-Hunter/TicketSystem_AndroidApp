package com.example.aneurinc.prcs_app.UI.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
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
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.fragments.ArtistFragment;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.fragments.ParentEventFragment;
import com.example.aneurinc.prcs_app.UI.fragments.TicketFragment;
import com.example.aneurinc.prcs_app.UI.fragments.VenueFragment;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    // Debug tag
    public static final String DEBUG_TAG = "PRCS";

    // ID intent data for fragment selection
    public static String FRAGMENT_ID;

    // Manage fragments
    private static FragmentManager fragmentManager;

    /*
    * Initialise activity and set layout
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addOnClickListeners();
        chooseFragment();

        fragmentManager = getSupportFragmentManager();
    }

    /*
    * Decides which fragment to load depending on what type is passed in the intent
    */
    private void chooseFragment() {
        switch (getIntent().getExtras().getString(FRAGMENT_ID)) {
            case "ARTIST":
                switchFragment(new ArtistFragment(), FragmentType.ARTIST);
                break;
            case "PARENT_EVENT":
                switchFragment(new ParentEventFragment(), FragmentType.PARENT_EVENT);
                break;
            case "VENUE":
                switchFragment(new VenueFragment(), FragmentType.VENUE);
                break;
        }
    }

    /*
    * Uses the fragment manager to transition between fragments
    */
    public void switchFragment(Fragment fragment, FragmentType type) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        setCustomAnimation(transaction, type);
        transaction.replace(R.id.main_content_fragment, fragment, type.toString());
        transaction.addToBackStack(null);
        transaction.commit();

        // update UI components
        updateButtons(type);

    }

    /*
    * Decides which way to slide depending on current fragment and next one
    */
    private void setCustomAnimation(FragmentTransaction trans, FragmentType type) {

        switch (getFragmentTag()) {

            case PARENT_EVENT:
                trans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            case ARTIST:
                if (type == FragmentType.VENUE || type == FragmentType.TICKET) {
                    trans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                break;

            case VENUE:
                if (type == FragmentType.TICKET) {
                    trans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                }
                break;

            case TICKET:
                trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                break;

            default:
                break;
        }

    }

    /*
    * Get current selected fragment by tag
    */
    private FragmentType getFragmentTag() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentType.PARENT_EVENT.toString());

        if (fragment != null && fragment.isVisible()) {
            return FragmentType.PARENT_EVENT;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(FragmentType.TICKET.toString());

        if (fragment != null && fragment.isVisible()) {
            return FragmentType.TICKET;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(FragmentType.ARTIST.toString());

        if (fragment != null && fragment.isVisible()) {
            return FragmentType.ARTIST;
        }

        return FragmentType.VENUE;
    }

    /*
    * Add menu fragment button onClick listeners
    */
    private void addOnClickListeners() {

        // get buttons
        Button btnEvents = (Button) findViewById(R.id.btn_event);
        Button btnArtists = (Button) findViewById(R.id.btn_artist);
        Button btnTickets = (Button) findViewById(R.id.btn_ticket);
        Button btnFollowing = (Button) findViewById(R.id.btn_venue);

        // set listeners
        btnEvents.setOnClickListener(this);
        btnTickets.setOnClickListener(this);
        btnArtists.setOnClickListener(this);
        btnFollowing.setOnClickListener(this);

    }

    /*
    * Handle back navigation
    * Determine whether to exit app or move to previous fragment
    */
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

    /*
    * Update buttons based on current fragment
    */
    private void updateCurrentFragment() {

        switch (getFragmentTag()) {
            case ARTIST:
                updateButtons(FragmentType.ARTIST);
                break;
            case VENUE:
                updateButtons(FragmentType.VENUE);
                break;
            case TICKET:
                updateButtons(FragmentType.TICKET);
                break;
            case PARENT_EVENT:
                updateButtons(FragmentType.PARENT_EVENT);
                break;
            default:
                break;
        }

    }

    /*
    * Update button colour and selection underline to match current fragment in view
    */
    private void updateButtons(FragmentType type) {

        Resources resources = getResources();

        // get reference to fragment buttons
        Button btnEvents = (Button) findViewById(R.id.btn_event);
        Button btnArtists = (Button) findViewById(R.id.btn_artist);
        Button btnTickets = (Button) findViewById(R.id.btn_ticket);
        Button btnVenues = (Button) findViewById(R.id.btn_venue);

        // get reference to fragment buttons underline
        View eventsUnderline = findViewById(R.id.event_underline);
        View artistUnderline = findViewById(R.id.artist_underline);
        View venueUnderline = findViewById(R.id.venue_underline);
        View ticketUnderline = findViewById(R.id.ticket_underline);

        // set 4 buttons to clickable
        btnEvents.setClickable(true);
        btnArtists.setClickable(true);
        btnTickets.setClickable(true);
        btnVenues.setClickable(true);

        // set 4 buttons text to black
        btnEvents.setTextColor(resources.getColor(R.color.colorGrey400));
        btnArtists.setTextColor(resources.getColor(R.color.colorGrey400));
        btnTickets.setTextColor(resources.getColor(R.color.colorGrey400));
        btnVenues.setTextColor(resources.getColor(R.color.colorGrey400));

        // hide all underlines
        eventsUnderline.setVisibility(View.INVISIBLE);
        artistUnderline.setVisibility(View.INVISIBLE);
        venueUnderline.setVisibility(View.INVISIBLE);
        ticketUnderline.setVisibility(View.INVISIBLE);

        switch (type) {
            case PARENT_EVENT:
                btnEvents.setClickable(false);
                btnEvents.setTextColor(resources.getColor(R.color.colorGrey600));
                eventsUnderline.setVisibility(View.VISIBLE);
                break;
            case ARTIST:
                btnArtists.setClickable(false);
                btnArtists.setTextColor(resources.getColor(R.color.colorGrey600));
                artistUnderline.setVisibility(View.VISIBLE);
                break;
            case VENUE:
                btnVenues.setClickable(false);
                btnVenues.setTextColor(resources.getColor(R.color.colorGrey600));
                venueUnderline.setVisibility(View.VISIBLE);
                break;
            case TICKET:
                btnTickets.setClickable(false);
                btnTickets.setTextColor(resources.getColor(R.color.colorGrey600));
                ticketUnderline.setVisibility(View.VISIBLE);
        }
    }

    /*
    * Inflate menu and add search icon to the right
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.tb_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setGravity(Gravity.RIGHT);

        return true;
    }


    /*
    * Handle menu item onClicks
    * Start corresponding activity
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about_us:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.log_out:
                startActivity(new Intent(this, LogInActivity.class));
                finish();
                break;
            case R.id.profile_settings:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            default:
                break;
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }

    /*
    * Handle onClicks for fragment buttons
    */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_event:
                switchFragment(new ParentEventFragment(), FragmentType.PARENT_EVENT);
                break;
            case R.id.btn_artist:
                switchFragment(new ArtistFragment(), FragmentType.ARTIST);
                break;
            case R.id.btn_ticket:
                switchFragment(new TicketFragment(), FragmentType.TICKET);
                break;
            case R.id.btn_venue:
                switchFragment(new VenueFragment(), FragmentType.VENUE);
                break;
            default:
                break;
        }
    }
}
