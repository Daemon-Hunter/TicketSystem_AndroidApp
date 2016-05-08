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

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.ArtistActAdapter;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ArtistActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static String ARTIST_ID;

    private IArtist mArtist;
    private List<IParentEvent> mParentEvents;
    private List<IChildEvent> mChildEvents;
    private ReadArtist mReadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        readArtist();
        setUpToolbar();
        addOnClickListeners();
    }

    private void readArtist() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(true);
            mReadTask = new ReadArtist(this);
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

    private void addOnClickListeners() {
        ImageView facebook = (ImageView) findViewById(R.id.facebook);
        ImageView twitter = (ImageView) findViewById(R.id.twitter);
        ImageView instagram = (ImageView) findViewById(R.id.instagram);
        ImageView soundcloud = (ImageView) findViewById(R.id.soundcloud);
        ImageView spotify = (ImageView) findViewById(R.id.spotify);

        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        instagram.setOnClickListener(this);
        soundcloud.setOnClickListener(this);
        spotify.setOnClickListener(this);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.artists);
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
    public void onBackPressed() {
        if (getIntent().getExtras().getIntArray(ARTIST_ID).length > 1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.ARTIST.toString());
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ChildEventActivity.class);

        int[] IDs = new int[2];
        IDs[0] = mChildEvents.get(position).getID();
        IDs[1] = mParentEvents.get(position).getID();

        intent.putExtra(ChildEventActivity.EVENT_ID, IDs);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

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

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {
            case R.id.facebook:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mArtist.getFacebook())));
                break;
            case R.id.twitter:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mArtist.getTwitter())));
                break;
            case R.id.instagram:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mArtist.getInstagram())));
                break;
            case R.id.soundcloud:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mArtist.getSoundcloud())));
                break;
            case R.id.spotify:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mArtist.getSpotify())));
                break;
        }
    }

    private void showProgress(final boolean show) {
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.read_progress);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private class ReadArtist extends AsyncTask<Void, Void, Void> {

        private Activity mContext;

        public ReadArtist(Activity context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(MainActivity.DEBUG_TAG, "artist activity thread started");
            try {
                int artistID = getIntent().getExtras().getIntArray(ARTIST_ID)[0];
                mArtist = UserWrapper.getInstance().getArtist(artistID);
                mChildEvents = mArtist.getChildEvents();
                mParentEvents = new ArrayList<>();
                for (IChildEvent c : mChildEvents) {
                    mParentEvents.add(c.getParentEvent());
                }
            } catch (IOException e) {
                // TODO: 29/04/2016 handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.d(MainActivity.DEBUG_TAG, "artist activity thread completed");

            mReadTask = null;

            showProgress(false);
            RelativeLayout container = (RelativeLayout) mContext.findViewById(R.id.upcoming_performances_container);
            ImageView artistImage = (ImageView) mContext.findViewById(R.id.artist_image);
            TextView artistName = (TextView) mContext.findViewById(R.id.artist_title);
            TextView artistDescription = (TextView) mContext.findViewById(R.id.artist_description);
            RelativeLayout socialMedia = (RelativeLayout) mContext.findViewById(R.id.social_media_container);

            int xy = Utilities.getScreenWidth(mContext) / 4;
            Bitmap scaledImage = Utilities.scaleDown(mArtist.getImage(0), xy, xy);


            if (mChildEvents.isEmpty()) {
                ImageView noEventsImage = (ImageView) findViewById(R.id.no_performances_image);
                TextView noEventsMessage = (TextView) findViewById(R.id.no_performances_message);
                container.setVisibility(View.GONE);
                noEventsImage.setVisibility(View.VISIBLE);
                noEventsMessage.setVisibility(View.VISIBLE);
            } else {
                ListView mListView = (ListView) findViewById(R.id.artist_lineup_list);
                mListView.setAdapter(new ArtistActAdapter(ArtistActivity.this, mChildEvents));
                mListView.setOnItemClickListener(ArtistActivity.this);
                container.setVisibility(View.VISIBLE);
            }

            artistName.setText(mArtist.getName());
            artistImage.setImageBitmap(scaledImage);
            artistDescription.setText(mArtist.getDescription());

            ImageView facebook = (ImageView) mContext.findViewById(R.id.facebook);
            ImageView twitter = (ImageView) mContext.findViewById(R.id.twitter);
            ImageView instagram = (ImageView) mContext.findViewById(R.id.instagram);
            ImageView spotify = (ImageView) mContext.findViewById(R.id.spotify);
            ImageView soundcloud = (ImageView) mContext.findViewById(R.id.soundcloud);

            if (mArtist.getFacebook() == null) {
                facebook.setEnabled(false);
                facebook.setAlpha(128);
            }

            if (mArtist.getTwitter() == null) {
                twitter.setEnabled(false);
                twitter.setAlpha(128);
            }

            if (mArtist.getInstagram() == null) {
                instagram.setEnabled(false);
                instagram.setAlpha(128);
            }

            if (mArtist.getSpotify() == null) {
                spotify.setEnabled(false);
                spotify.setAlpha(128);
            }

            if (mArtist.getSoundcloud() == null) {
                soundcloud.setEnabled(false);
                soundcloud.setAlpha(128);
            }

            socialMedia.setVisibility(View.VISIBLE);

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
