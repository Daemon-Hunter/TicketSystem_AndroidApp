package com.example.aneurinc.prcs_app.UI;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayImage();
        setListAdapter();

    }

    private void displayImage() {
        int imageIndex = getIntent().getExtras().getInt(VenueImageIndex);
        ImageView venueImage = (ImageView) findViewById(R.id.venue_image);
        venueImage.setImageResource(Constants.venueImages[imageIndex]);
    }

    private void setListAdapter() {
        FeatureListAdapter adapter = new FeatureListAdapter(this, Constants.eventName,
                Constants.dates, Constants.eventImages);

        list = (ListView) findViewById(R.id.featured_list);
        list.setAdapter(adapter);
    }

}
