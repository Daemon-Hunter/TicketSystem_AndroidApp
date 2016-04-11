package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
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
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    public static String EventImageIndex;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        setUpToolbar();

        displayImage();

        setOnClickListeners();

        setLineupListAdapter();

    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText(R.string.event);
    }

    private void setLineupListAdapter() {
        LineupListAdapter adapter = new LineupListAdapter(this);
        list = (ListView) findViewById(R.id.lineup_list);
        list.setAdapter(adapter);
    }

    private void setOnClickListeners() {

        ImageView tickets = (ImageView) findViewById(R.id.buy_tickets);
        tickets.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {

            case R.id.buy_tickets:
                int imageIndex = getIntent().getExtras().getInt(EventImageIndex);
                Intent i = new Intent(this, TicketActivity.class);
                i.putExtra(TicketActivity.EventImageIndex, imageIndex);
                startActivity(i);
                break;

        }
    }

    private void displayImage() {
        int imageIndex = getIntent().getExtras().getInt(EventImageIndex);
        ImageView eventImage = (ImageView) findViewById(R.id.event_image);
        eventImage.setImageResource(Constants.eventImages[imageIndex]);
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
