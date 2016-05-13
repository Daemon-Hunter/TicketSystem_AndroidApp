package com.example.aneurinc.prcs_app.UI.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.VenueActAdapter;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VenueActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {

    // Venue ID passed in intent
    public static String VENUE_ID;

    // The Venue
    private IVenue mVenue;

    // Venue Child Events and Parent Events
    private List<IChildEvent> mChildEvents;
    private List<IParentEvent> mParentEvents;

    // Async task to read Child Events of Venue
    private ReadChildEvents mReadTask;

    /*
    * Initialise activity
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);
        createToolbar();
        readChildEvents();
        addOnClickListeners();
    }

    /*
    * Start async read task if it is not running
    * Show progress spinner to notify user
    */
    private void readChildEvents() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(true);
            mReadTask = new ReadChildEvents(this);
            mReadTask.execute();
        }
    }

    /*
    * Called when activity is paused
    * Cancels any running background threads
    */
    @Override
    public void onPause() {
        handleQuit();
        super.onPause();
    }

    /*
    * Called when activity is stopped
    * Cancels any running background threads
    */
    @Override
    public void onStop() {
        handleQuit();
        super.onStop();
    }

    /*
    * Called when activity is destroy
    * Cancels any running background threads
    */
    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    /*
    * Quit any running background threads
    * Update progress spinner to notify user of change
    */
    private void handleQuit() {
        if (isTaskRunning(mReadTask)) {
            showProgress(false);
            mReadTask.cancel(true);
        }
    }

    /*
    * Check if thread is in Running state
    */
    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    /*
    * Add onClick listeners image views
    */
    private void addOnClickListeners() {

        ImageView facebook = (ImageView) findViewById(R.id.facebook);
        ImageView twitter = (ImageView) findViewById(R.id.twitter);
        ImageView instagram = (ImageView) findViewById(R.id.instagram);
        ImageView map = (ImageView) findViewById(R.id.venue_maps);

        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        instagram.setOnClickListener(this);
        map.setOnClickListener(this);

    }

    /*
    * Set up toolbar - title and back navigation added
    */
    private void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.venues);
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

    /*
    * Handle back navigation
    */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.VENUE.toString());
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /*
    * Inflate menu and its items
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    /*
    * Handle menu item clicks
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.tb_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Handle social media and map image onClicks
    */
    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {
            case R.id.facebook:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mVenue.getFacebook())));
                break;
            case R.id.twitter:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mVenue.getTwitter())));
                break;
            case R.id.instagram:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mVenue.getInstagram())));
                break;
            case R.id.venue_maps:
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra(MapActivity.VENUE_ID, mVenue.getID());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    /*
    * Handle list view onClicks
    * Get position and pass into Child Event activity in intent
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ChildEventActivity.class);

        int[] IDs = new int[2];
        IDs[0] = mChildEvents.get(position).getID();
        IDs[1] = mParentEvents.get(position).getID();

        intent.putExtra(ChildEventActivity.EVENT_ID, IDs);
        startActivity(intent);
    }

    /*
    * Shows progress spinner to notify user of any changes to background threads
    */
    private void showProgress(final boolean show) {
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.read_progress);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /*
    * Async task class to read Child Events from the database
    */
    private class ReadChildEvents extends AsyncTask<Void, Void, Boolean> {

        private Activity mContext;

        /*
        * Pass in reference to context of outer class
        */
        public ReadChildEvents(Activity context) {
            mContext = context;
        }

        /*
        * Execute background thread task here - read Child Events
        * Get Venue and its Child Events
        * Get Child Events Parent Events
        * Return true if the task is completed
        */
        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d(MainActivity.DEBUG_TAG, "venue activity thread started");

            try {
                mVenue = UserWrapper.getInstance().getVenue(getIntent().getExtras().getInt(VENUE_ID));
                mChildEvents = mVenue.getChildEvents();
                mParentEvents = new ArrayList<>();
                for (IChildEvent c : mChildEvents) {
                    mParentEvents.add(c.getParentEvent());
                }
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        /*
        * Callback fired once task is finished
        * If successful, success boolean is true
        * UI is updated accordingly
        */
        @Override
        protected void onPostExecute(Boolean success) {

            Log.d(MainActivity.DEBUG_TAG, "venue activity thread completed");

            mReadTask = null;

            showProgress(false);

            if (success) { // Child Events read succesfully
                RelativeLayout container = (RelativeLayout) mContext.findViewById(R.id.upcoming_events_container);
                ImageView venueImage = (ImageView) mContext.findViewById(R.id.venue_image);
                TextView venueTitle = (TextView) mContext.findViewById(R.id.venue_title);
                TextView venueDesc = (TextView) mContext.findViewById(R.id.venue_description);
                TextView venueCapacity = (TextView) mContext.findViewById(R.id.venue_capacity);
                TextView venueEmail = (TextView) mContext.findViewById(R.id.venue_email);
                TextView venuePhoneNo = (TextView) mContext.findViewById(R.id.venue_phone_no);
                RelativeLayout socialMedia = (RelativeLayout) mContext.findViewById(R.id.social_media_container);
                ImageView map = (ImageView) mContext.findViewById(R.id.venue_maps);

                // Calculate screen width and image accordingly
                int xy = Utilities.getScreenWidth(mContext) / 4;
                Bitmap scaledImage = Utilities.scaleDown(mVenue.getImage(0), xy, xy);


                if (mChildEvents.isEmpty()) { // Display empty UI view
                    ImageView noEventsImage = (ImageView) findViewById(R.id.no_venue_events_image);
                    TextView noEventsMessage = (TextView) findViewById(R.id.no_venue_events_message);
                    container.setVisibility(View.INVISIBLE);
                    noEventsImage.setVisibility(View.VISIBLE);
                    noEventsMessage.setVisibility(View.VISIBLE);
                } else { // Set adapter for list
                    ListView childEventsListView = (ListView) mContext.findViewById(R.id.venue_event_list);
                    childEventsListView.setAdapter(new VenueActAdapter(mContext, mChildEvents));
                    childEventsListView.setOnItemClickListener(VenueActivity.this);
                    container.setVisibility(View.VISIBLE);
                }

                venueImage.setImageBitmap(scaledImage);
                venueTitle.setText(mVenue.getName());
                venueDesc.setText(mVenue.getDescription());
                venueCapacity.setText(getString(R.string.capacity) + " " + Integer.toString(mVenue.getSeatingCapacity() + mVenue.getStandingCapacity()));
                venueEmail.setText(mVenue.getEmail());
                venuePhoneNo.setText(mVenue.getPhoneNumber());
                map.setVisibility(View.VISIBLE);

                ImageView facebook = (ImageView) mContext.findViewById(R.id.facebook);
                ImageView twitter = (ImageView) mContext.findViewById(R.id.twitter);
                ImageView instagram = (ImageView) mContext.findViewById(R.id.instagram);

                // Hide social media if Venue does not have one
                if (mVenue.getFacebook() == null) {
                    facebook.setEnabled(false);
                    facebook.setAlpha(128);
                }

                if (mVenue.getTwitter() == null) {
                    twitter.setEnabled(false);
                    twitter.setAlpha(128);
                }

                if (mVenue.getInstagram() == null) {
                    instagram.setEnabled(false);
                    instagram.setAlpha(128);
                }

                socialMedia.setVisibility(View.VISIBLE);
            } else
                Toast.makeText(mContext, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();

        }

        /*
        * Callback called if this task is cancelled
        * Set task to null and update progress spinner
        */
        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "artist activity thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(false);
        }
    }
}
