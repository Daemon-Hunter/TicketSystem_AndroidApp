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
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.ArtistActAdapter;
import com.example.aneurinc.prcs_app.UI.utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;


public class ArtistActivity extends AppCompatActivity implements View.OnClickListener {

    public static String ARTIST_ID;
    private IArtist mArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        mArtist = UserWrapper.getInstance().getArtist(getIntent().getExtras().getInt(ARTIST_ID));
        readArtist();
        setUpToolbar();
        addOnClickListeners();
    }

    private void readArtist() {
        ReadArtist task = new ReadArtist(this);
        task.execute();
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
            case R.id.soundcloud:
                break;
            case R.id.spotify:
                break;
        }
    }

    private class ReadArtist extends AsyncTask<Void, Void, List<IChildEvent>> {

        private Activity mContext;

        public ReadArtist(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO display something like display a progress bar
        }

        @Override
        protected List<IChildEvent> doInBackground(Void... voids) {
            List<IChildEvent> mChildEvents = null;
            try {
                mChildEvents = mArtist.getChildEvents();
            } catch (IOException e) {
                // TODO: 29/04/2016 handle exception
            }


            return mChildEvents;
        }

        @Override
        protected void onPostExecute(List<IChildEvent> childEvents) {

            RelativeLayout container = (RelativeLayout) mContext.findViewById(R.id.upcoming_performances_container);
            ImageView artistImage = (ImageView) mContext.findViewById(R.id.artist_image);
            TextView artistName = (TextView) mContext.findViewById(R.id.artist_title);
            TextView artistDescription = (TextView) mContext.findViewById(R.id.artist_description);

            int xy = ImageUtils.getScreenWidth(mContext) / 4;
            Bitmap scaledImage = ImageUtils.scaleDown(mArtist.getImage(0), xy, xy);


            if (childEvents.isEmpty()) {
                ImageView noEventsImage = (ImageView) findViewById(R.id.no_performances_image);
                TextView noEventsMessage = (TextView) findViewById(R.id.no_performances_message);
                container.setVisibility(View.GONE);
                noEventsImage.setVisibility(View.VISIBLE);
                noEventsMessage.setVisibility(View.VISIBLE);
            } else {
                ListView artistEventsListView = (ListView) mContext.findViewById(R.id.artist_lineup_list);
                artistEventsListView.setAdapter(new ArtistActAdapter(mContext, childEvents));
                container.setVisibility(View.VISIBLE);
            }

            artistName.setText(mArtist.getName());
            artistImage.setImageBitmap(scaledImage);
            artistDescription.setText(mArtist.getDescription());
        }
    }

}
