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

    public static String ORDER_ID;
    private IOrder mOrder;
    private ReadOrder mReadTask;
    private List<ITicket> mTickets;
    private List<IBooking> mBookings;
    private ListView mListView;
    private ICustomer mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        setupToolbar();

        ImageView btnOk = (ImageView) findViewById(R.id.confirm_order);
        btnOk.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.invoice_list);

        readOrder();
    }

    private void readOrder() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(true);
            mReadTask = new ReadOrder(this);
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

    private void handleIOException() {
        Toast.makeText(this, "Ooops! There has been an internal error. Please try again.", Toast
                .LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    private void handleQuit() {
        if (isTaskRunning(mReadTask)) {
            showProgress(false);
            mReadTask.cancel(true);
        }
    }

    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_receipt);
        setSupportActionBar(toolbar);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

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

    private void showProgress(final boolean show) {
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.read_progress);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private class ReadOrder extends AsyncTask<Void, Void, Void> {

        private Activity mContext;

        public ReadOrder(Activity context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {

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
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.d(MainActivity.DEBUG_TAG, "receipt activity thread completed");

            showProgress(false);
            mReadTask = null;

            TextView custName = (TextView) mContext.findViewById(R.id.customer_name);
            TextView custEmail = (TextView) mContext.findViewById(R.id.customer_email);
            TextView orderID = (TextView) mContext.findViewById(R.id.booking_id);
            TextView totalCost = (TextView) findViewById(R.id.total_cost);
            TextView totalQty = (TextView) findViewById(R.id.total_qty);
            RelativeLayout customerContainer = (RelativeLayout) mContext.findViewById(R.id
                    .customer_details_container);
            RelativeLayout confirmationContainer = (RelativeLayout) mContext.findViewById(R.id
                    .confirmation_message_container);
            RelativeLayout bookingDetailsContainer = (RelativeLayout) mContext.findViewById(R.id
                    .booking_details_container);
            LinearLayout totalContainer = (LinearLayout) mContext.findViewById(R.id
                    .total_header);

            customerContainer.setVisibility(View.VISIBLE);
            bookingDetailsContainer.setVisibility(View.VISIBLE);
            confirmationContainer.setVisibility(View.VISIBLE);
            totalContainer.setVisibility(View.VISIBLE);

            custName.setText(String.format("%s %s", mCustomer.getFirstName(), mCustomer.getLastName()));
            custEmail.setText(mCustomer.getEmail());
            orderID.setText(mOrder.getOrderID().toString());

            double totalCostVal = 0.0;
            int totalQtyVal = 0;

            for (int i = 0; i < mTickets.size(); i++) {
                totalCostVal += mBookings.get(i).getQuantity() * mTickets.get(i).getPrice();
                totalQtyVal += mBookings.get(i).getQuantity();
            }

            totalCost.setText(Utilities.formatPrice(totalCostVal));
            totalQty.setText(Integer.toString(totalQtyVal));

            if (!mTickets.isEmpty()) {
                mListView.setAdapter(new ReceiptActAdapter(mContext, mTickets, mBookings));
            }

        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "receipt activity thread cancelled");
            super.onCancelled();
            showProgress(false);
            mReadTask = null;
        }
    }
}
