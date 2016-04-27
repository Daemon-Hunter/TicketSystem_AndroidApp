package com.example.aneurinc.prcs_app.UI.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.VenueActAdapter;
import com.example.aneurinc.prcs_app.UI.utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

public class VenueActivity extends AppCompatActivity implements OnClickListener {

    public static String VENUE_ID;
    private IVenue mVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);
        setupToolbar();
        getChildEvents();
        addOnClickListeners();
    }

    private void getChildEvents() {
        ReadChildEvents task = new ReadChildEvents(this);
        task.execute();
    }

    private void displayInfo() {

        ImageView venueImage = (ImageView) findViewById(R.id.venue_image);
        TextView venueTitle = (TextView) findViewById(R.id.venue_title);
        TextView venueDesc = (TextView) findViewById(R.id.venue_description);
        TextView venueCapacity = (TextView) findViewById(R.id.venue_capacity);
        TextView venueEmail = (TextView) findViewById(R.id.venue_email);
        TextView venuePhoneNo = (TextView) findViewById(R.id.venue_phone_no);

        int xy = ImageUtils.getScreenWidth(this) / 3;
        Bitmap scaledImage = ImageUtils.scaleDown(mVenue.getImage(0), xy, xy);

        venueImage.setImageBitmap(scaledImage);
        venueTitle.setText(mVenue.getName());
        venueDesc.setText(mVenue.getDescription());
        venueCapacity.setText(getString(R.string.capacity) + " " + Integer.toString(mVenue
                .getStandingCapacity() + mVenue.getStandingCapacity()));
        venueEmail.setText(mVenue.getEmail());
        venuePhoneNo.setText(mVenue.getPhoneNumber());
    }

    private void addOnClickListeners() {

        ImageView facebook = (ImageView) findViewById(R.id.facebook);
        ImageView twitter = (ImageView) findViewById(R.id.twitter);
        ImageView instagram = (ImageView) findViewById(R.id.instagram);
        ImageView map = (ImageView) findViewById(R.id.venue_map);

        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        instagram.setOnClickListener(this);
        map.setOnClickListener(this);

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

            case R.id.tb_home:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {
            case R.id.facebook:
                break;
            case R.id.twitter:
                break;
            case R.id.instagram:
                break;
            case R.id.venue_map:
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra(MapActivity.VENUE_ID, mVenue.getID());
                startActivity(intent);
                break;
        }
    }

    private class ReadChildEvents extends AsyncTask<Void, Void, List<IChildEvent>> {

        private Activity mContext;

        public ReadChildEvents(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<IChildEvent> doInBackground(Void... params) {
            mVenue = UserWrapper.getInstance().getVenue(getIntent().getExtras().getInt(VENUE_ID));
            List<IChildEvent> mChildEvents = null;
            try {
                mChildEvents = mVenue.getChildEvents();
            } catch (IOException e) {
                // TODO: 26/04/2016 handle IOException 
                Log.d(MainActivity.DEBUG_TAG, "doInBackground: IOException");
            }
            return mChildEvents;
        }

        @Override
        protected void onPostExecute(List<IChildEvent> childEvents) {

            if (childEvents.isEmpty()) {
                ImageView noEventsImage = (ImageView) findViewById(R.id.no_upcoming_events_image);
                TextView noEventsMessage = (TextView) findViewById(R.id.upcoming_uk_events_message);
                LinearLayout eventsMessageContainer = (LinearLayout) findViewById(R.id
                        .upcoming_message_container);
                noEventsImage.setVisibility(View.VISIBLE);
                noEventsMessage.setText(getString(R.string.no_upcoming_events));
                eventsMessageContainer.setBackgroundColor(Color.TRANSPARENT);
            } else {
                ListView childEventsListView = (ListView) mContext.findViewById(R.id
                        .child_events_list);
                childEventsListView.setAdapter(new VenueActAdapter(mContext, childEvents));
            }

            displayInfo();

        }
    }
}
