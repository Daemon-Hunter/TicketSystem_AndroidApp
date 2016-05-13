package com.example.aneurinc.prcs_app.UI.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.TicketActAdapter;
import com.example.aneurinc.prcs_app.UI.custom_views.CustomConfirmDialog;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TicketActivity extends AppCompatActivity implements OnClickListener {

    // Event ID that tickets are displayed for - get this from intent
    public static String EVENT_ID;

    // List of event tickets
    private List<ITicket> mTickets;

    // The Child Event object
    private IChildEvent mChildEvent;

    // Async task to read tickets of event
    private ReadTickets mReadTask;

    // Async task to make an booking
    private MakeBooking mBookingTask;

    // List of event bookings
    private List<IBooking> mBookings;

    // Reference to UI list view
    private ListView mListView;

    // This order
    private IOrder mOrder;

    // Confirmation of purchase tickets dialog
    private CustomConfirmDialog mDialog;


    /*
    * Initialise activity
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        createToolbar();

        ImageView checkout = (ImageView) findViewById(R.id.checkout);
        checkout.setOnClickListener(this);
        checkout.setClickable(false);

        mDialog = new CustomConfirmDialog(this);
        mDialog.create();
        Button confirm = (Button) mDialog.findViewById(R.id.confirm);
        Button cancel = (Button) mDialog.findViewById(R.id.cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        mTickets = new ArrayList<>();
        mBookings = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.ticket_type_list);
        TicketActAdapter mAdapter = new TicketActAdapter(this, mTickets);
        mListView.setAdapter(mAdapter);

        readTickets();
    }

    /*
    * Starts async task if it is not running
    * Shows progress bar to notify user of task
    */
    private void readTickets() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(true);
            mReadTask = new ReadTickets(this);
            mReadTask.execute();
        }
    }

    /*
    * Start async task of making an booking if it is not already running
    */
    private void makeBooking() {
        if (!isTaskRunning(mBookingTask)) {
            mBookingTask = new MakeBooking();
            mBookingTask.execute();
        }
    }

    /*
    * Called when the activity is paused
    * Quits any running background threads
    */
    @Override
    public void onPause() {
        handleQuit();
        super.onPause();
    }

    /*
    * Called when the activity is stopped
    * Quits any running background threads
    */
    @Override
    public void onStop() {
        handleQuit();
        super.onStop();
    }

    /*
    * Called when the activity is destroyed
    * Quits any running background threads
    */
    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    /*
    * Terminates any running tasks to prevent them from running
    * once activity is out of scope
    */
    private void handleQuit() {
        if (isTaskRunning(mReadTask)) {
            showProgress(false);
            mReadTask.cancel(true);
        }
        if (isTaskRunning(mBookingTask)) {
            mBookingTask.cancel(true);
        }
    }

    /*
    * Checks if thread is in Running State
    */
    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }


    /*
    * Creates toolbar and sets title and back navigation
    * */
    private void createToolbar() {
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

    /*
    * Handle back naviagtion
    * Add custom transition
    */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /*
    * Create and inflate the menu
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    /*
    * Handles menu item clicks
    */
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
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Gets ticket quantities for each row in the ticket list
    * by calling list adapter get view by position method
    */
    private List<Integer> getTicketQuantities() {
        TicketActAdapter mAdapter = (TicketActAdapter) mListView.getAdapter();
        final List<Integer> mQuantities = new ArrayList<>();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View rowView = mAdapter.getViewByPosition(i, mListView);
            TextView mTextView = (TextView) rowView.findViewById(R.id.ticket_qty);
            mQuantities.add(Integer.parseInt(mTextView.getText().toString()));
        }
        return mQuantities;
    }

    /*
    * Handle onClick of views
    */
    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {

            // show dialog
            case R.id.checkout:
                mDialog.show();
                break;

            // hide dialog
            case R.id.confirm:
                mDialog.dismiss();
                makeBooking();
                break;

            // hide dialog
            case R.id.cancel:
                mDialog.dismiss();

            default:
                break;

        }
    }

    /*
    * Show progress spinner to notify user of task status
    */
    private void showProgress(final boolean show) {
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.read_progress);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /*
    * Update ticket list view using adapter
    */
    private void refreshAdapter() {
        TicketActAdapter mAdapter = (TicketActAdapter) mListView.getAdapter();
        mAdapter.clear();
        mAdapter.addAll(mTickets);
        mAdapter.notifyDataSetChanged();
    }

    /*
    * Async task class to handle making an order
    */
    private class MakeBooking extends AsyncTask<Void, Void, Boolean> {

        /*
        * Task is executed here
        * Get tickets and their quantities
        * Return true if booking was successful
        */
        @Override
        protected Boolean doInBackground(Void... params) {
            List<Integer> ticketQuantities = getTicketQuantities();
            List<ITicket> ticketCopy = new ArrayList<>();
            List<Integer> qtyCopy = new ArrayList<>();
            for (int i = 0; i < mTickets.size(); i++) {
                if (ticketQuantities.get(i) > 0) {
                    ticketCopy.add(mTickets.get(i));
                    qtyCopy.add(ticketQuantities.get(i));
                }
            }

            try {
                // make booking
                mOrder = UserWrapper.getInstance().makeCustomerBooking(ticketCopy, qtyCopy);
            } catch (IOException e) {
                return false;
            }

            return true;
        }

        /*
        * Callback fired when Order task is completed
        * Takes boolean to determine if booking was successful
        */
        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Intent intent = new Intent(TicketActivity.this, CheckoutActivity.class);
                intent.putExtra(CheckoutActivity.ORDER_ID, mOrder.getOrderID());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else
                Toast.makeText(TicketActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();

        }

        /*
        * Callback fired if thread is cancelled
        */
        @Override
        protected void onCancelled() {
            // cancel booking task
            mBookingTask = null;
        }
    }

    /*
    * Async task to handle reading tickets from the database
    */
    private class ReadTickets extends AsyncTask<Void, Void, Boolean> {

        private Activity mContext;

        /*
        * Pass in reference to outer class as a context
        */
        public ReadTickets(Activity context) {
            mContext = context;
        }

        /*
        * Thread is executed here
        * Get ticket list
        * and Child Event tickets are for
        * Return true if task was successful
        */
        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d(MainActivity.DEBUG_TAG, "ticket activity thread started");

            int childID = getIntent().getExtras().getIntArray(EVENT_ID)[0];
            int parentID = getIntent().getExtras().getIntArray(EVENT_ID)[1];

            try {
                mTickets = UserWrapper.getInstance().getParentEvent(parentID).getChildEvent(childID).getTickets();
                mChildEvent = UserWrapper.getInstance().getParentEvent(parentID).getChildEvent(childID);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        /*
        * Callback fired once task is completed
        * Takes parameter that determine if task was completed
        */
        @Override
        protected void onPostExecute(Boolean success) {

            Log.d(MainActivity.DEBUG_TAG, "ticket activity thread completed");
            mReadTask = null;

            showProgress(false);

            if (success) { // tickets successfully read

                ImageView eventImage = (ImageView) mContext.findViewById(R.id.ticket_event_image);
                TextView eventName = (TextView) mContext.findViewById(R.id.ticket_event_title);
                TextView eventDate = (TextView) mContext.findViewById(R.id.ticket_event_date);
                TextView eventVenue = (TextView) mContext.findViewById(R.id.ticket_venue);
                RelativeLayout totalContainer = (RelativeLayout) mContext.findViewById(R.id.total_container);

                if (mTickets.isEmpty()) { // Display empty UI view
                    ImageView soldOutImage = (ImageView) mContext.findViewById(R.id.sold_out_image);
                    TextView soldOutMessage = (TextView) mContext.findViewById(R.id.sold_out_message);
                    soldOutImage.setVisibility(View.VISIBLE);
                    soldOutMessage.setVisibility(View.VISIBLE);
                } else { // Set list adapter
                    RelativeLayout ticketTypeContainer = (RelativeLayout) mContext.findViewById(R.id.ticket_types_container);
                    TextView ticketTypeMessage = (TextView) mContext.findViewById(R.id.ticket_types_message);
                    ticketTypeContainer.setVisibility(View.VISIBLE);
                    ticketTypeMessage.setVisibility(View.VISIBLE);
                    refreshAdapter();
                }

                // Calculate screen width and image accordingly
                int xy = Utilities.getScreenWidth(mContext) / 4;
                Bitmap scaledImage = Utilities.scaleDown(mChildEvent.getImage(0), xy, xy);
                eventImage.setImageBitmap(scaledImage);

                // Remove time from DateTime object
                String startDate = mChildEvent.getStartDateTime().toString().substring(0, 10);
                String endDate = mChildEvent.getEndDateTime().toString().substring(0, 10);

                eventDate.setText(Utilities.formatDateDuration(startDate, endDate));
                eventName.setText(mChildEvent.getName());
                eventVenue.setText(mChildEvent.getVenue().getName());
                totalContainer.setVisibility(View.VISIBLE);

            } else
                Toast.makeText(mContext, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();

        }

        /*
        * Callback fired if thread is cancelled
        */
        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "ticket activity thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(false);
        }
    }
}
