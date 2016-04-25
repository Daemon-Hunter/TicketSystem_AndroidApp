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
    public static IArtist mArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        getArtist();
        setUpToolbar();
        addOnClickListeners();
    }

    private void getArtist() {
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
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText(R.string.artist);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void displayInfo() {

        ImageView artistImage = (ImageView) findViewById(R.id.artist_image);
        TextView artistName = (TextView) findViewById(R.id.artist_name);
        TextView artistDescription = (TextView) findViewById(R.id.artist_description);

        int xy = ImageUtils.getScreenWidth(this) / 3;
        Bitmap scaledImage = ImageUtils.scaleDown(mArtist.getImage(0), xy, xy);

        artistName.setText(mArtist.getName());
        artistImage.setImageBitmap(scaledImage);
        artistDescription.setText(mArtist.getDescription());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    private class ReadArtist extends AsyncTask<Void, Void, IArtist> {

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
        protected IArtist doInBackground(Void... voids) {
            mArtist = UserWrapper.getInstance().getArtist(getIntent().getExtras().getInt(ARTIST_ID));
            return mArtist;
        }

        @Override
        protected void onPostExecute(IArtist artist) {

            ListView artistEventsListView = (ListView) mContext.findViewById(R.id
                    .artist_child_event_list);
            List<IChildEvent> artistEventsList = null;

            try {
                artistEventsList = artist.getChildEvents();
            } catch (IOException e) {

            }

            artistEventsListView.setAdapter(new ArtistActAdapter(mContext, artistEventsList));
            displayInfo();
        }
    }

}
