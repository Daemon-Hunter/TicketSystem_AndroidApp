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
import com.example.aneurinc.prcs_app.UI.custom_adapters.ChildEventActAdapter;
import com.example.aneurinc.prcs_app.UI.utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

public class ChildEventActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {

    public static String EVENT_ID;
    private IChildEvent mChildEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_event);

        int childID = getIntent().getExtras().getIntArray(EVENT_ID)[0];
        int parentID = getIntent().getExtras().getIntArray(EVENT_ID)[1];

        mChildEvent = UserWrapper.getInstance().getParentEvent(parentID).getChildEvent(childID);

        setupToolbar();

        addOnClickListeners();

        readChildEvent();

    }

    private void readChildEvent() {
        ReadChildEvent task = new ReadChildEvent(this);
        task.execute();
    }

    private void addOnClickListeners() {
        ImageView tickets = (ImageView) findViewById(R.id.buy_tickets);
        tickets.setOnClickListener(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.child_event);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, ArtistActivity.class));
    }

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {
            case R.id.buy_tickets:
//                int imageIndex = getIntent().getExtras().getInt(PARENT_EVENT_ID);
//                Intent i = new Intent(this, TicketActivity.class);
//                i.putExtra(TicketActivity.EventImageIndex, imageIndex);
//                startActivity(i);
                break;

        }
    }

    private class ReadChildEvent extends AsyncTask<Void, Void, List<IArtist>> {

        private final Activity mContext;

        public ReadChildEvent(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<IArtist> doInBackground(Void... params) {

            List<IArtist> mArtistLineup = null;

            try {
                mArtistLineup = mChildEvent.getArtistList();
            } catch (IOException e) {
                // TODO: 29/04/2016 handle exception
            }

            return mArtistLineup;
        }

        @Override
        protected void onPostExecute(List<IArtist> artists) {

            TextView name = (TextView) mContext.findViewById(R.id.child_event_title);
            TextView date = (TextView) mContext.findViewById(R.id.child_event_date);
            TextView city = (TextView) mContext.findViewById(R.id.child_event_city);
            TextView desc = (TextView) mContext.findViewById(R.id.child_event_description);
            ImageView image = (ImageView) mContext.findViewById(R.id.child_event_venue_image);
            RelativeLayout container = (RelativeLayout) mContext.findViewById(R.id.artist_lineup_container);

            String startDate = mChildEvent.getStartDateTime().toString();
            String endDate = mChildEvent.getEndDateTime().toString();

            int xy = ImageUtils.getScreenWidth(mContext) / 4;
            Bitmap scaledImage = ImageUtils.scaleDown(mChildEvent.getVenue().getImage(0), xy, xy);

            if (artists.isEmpty()) {
                ImageView noEventsImage = (ImageView) mContext.findViewById(R.id.no_artist_lineup_image);
                TextView noEventsMessage = (TextView) mContext.findViewById(R.id.no_artist_lineup_message);
                container.setVisibility(View.GONE);
                noEventsImage.setVisibility(View.VISIBLE);
                noEventsMessage.setVisibility(View.VISIBLE);
            } else {
                ListView mLineup = (ListView) mContext.findViewById(R.id.child_event_lineup_list);
                mLineup.setAdapter(new ChildEventActAdapter(mContext, artists));
                container.setVisibility(View.VISIBLE);
            }

            date.setText(startDate.substring(0, 10) + " - " + endDate.substring(0, 10));
            name.setText(mChildEvent.getName());
            city.setText(mChildEvent.getVenue().getCity());
            desc.setText(mChildEvent.getDescription());
            image.setImageBitmap(scaledImage);
        }
    }
}
