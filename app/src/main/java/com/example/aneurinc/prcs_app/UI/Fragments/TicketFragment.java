package com.example.aneurinc.prcs_app.UI.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.custom_adapters.TicketFragAdapter;
import com.example.aneurinc.prcs_app.UI.custom_listeners.OnSwipeTouchListener;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aneurinc on 01/03/2016.
 */
public class TicketFragment extends Fragment {

    private ProgressBar mReadProgressBar;

    private ReadTickets mReadTask;
    private LoadMoreTickets mLoadTask;
    private SearchTickets mSearchTask;

    private ListView mListView;

    private MainActivity mMainActivity;

    private List<ITicket> mTickets;
    private List<IChildEvent> mChildEvents;
    private List<IVenue> mVenues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.my_tickets);

        mListView = (ListView) view.findViewById(R.id.my_tickets_list);
        setSwipe(mListView);

        mTickets = new ArrayList<>();
        mChildEvents = new ArrayList<>();
        mVenues = new ArrayList<>();

        if (getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        }

        setSwipe(view);

        mReadProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.ticket_progress);

        readTickets();

        return view;
    }

    private void setSwipe(View v) {
        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                mMainActivity.switchFragment(new VenueFragment(), FragmentType.VENUE);
            }
        });
    }

    private void readTickets() {
        if (!isTaskRunning(mReadTask)) {
            mReadTask = new ReadTickets(getActivity());
            mReadTask.execute();
        }
    }

    private void loadMoreTickets() {
        if (!isTaskRunning(mLoadTask)) {
            mLoadTask = new LoadMoreTickets(getActivity());
            mLoadTask.execute();
        }
    }

    private void searchTickets(String query) {
        if (!isTaskRunning(mSearchTask)) {
            mSearchTask = new SearchTickets(getActivity(), query);
            mSearchTask.execute();
        }
    }

    private void showProgress(final boolean show) {
        mReadProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
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
        if (isTaskRunning(mSearchTask)) {
            mSearchTask.cancel(true);
        }
        if (isTaskRunning(mLoadTask)) {
            mLoadTask.cancel(true);
        }
    }

    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    private void refreshAdapter() {
        mListView.setAdapter(new TicketFragAdapter(getActivity(), mTickets, mChildEvents, mVenues));
    }

    private class SearchTickets extends AsyncTask<Void, Void, Void> {

        private Activity mContext;
        private String mQuery;

        public SearchTickets(Activity context, String query) {
            mContext = context;
            mQuery = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class LoadMoreTickets extends AsyncTask<Void, Void, Void> {

        private Activity mContext;

        public LoadMoreTickets(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class ReadTickets extends AsyncTask<Void, Void, Void> {

        private Activity mContext;

        public ReadTickets(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ICustomer customer = (ICustomer) UserWrapper.getInstance().getUser();
                List<IBooking> bookings = customer.getBookings();
                for (IBooking booking : bookings) {
                    mTickets.add(booking.getTicket());
                    mChildEvents.add(booking.getTicket().getChildEvent());
                    mVenues.add(booking.getTicket().getChildEvent().getVenue());
                }
            } catch (IOException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            showProgress(false);

            if (mContext != null && isAdded()) {
                if (!mTickets.isEmpty()) {
                    refreshAdapter();
                }
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}
