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
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.ParentEventActAdapter;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

public class ParentEventActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    // ID of Parent Event passed in as intent
    public static String PARENT_EVENT_ID;

    // The current Parent Event
    private IParentEvent mParentEvent;

    // List of Child Events belonging to Parent Event
    private List<IChildEvent> mChildEvents;

    // Async task for reading Parent Event
    private ReadParentEvent mReadTask;

    /*
    * Initialise activity and set layout
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_event);

        createToolbar();

        readParentEvent();
    }

    /*
    * Check if async task is running
    * Start it if not running
    * Notify user with progress spinner
    */
    private void readParentEvent() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(true);
            mReadTask = new ReadParentEvent(this);
            mReadTask.execute();
        }
    }

    /*
    * Called when activity is paused
    * Cancel any running threads
    */
    @Override
    public void onPause() {
        handleQuit();
        super.onPause();
    }

    /*
    * Called when activity is stopped
    * Cancel any running threads
    */
    @Override
    public void onStop() {
        handleQuit();
        super.onStop();
    }

    /*
    * Called when activity is destroyed
    * Cancel any running threads
    */
    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    /*
    * Check if task is running
    * Cancel it if it is and hide progress spinner
    */
    private void handleQuit() {
        if (isTaskRunning(mReadTask)) {
            showProgress(false);
            mReadTask.cancel(true);
        }
    }

    /*
    * Check if thread is in Running State
    */
    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    /*
    * Set up the toolbar
    * Set title and back navigation listener
    */
    private void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.parent_event);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /*
    * Handle back navigation
    * Set transition animation
    */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /*
    * Create and inflate menu and its items
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
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Handle list view clicks
    * Get position of click and pass it into activity as an intent
    * */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, ChildEventActivity.class);

        // Child and Parent Event IDs
        int[] IDs = new int[2];
        IDs[0] = mChildEvents.get(position).getID();
        IDs[1] = mParentEvent.getID();

        i.putExtra(ChildEventActivity.EVENT_ID, IDs);

        startActivity(i);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    /*
    * Handle social media onClicks
    */
    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {
            case R.id.facebook:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mParentEvent.getFacebook())));
                break;
            case R.id.twitter:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mParentEvent.getTwitter())));
                break;
            case R.id.instagram:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mParentEvent.getInstagram())));
                break;
        }
    }

    /*
    * Show or hide the progress bar depending on the value of boolean show
    */
    private void showProgress(final boolean show) {
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.read_progress);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /*
    * Async task to handle reading Parent Event form the database
    */
    private class ReadParentEvent extends AsyncTask<Void, Void, Boolean> {

        private Activity mContext;

        /*
        * Get context from outer class
        */
        public ReadParentEvent(Activity context) {
            mContext = context;
        }

        /*
        * Thread task is executed here
        * Read Parent Event by its ID and get a list of Child Events from Parent
        * Return true if successful
        */
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(MainActivity.DEBUG_TAG, "parent event activity thread started");
            try {
                mParentEvent = UserWrapper.getInstance().getParentEvent(getIntent().getExtras().getInt(PARENT_EVENT_ID));
                mChildEvents = mParentEvent.getChildEvents();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        /*
        * Callback fired once task is complete
        * Accepts boolean that determine outcome of task
        */
        @Override
        protected void onPostExecute(Boolean success) {
            Log.d(MainActivity.DEBUG_TAG, "parent event activity thread completed");
            mReadTask = null;

            // Hide progress bar as task has finished
            showProgress(false);

            if (success) { // Update UI
                RelativeLayout container = (RelativeLayout) mContext.findViewById(R.id.featured_events_container);
                ImageView image = (ImageView) mContext.findViewById(R.id.ticket_event_image);
                TextView name = (TextView) mContext.findViewById(R.id.ticket_event_title);
                TextView desc = (TextView) mContext.findViewById(R.id.parent_event_description);
                RelativeLayout socialMedia = (RelativeLayout) mContext.findViewById(R.id.social_media_container);

                int xy = Utilities.getScreenWidth(mContext) / 4;
                Bitmap scaledImage = Utilities.scaleDown(mParentEvent.getImage(0), xy, xy);

                if (mChildEvents.isEmpty()) { // Show empty UI views
                    TextView noChildEventsMessage = (TextView) mContext.findViewById(R.id.no_child_events_message);
                    ImageView noChildEventsImage = (ImageView) mContext.findViewById(R.id.no_child_events_image);
                    container.setVisibility(View.GONE);
                    noChildEventsMessage.setVisibility(View.VISIBLE);
                    noChildEventsImage.setVisibility(View.VISIBLE);

                } else { // Set list adapter
                    ListView list = (ListView) mContext.findViewById(R.id.child_events_list);
                    list.setAdapter(new ParentEventActAdapter(mContext, mChildEvents));
                    list.setOnItemClickListener(ParentEventActivity.this);
                    container.setVisibility(View.VISIBLE);
                }

                image.setImageBitmap(scaledImage);
                name.setText(mParentEvent.getName());
                desc.setText(mParentEvent.getDescription());

                // Get reference to social media images
                ImageView facebook = (ImageView) mContext.findViewById(R.id.facebook);
                ImageView twitter = (ImageView) mContext.findViewById(R.id.twitter);
                ImageView instagram = (ImageView) mContext.findViewById(R.id.instagram);

                // Hide social media image if null
                if (mParentEvent.getFacebook() == null) {
                    facebook.setEnabled(false);
                    facebook.setAlpha(128);
                }

                if (mParentEvent.getTwitter() == null) {
                    twitter.setEnabled(false);
                    twitter.setAlpha(128);
                }

                if (mParentEvent.getInstagram() == null) {
                    instagram.setEnabled(false);
                    instagram.setAlpha(128);
                }

                socialMedia.setVisibility(View.VISIBLE);
            } else
                Toast.makeText(mContext, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
        }

        /*
        * Callback fired if task is cancelled
        */
        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "parent event activity thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(false);
        }
    }

}
