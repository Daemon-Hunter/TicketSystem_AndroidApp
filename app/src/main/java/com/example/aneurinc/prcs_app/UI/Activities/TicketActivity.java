package com.example.aneurinc.prcs_app.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.TicketActAdapter;
import com.example.aneurinc.prcs_app.UI.custom_views.CustomDialog;
import com.example.aneurinc.prcs_app.UI.utilities.Constants;

public class TicketActivity extends AppCompatActivity implements OnClickListener {

    public static String EventImageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        setupToolbar();
        setListAdapter();
        displayImage();
        addOnClickListeners();

        // disable the checkout button
        ImageView checkout = (ImageView) findViewById(R.id.checkout);
        checkout.setClickable(false);

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tickets);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addOnClickListeners() {
        ImageView checkout = (ImageView) findViewById(R.id.checkout);
        checkout.setOnClickListener(this);
    }

    private void setListAdapter() {
        TicketActAdapter adapter = new TicketActAdapter(this);
        ListView list = (ListView) findViewById(R.id.ticket_list);
        list.setAdapter(adapter);
    }

    private void displayImage() {
        int imageIndex = getIntent().getExtras().getInt(EventImageIndex);
        ImageView eventImage = (ImageView) findViewById(R.id.parent_event_image);
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
        menu.findItem(R.id.tb_search).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

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

            case R.id.checkout:

                new CustomDialog(this).show();

                break;

            default:
                break;

        }
    }
}
