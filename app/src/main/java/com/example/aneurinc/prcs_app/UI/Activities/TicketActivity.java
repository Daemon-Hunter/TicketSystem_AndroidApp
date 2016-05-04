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
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.TicketActAdapter;
import com.example.aneurinc.prcs_app.UI.custom_views.CustomDialog;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

public class TicketActivity extends AppCompatActivity implements OnClickListener {

    public static String EVENT_ID;
    private List<ITicket> mTickets;
    private IChildEvent mChildEvent;
    private ReadTickets mReadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        setupToolbar();

        addOnClickListeners();

        // disable the checkout button
        ImageView checkout = (ImageView) findViewById(R.id.checkout);
        checkout.setClickable(false);

        readTickets();

    }

    private void readTickets() {
        if (!isTaskRunning(mReadTask)) {
            mReadTask = new ReadTickets(this);
            mReadTask.execute();
        }
    }

    @Override
    public void onPause() {
        handleQuit();
        super.onPause();
    }

    @Override
    public void onStop() {
        handleQuit();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    private void handleQuit() {
        if (isTaskRunning(mReadTask)) {
            mReadTask.cancel(true);
        }
    }

    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.tb_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
                startActivity(intent);
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

    private class ReadTickets extends AsyncTask<Void, Void, Void> {

        private Activity mContext;

        public ReadTickets(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO: 03/05/2016 add a spinner
        }

        @Override
        protected Void doInBackground(Void... params) {

            int childID = getIntent().getExtras().getIntArray(EVENT_ID)[0];
            int parentID = getIntent().getExtras().getIntArray(EVENT_ID)[1];

            try {
                mTickets = UserWrapper.getInstance().getParentEvent(parentID).getChildEvent(childID).getTickets();
                mChildEvent = UserWrapper.getInstance().getParentEvent(parentID).getChildEvent(childID);
            } catch (IOException e) {
                // TODO: 03/05/2016 handle
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            ImageView eventImage = (ImageView) mContext.findViewById(R.id.ticket_event_image);
            TextView eventName = (TextView) mContext.findViewById(R.id.ticket_event_title);
            TextView eventDate = (TextView) mContext.findViewById(R.id.ticket_event_date);
            TextView eventVenue = (TextView) mContext.findViewById(R.id.ticket_venue);
            ListView ticketTypeList = (ListView) mContext.findViewById(R.id.ticket_type_list);

            if (mTickets.isEmpty()) {
                ImageView soldOutImage = (ImageView) mContext.findViewById(R.id.sold_out_image);
                TextView soldOutMessage = (TextView) mContext.findViewById(R.id.sold_out_message);
                soldOutImage.setVisibility(View.VISIBLE);
                soldOutMessage.setVisibility(View.VISIBLE);
            } else {
                RelativeLayout ticketTypeContainer = (RelativeLayout) mContext.findViewById(R.id.ticket_types_container);
                TextView ticketTypeMessage = (TextView) mContext.findViewById(R.id.ticket_types_message);
                ticketTypeContainer.setVisibility(View.VISIBLE);
                ticketTypeMessage.setVisibility(View.VISIBLE);
                ticketTypeList.setAdapter(new TicketActAdapter(mContext, mTickets));
            }

            int xy = Utilities.getScreenWidth(mContext) / 4;
            Bitmap scaledImage = Utilities.scaleDown(mChildEvent.getImage(0), xy, xy);
            eventImage.setImageBitmap(scaledImage);

            String startDate = mChildEvent.getStartDateTime().toString().substring(0, 10);
            String endDate = mChildEvent.getEndDateTime().toString().substring(0, 10);

            eventDate.setText(Utilities.formatDateDuration(startDate, endDate));
            eventName.setText(mChildEvent.getName());
            eventVenue.setText(mChildEvent.getVenue().getName());
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
