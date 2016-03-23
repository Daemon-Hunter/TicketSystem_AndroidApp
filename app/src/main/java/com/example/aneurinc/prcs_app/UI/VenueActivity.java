package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.aneurinc.prcs_app.R;

public class VenueActivity extends AppCompatActivity {

    public static String VenueImageIndex;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        setUpToolbar();
        displayImage();
        setListAdapter();

    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarListener(toolbar);
    }

    private void setToolbarListener(Toolbar t) {
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void displayImage() {
        int imageIndex = getIntent().getExtras().getInt(VenueImageIndex);
        ImageView venueImage = (ImageView) findViewById(R.id.venue_image);
        venueImage.setImageResource(Constants.venueImages[imageIndex]);
    }

    private void setListAdapter() {
        UpcomingListAdapter adapter = new UpcomingListAdapter(this, Constants.eventName,
                Constants.dates, Constants.eventImages);

        list = (ListView) findViewById(R.id.upcoming_list);
        list.setAdapter(adapter);
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
                Log.d(MainActivity.DEBUG_TAG, "Action Bar: Search");
                break;

            case R.id.tb_home:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
