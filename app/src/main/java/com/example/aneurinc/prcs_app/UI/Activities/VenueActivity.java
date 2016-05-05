package com.example.aneurinc.prcs_app.UI.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    public static String VENUE_ID;
    private IVenue mVenue;
    private List<IChildEvent> mChildEvents;
    private List<IParentEvent> mParentEvents;

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

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.venues);
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
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.VENUE.toString());
        startActivity(intent);
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
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
                startActivity(intent);
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
            case R.id.venue_maps:
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra(MapActivity.VENUE_ID, mVenue.getID());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ChildEventActivity.class);

        int[] IDs = new int[2];
        IDs[0] = mChildEvents.get(position).getID();
        IDs[1] = mParentEvents.get(position).getID();

        intent.putExtra(ChildEventActivity.EVENT_ID, IDs);
        startActivity(intent);
    }

    private class ReadChildEvents extends AsyncTask<Void, Void, Void> {

        private Activity mContext;

        public ReadChildEvents(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mVenue = UserWrapper.getInstance().getVenue(getIntent().getExtras().getInt(VENUE_ID));
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: 03/05/2016 handle 
            }
            try {
                mChildEvents = mVenue.getChildEvents();
                mParentEvents = new ArrayList<>();
                for (IChildEvent c : mChildEvents) {
                    mParentEvents.add(c.getParentEvent());
                }
            } catch (IOException e) {
                // TODO: 26/04/2016 handle IOException
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            RelativeLayout container = (RelativeLayout) mContext.findViewById(R.id.upcoming_events_container);
            ImageView venueImage = (ImageView) mContext.findViewById(R.id.venue_image);
            TextView venueTitle = (TextView) mContext.findViewById(R.id.venue_title);
            TextView venueDesc = (TextView) mContext.findViewById(R.id.venue_description);
            TextView venueCapacity = (TextView) mContext.findViewById(R.id.venue_capacity);
            TextView venueEmail = (TextView) mContext.findViewById(R.id.venue_email);
            TextView venuePhoneNo = (TextView) mContext.findViewById(R.id.venue_phone_no);

            int xy = Utilities.getScreenWidth(mContext) / 4;
            Bitmap scaledImage = Utilities.scaleDown(mVenue.getImage(0), xy, xy);


            if (mChildEvents.isEmpty()) {
                ImageView noEventsImage = (ImageView) findViewById(R.id.no_venue_events_image);
                TextView noEventsMessage = (TextView) findViewById(R.id.no_venue_events_message);
                container.setVisibility(View.INVISIBLE);
                noEventsImage.setVisibility(View.VISIBLE);
                noEventsMessage.setVisibility(View.VISIBLE);
            } else {
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
        }
    }
}
