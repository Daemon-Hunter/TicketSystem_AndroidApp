package com.example.aneurinc.prcs_app.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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

        // disable the checkout button
        ImageView checkout = (ImageView) findViewById(R.id.checkout);
        checkout.setClickable(false);

    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarListener(toolbar);
        toolbarTitle.setText(R.string.tickets);
    }

    private void setToolbarListener(Toolbar t) {
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
        TicketListAdapter adapter = new TicketListAdapter(this);
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
