package com.example.aneurinc.prcs_app.UI.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.ChildEventActAdapter;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

public class ChildEventActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnClickListener {

    public static String EVENT_ID;
    private IChildEvent mChildEvent;
    private List<IArtist> mArtists;
    private ReadChildEvent mReadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_event);

        setupToolbar();

        ImageView tickets = (ImageView) findViewById(R.id.buy_tickets);
        tickets.setOnClickListener(this);

        readChildEvent();

    }

    private void readChildEvent() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(true);
            mReadTask = new ReadChildEvent(this);
            mReadTask.execute();
        }
    }

    @Override
    public void onPause() {
        handleQuit();
        super.onPause();
    }

    @Override
    public void onStop() {
        handleQuit();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    private void handleQuit() {
        if (isTaskRunning(mReadTask)) {
            showProgress(false);
            mReadTask.cancel(true);
        }
    }

    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ArtistActivity.class);
        int[] data = new int[1];
        data[0] = mArtists.get(position).getID();
        intent.putExtra(ArtistActivity.ARTIST_ID, data);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {
            case R.id.buy_tickets:
                Intent i = new Intent(this, TicketActivity.class);
                int[] IDs = new int[2];
                IDs[0] = getIntent().getExtras().getIntArray(EVENT_ID)[0];
                IDs[1] = getIntent().getExtras().getIntArray(EVENT_ID)[1];
                i.putExtra(ChildEventActivity.EVENT_ID, IDs);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    private void showProgress(final boolean show) {
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.read_progress);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private class ReadChildEvent extends AsyncTask<Void, Void, Void> {

        private final Activity mContext;

        public ReadChildEvent(Activity context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(MainActivity.DEBUG_TAG, "child event activity thread started");
            try {
                int childID = getIntent().getExtras().getIntArray(EVENT_ID)[0];
                int parentID = getIntent().getExtras().getIntArray(EVENT_ID)[1];
                mChildEvent = UserWrapper.getInstance().getParentEvent(parentID).getChildEvent(childID);
                mArtists = mChildEvent.getArtistList();
            } catch (IOException e) {
                // TODO: 29/04/2016 handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.d(MainActivity.DEBUG_TAG, "child event activity thread completed");

            mReadTask = null;

            showProgress(false);
            TextView name = (TextView) mContext.findViewById(R.id.child_event_title);
            TextView date = (TextView) mContext.findViewById(R.id.child_event_date);
            TextView city = (TextView) mContext.findViewById(R.id.child_event_city);
            TextView desc = (TextView) mContext.findViewById(R.id.child_event_description);
            ImageView image = (ImageView) mContext.findViewById(R.id.child_event_venue_image);
            ImageView buyTickets = (ImageView) mContext.findViewById(R.id.buy_tickets);
            RelativeLayout container = (RelativeLayout) mContext.findViewById(R.id.artist_lineup_container);

            String startDate = mChildEvent.getStartDateTime().toString().substring(0, 10);
            String endDate = mChildEvent.getEndDateTime().toString().substring(0, 10);

            int xy = Utilities.getScreenWidth(mContext) / 4;
            Bitmap scaledImage = Utilities.scaleDown(mChildEvent.getImage(0), xy, xy);

            if (mArtists.isEmpty()) {
                ImageView noEventsImage = (ImageView) mContext.findViewById(R.id.no_artist_lineup_image);
                TextView noEventsMessage = (TextView) mContext.findViewById(R.id.no_artist_lineup_message);
                container.setVisibility(View.GONE);
                noEventsImage.setVisibility(View.VISIBLE);
                noEventsMessage.setVisibility(View.VISIBLE);
            } else {
                ListView mLineup = (ListView) mContext.findViewById(R.id.child_event_lineup_list);
                mLineup.setAdapter(new ChildEventActAdapter(mContext, mArtists));
                mLineup.setOnItemClickListener(ChildEventActivity.this);
                container.setVisibility(View.VISIBLE);
            }

            date.setText(Utilities.formatDateDuration(startDate, endDate));
            name.setText(mChildEvent.getName());
            city.setText(mChildEvent.getVenue().getCity());
            desc.setText(mChildEvent.getDescription());
            image.setImageBitmap(scaledImage);
            buyTickets.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "artist activity thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(false);
        }
    }
}
