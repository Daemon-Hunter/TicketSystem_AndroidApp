package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

public class TicketActivity extends AppCompatActivity implements View.OnClickListener {

    public static String EventImageIndex;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        setUpToolbar();
        setListAdapter();
        displayImage();
        initOnClickListeners();

    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationOnClickListener(toolbar);
    }

    private void setNavigationOnClickListener(Toolbar t) {
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initOnClickListeners() {
        ImageView checkout = (ImageView) findViewById(R.id.checkout);
        checkout.setOnClickListener(this);
    }

    private void setListAdapter() {
        TicketListAdapter adapter = new TicketListAdapter(this, Constants.ticketType, Constants.ticketCost);
        list = (ListView) findViewById(R.id.ticket_list);
        list.setAdapter(adapter);
    }

    private void displayImage() {
        int imageIndex = getIntent().getExtras().getInt(EventImageIndex);
        ImageView eventImage = (ImageView) findViewById(R.id.event_image);
        eventImage.setImageResource(Constants.eventImages[imageIndex]);
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

            case R.id.ab_search:
                Log.d(MainActivity.DEBUG_TAG, "Action Bar: Search");
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {

            case R.id.checkout:
                int imageIndex = getIntent().getExtras().getInt(EventImageIndex);
                Intent i = new Intent(this, CheckoutActivity.class);
                i.putExtra(CheckoutActivity.EventImageIndex, imageIndex);
                startActivity(i);
                break;

            default:
                break;

        }
    }
}
