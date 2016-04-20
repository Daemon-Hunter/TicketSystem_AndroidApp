package com.example.aneurinc.prcs_app.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ChildEventActAdapter;
import com.example.aneurinc.prcs_app.UI.Utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.datamodel.ChildEvent;
import com.google.jkellaway.androidapp_datamodel.datamodel.IChildEvent;

public class ChildEventActivity extends AppCompatActivity implements OnClickListener {

    public static String CHILD_EVENT_ID;
    private IChildEvent childEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_event);

        setupToolbar();

        addOnClickListeners();

        readChildEvent();

        addListAdapter();

    }

    private void readChildEvent() {
        ReadChildEvent task = new ReadChildEvent();
        task.execute();
    }

    private void addOnClickListeners() {
        ImageView tickets = (ImageView) findViewById(R.id.buy_tickets);
        tickets.setOnClickListener(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText(R.string.child_event);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addListAdapter() {
        ChildEventActAdapter adapter = new ChildEventActAdapter(this);
        ListView list = (ListView) findViewById(R.id.lineup_list);
        list.setAdapter(adapter);
    }

    private void displayChildEvent() {

        TextView name = (TextView) findViewById(R.id.child_event_name);
        TextView date = (TextView) findViewById(R.id.child_event_date);
        TextView address = (TextView) findViewById(R.id.child_event_address);
        TextView desc = (TextView) findViewById(R.id.child_event_description);
        ImageView image = (ImageView) findViewById(R.id.child_event_image);

        name.setText(childEvent.getChildEventName());
        date.setText(childEvent.getChildEventStartDateTime() + " - "
                + childEvent.getChildEventEndDateTime());
        address.setText((CharSequence) childEvent.getVenue());
        desc.setText(childEvent.getChildEventDescription());

        int width = ImageUtils.getScreenWidth(this) / 4;
        int height = ImageUtils.getScreenHeight(this) / 4;
        Bitmap scaledImage = ImageUtils.scaleDown(childEvent.getImage(0), width, height);
        image.setImageBitmap(scaledImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
            case R.id.buy_tickets:
                int imageIndex = getIntent().getExtras().getInt(CHILD_EVENT_ID);
                Intent i = new Intent(this, TicketActivity.class);
                i.putExtra(TicketActivity.EventImageIndex, imageIndex);
                startActivity(i);
                break;

        }
    }

    private class ReadChildEvent extends AsyncTask<Void, Void, IChildEvent> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(IChildEvent childEvent) {
            displayChildEvent();
        }

        @Override
        protected IChildEvent doInBackground(Void... params) {
            int index = getIntent().getExtras().getInt(CHILD_EVENT_ID);
            return new ChildEvent();
        }
    }
}
