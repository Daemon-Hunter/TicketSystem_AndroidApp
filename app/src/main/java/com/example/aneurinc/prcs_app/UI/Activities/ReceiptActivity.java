package com.example.aneurinc.prcs_app.UI.activities;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.ReceiptActAdapter;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptActivity extends AppCompatActivity implements OnClickListener {

    // Order ID form intent
    public static String ORDER_ID;

    // Actual Order
    private IOrder mOrder;

    // Async task to read Order from database
    private ReadOrder mReadTask;

    // List of Order Tickets
    private List<ITicket> mTickets;

    // List of Order Bookings
    private List<IBooking> mBookings;

    // Reference to list view
    private ListView mListView;

    // Logged in Customer
    private ICustomer mCustomer;


    /*
    * Initialise activity
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        createToolbar();

        ImageView btnOk = (ImageView) findViewById(R.id.confirm_order);
        btnOk.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.invoice_list);

        readOrder();
    }

    /*
    * Start async task if thread is not running
    * Show progress spinner to notify user
    */
    private void readOrder() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(true);
            mReadTask = new ReadOrder(this);
            mReadTask.execute();
        }
    }

    /*
    * Called when activity is paused
    * Cancel any running threads
    */
    @Override
    public void onPause() {
        handleQuit();
        super.onPause();
    }

    /*
    * Called when activity is stopped
    * Cancel any running threads
    */
    @Override
    public void onStop() {
        handleQuit();
        super.onStop();
    }

    /*
    * Called when activity is destroyed
    * Cancel any running threads
    */
    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    /*
    * Cancel any running threads to prevent them running when activity is out of scope
    */
    private void handleQuit() {
        if (isTaskRunning(mReadTask)) {
            showProgress(false);
            mReadTask.cancel(true);
        }
    }

    /*
    * Check if thread is in the Running state
    */
    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }


    /*
    * Set up the toolbar - set title
    */
    private void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_receipt);
        setSupportActionBar(toolbar);
    }

    /*
    * Handle onClicks event from views in this activity
    */
    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {

            case R.id.confirm_order:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            default:
                break;
        }
    }

    /*
    * Create menu toolbar
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    /*
    * Handle menu item clicks
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.tb_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Show progress spinner to notify the user a task is running
    */
    private void showProgress(final boolean show) {
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.read_progress);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /*
    * Async task class to read Order by its ID from the database
    * Get logged in user, Tickets and Bookings
    */
    private class ReadOrder extends AsyncTask<Void, Void, Boolean> {

        private Activity mContext;

        // Pass in a reference to the outer class context
        public ReadOrder(Activity context) {
            mContext = context;
        }

        /*
        * Execute thread task
        * Return true if completed
        */
        @Override
        protected Boolean doInBackground(Void... params) {

            Log.d(MainActivity.DEBUG_TAG, "receipt activity thread started");

            try {
                mCustomer = (ICustomer) UserWrapper.getInstance().getUser();
                mOrder = mCustomer.getOrder(getIntent().getExtras().getInt(ORDER_ID));
                mTickets = new ArrayList<>();
                mBookings = mOrder.getBookingList();

                for (IBooking booking : mBookings) {
                    mTickets.add(booking.getTicket());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        /*
        * Callback fired once task is completed
        */
        @Override
        protected void onPostExecute(Boolean success) {

            Log.d(MainActivity.DEBUG_TAG, "receipt activity thread completed");
            mReadTask = null;

            showProgress(false);

            if (success) { // Order was successfully read from the database

                TextView custName = (TextView) mContext.findViewById(R.id.customer_name);
                TextView custEmail = (TextView) mContext.findViewById(R.id.customer_email);
                TextView orderID = (TextView) mContext.findViewById(R.id.booking_id);
                TextView totalCost = (TextView) findViewById(R.id.total_cost);
                TextView totalQty = (TextView) findViewById(R.id.total_qty);
                RelativeLayout customerContainer = (RelativeLayout) mContext.findViewById(R.id.customer_details_container);
                RelativeLayout confirmationContainer = (RelativeLayout) mContext.findViewById(R.id.confirmation_message_container);
                RelativeLayout bookingDetailsContainer = (RelativeLayout) mContext.findViewById(R.id.booking_details_container);
                LinearLayout totalContainer = (LinearLayout) mContext.findViewById(R.id.total_header);

                customerContainer.setVisibility(View.VISIBLE);
                bookingDetailsContainer.setVisibility(View.VISIBLE);
                confirmationContainer.setVisibility(View.VISIBLE);
                totalContainer.setVisibility(View.VISIBLE);

                custName.setText(String.format("%s %s", mCustomer.getFirstName(), mCustomer.getLastName()));
                custEmail.setText(mCustomer.getEmail());
                orderID.setText(mOrder.getOrderID().toString());

                double totalCostVal = 0.0;
                int totalQtyVal = 0;

                // Calculate total quatity and cose of tickets
                for (int i = 0; i < mTickets.size(); i++) {
                    totalCostVal += mBookings.get(i).getQuantity() * mTickets.get(i).getPrice();
                    totalQtyVal += mBookings.get(i).getQuantity();
                }

                totalCost.setText(Utilities.formatPrice(totalCostVal));
                totalQty.setText(Integer.toString(totalQtyVal));

                if (!mTickets.isEmpty()) {
                    mListView.setAdapter(new ReceiptActAdapter(mContext, mTickets, mBookings));
                }
            } else
                Toast.makeText(mContext, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();

        }

        /*
        * Callback fired if task is cancelled
        */
        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "receipt activity thread cancelled");
            super.onCancelled();
            showProgress(false);
            mReadTask = null;
        }
    }
}
