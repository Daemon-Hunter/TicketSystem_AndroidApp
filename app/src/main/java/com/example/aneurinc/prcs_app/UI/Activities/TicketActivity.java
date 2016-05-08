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

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.TicketActAdapter;
import com.example.aneurinc.prcs_app.UI.custom_views.CustomDialog;
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

    public static String EVENT_ID;
    private List<ITicket> mTickets;
    private IChildEvent mChildEvent;
    private ReadTickets mReadTask;
    private MakeOrder mOrderTask;
    private List<IBooking> mBookings;
    private ListView mListView;
    private IOrder mOrder;
    private CustomDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        setupToolbar();

        addOnClickListeners();

        // disable the checkout button
        ImageView checkout = (ImageView) findViewById(R.id.checkout);
        checkout.setClickable(false);

        mDialog = new CustomDialog(this);
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

    private void readTickets() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(true);
            mReadTask = new ReadTickets(this);
            mReadTask.execute();
        }
    }

    private void makeOrder() {
        if (!isTaskRunning(mOrderTask)) {
            mOrderTask = new MakeOrder();
            mOrderTask.execute();
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
            showProgress(false);
            mReadTask.cancel(true);
        }
        if (isTaskRunning(mOrderTask)) {
            mOrderTask.cancel(true);
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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {

            case R.id.checkout:
                mDialog.show();
                break;

            case R.id.confirm:
                mDialog.dismiss();
                makeOrder();
                break;

            case R.id.cancel:
                mDialog.dismiss();

            default:
                break;

        }
    }

    private void showProgress(final boolean show) {
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.read_progress);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void refreshAdapter() {
        TicketActAdapter mAdapter = (TicketActAdapter) mListView.getAdapter();
        mAdapter.clear();
        mAdapter.addAll(mTickets);
        mAdapter.notifyDataSetChanged();
    }

    private class MakeOrder extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
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
                mOrder = UserWrapper.getInstance().makeCustomerBooking(ticketCopy, qtyCopy);
            } catch (IOException e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(TicketActivity.this, CheckoutActivity.class);
            intent.putExtra(CheckoutActivity.ORDER_ID, mOrder.getOrderID());
            startActivity(intent);
        }

        @Override
        protected void onCancelled() {
            mOrder = null;
        }
    }

    private class ReadTickets extends AsyncTask<Void, Void, Void> {

        private Activity mContext;

        public ReadTickets(Activity context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d(MainActivity.DEBUG_TAG, "ticket activity thread started");

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

            Log.d(MainActivity.DEBUG_TAG, "ticket activity thread completed");

            showProgress(false);
            mReadTask = null;

            ImageView eventImage = (ImageView) mContext.findViewById(R.id.ticket_event_image);
            TextView eventName = (TextView) mContext.findViewById(R.id.ticket_event_title);
            TextView eventDate = (TextView) mContext.findViewById(R.id.ticket_event_date);
            TextView eventVenue = (TextView) mContext.findViewById(R.id.ticket_venue);
            RelativeLayout totalContainer = (RelativeLayout) mContext.findViewById(R.id.total_container);

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
                refreshAdapter();
            }

            int xy = Utilities.getScreenWidth(mContext) / 4;
            Bitmap scaledImage = Utilities.scaleDown(mChildEvent.getImage(0), xy, xy);
            eventImage.setImageBitmap(scaledImage);

            String startDate = mChildEvent.getStartDateTime().toString().substring(0, 10);
            String endDate = mChildEvent.getEndDateTime().toString().substring(0, 10);

            eventDate.setText(Utilities.formatDateDuration(startDate, endDate));
            eventName.setText(mChildEvent.getName());
            eventVenue.setText(mChildEvent.getVenue().getName());
            totalContainer.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "ticket activity thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(false);
        }
    }
}
