package com.example.aneurinc.prcs_app.UI.Activities;

import android.content.Intent;
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

import com.example.aneurinc.prcs_app.Database.APIConnection;
import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.Datamodel.Artist;
import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ArtistActAdapter;

import static com.example.aneurinc.prcs_app.Database.MapToObject.ConvertArtist;

public class ArtistActivity extends AppCompatActivity implements View.OnClickListener {

    public static String EventImageIndex;
    public static Artist artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        executeAsyncTask();
        setUpToolbar();
        setAdapter();
        addOnClickListeners();
    }

    private void executeAsyncTask() {
        ReadArtist task = new ReadArtist();
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

        artistName.setText(artist.getArtistName());
        artistDescription.setText(artist.getDescription());
        artistImage.setImageBitmap(artist.getImage());

    }

    private void setAdapter() {
        ArtistActAdapter adapter = new ArtistActAdapter(this);
        ListView list = (ListView) findViewById(R.id.upcoming_list);
        list.setAdapter(adapter);
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

    private class ReadArtist extends AsyncTask<Void, Void, Artist> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO display something like display a progress bar
        }

        @Override
        protected Artist doInBackground(Void... voids) {

            APIConnection connection = new APIConnection(DatabaseTable.ARTIST);
            int index = getIntent().getExtras().getInt(EventImageIndex);

            artist =  ConvertArtist(connection.readSingle(index));


            return artist;
        }

        @Override
        protected void onPostExecute(Artist anArtist) {
            displayInfo();
        }
    }

}
