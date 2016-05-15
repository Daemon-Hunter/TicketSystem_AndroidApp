package com.example.aneurinc.prcs_app.UI.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.custom_adapters.TicketFragAdapter;
import com.example.aneurinc.prcs_app.UI.custom_listeners.OnSwipeTouchListener;
import com.google.jkellaway.androidapp_datamodel.bookings.CustomerBooking;
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
public class TicketFragment extends Fragment implements AbsListView.OnScrollListener {

    // Progress bars
    private ProgressBar mReadProgressBar, mLoadProgressbar;

    // Async tasks
    private ReadTickets mReadTask;
    private LoadMoreTickets mLoadTask;

    // List of tickets
    private ListView mListView;

    // Reference to parent activity
    private MainActivity mMainActivity;

    // Lists for ticket information
    private List<ITicket> mTickets;
    private List<IBooking> mBookings;
    private List<IChildEvent> mChildEvents;
    private List<IVenue> mVenues;
    private List<Integer> mOrderIDs;

    // Last visible item in the list
    private int mLastFirstVisibleItem;

    /*
    * Initialises fragment view with layout
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.my_tickets);

        mTickets = new ArrayList<>();
        mChildEvents = new ArrayList<>();
        mVenues = new ArrayList<>();
        mOrderIDs = new ArrayList<>();
        mBookings = new ArrayList<>();

        mListView = (ListView) view.findViewById(R.id.my_tickets_list);
        mListView.setOnScrollListener(this);

        setSwipe(mListView);

        if (getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        }

        setSwipe(view);

        mReadProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.ticket_progress);
        mLoadProgressbar = (ProgressBar) view.getRootView().findViewById(R.id.load_more_progress);

        readTickets();

        return view;
    }

    /*
    * Create and inflate toolbar menu
    */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

//        MenuItem item = menu.getItem(0);
//        mSearchView = (SearchView) item.getActionView();
//        mSearchView.setOnQueryTextListener(this);
//        mSearchView.addOnAttachStateChangeListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /*
    * Handled by parent activity
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    /*
    * Set swipe listener for view
    */
    private void setSwipe(View v) {
        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                mMainActivity.switchFragment(new VenueFragment(), FragmentType.VENUE);
            }
        });
    }

    /*
    * Call read Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void readTickets() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(mReadProgressBar, true);
            mReadTask = new ReadTickets(getActivity());
            mReadTask.execute();
        }
    }

    /*
    * Call load Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void loadMoreTickets() {
        if (!isTaskRunning(mLoadTask)) {
            showProgress(mLoadProgressbar, true);
            mLoadTask = new LoadMoreTickets();
            mLoadTask.execute();
        }
    }

    /*
    * Show progress spinner if show is true
    * Notifies the user of the progress of the Async task
    */
    private void showProgress(final ProgressBar progressBar, final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /*
    * Called when the fragment is paused
    * Cancel all running threads
    */
    @Override
    public void onPause() {
        handleQuit();
        super.onPause();
    }

    /*
    * Called when the fragment is stopped
    * Cancel all running threads
    */
    @Override
    public void onStop() {
        handleQuit();
        super.onStop();
    }

    /*
    * Called when the fragment is destroyed
    * Cancel all running threads
    */
    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    /*
    * Called when fragment is paused, stopped or destroyed
    * Checks if reading thread is running and cancels it if necessary
    */
    private void handleQuit() {
        if (isTaskRunning(mReadTask)) {
            showProgress(mReadProgressBar, false);
            mReadTask.cancel(true);
        }
        if (isTaskRunning(mLoadTask)) {
            showProgress(mLoadProgressbar, false);
            mLoadTask.cancel(true);
        }
    }

    /*
    * Check if passed in thread is in the Running state
    */
    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    /*
    * Set new adapter for ticket fragment
    */
    private void refreshAdapter() {
        mListView.setAdapter(new TicketFragAdapter(getActivity(), mTickets, mBookings, mOrderIDs, mChildEvents, mVenues));
    }

    /*
    * Called when scroll state is changed
    */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /*
    * Checks if list is at the bottom and fires load more async task if it is
    */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mLastFirstVisibleItem < firstVisibleItem) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount) loadMoreTickets();
        }

        mLastFirstVisibleItem = firstVisibleItem;
    }

    /*
    * Load more ticket async task loads a set amount from the database and returns it in a list
    * which is added to list of user tickets list view
    */
    private class LoadMoreTickets extends AsyncTask<Void, Void, Boolean> {

        /*
        * Thread search task is executed here
        * A list of the new bookings are retrieved from the database
        * and iterated through
        * Bookings, Tickets, OrderIDs and Child Events lists are updated with the
        * new information from the new bookings
        * Thread is check for cancel value after each request to ensure thread quits
        * quickly after it is cancelled
        * Finally, a boolean is return to indicate whether or nor database request
        * was successful*/
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                List<IBooking> newBookings = (((ICustomer) UserWrapper.getInstance().getUser()).loadMoreBookings());
                for (IBooking booking : newBookings) {
                    if (isCancelled()) break;
                    mBookings.add(booking);
                    if (isCancelled()) break;
                    mTickets.add(booking.getTicket());
                    if (isCancelled()) break;
                    mOrderIDs.add(((CustomerBooking) booking).getOrderID());
                    if (isCancelled()) break;
                    mChildEvents.add(booking.getTicket().getChildEvent());
                    if (isCancelled()) break;
                    mVenues.add(booking.getTicket().getChildEvent().getVenue());
                }
                Thread.sleep(300);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (InterruptedException e){
                e.printStackTrace();
                return false;
            }
            return true;
        }

        /*
        * Callback fired once async task is completed
        * Boolean success determines if async task finished properly or not
        * UI is updated accordingly
        */
        @Override
        protected void onPostExecute(Boolean success) {
            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: finished ticket load more thread");
            super.onPostExecute(success);
            mLoadTask = null;
            showProgress(mLoadProgressbar, false);
            if (success) refreshAdapter();
            else
                Toast.makeText(getActivity(), getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
        }

        /*
        * Callback fired if thread is cancelled
        * Progress bar is closed
        */
        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: cancelled ticket load more thread");
            super.onCancelled();
            showProgress(mLoadProgressbar, false);
            mLoadTask = null;
        }
    }

    /*
    * Async task class to handle reading Parent Events from database
    */
    private class ReadTickets extends AsyncTask<Void, Void, Boolean> {

        private Activity mContext;

        /*
        * Pass in a reference of parent activity
        */
        public ReadTickets(Activity context) {
            mContext = context;
        }

        /*
        * Thread is executed here
        * All bookings for the logged in customer are returned
        * and iterated through. Tickets, Venues, Child Events and OrderIDs are updated
        * with the necessary information
        * The thread state is checked after each request to ensure it stops quickly after
        * being cancelled. A boolean is returned to indicate whether the database request was
        * successful
        */
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(MainActivity.DEBUG_TAG, "ticket fragment thread started");
            try {
                ICustomer customer = (ICustomer) UserWrapper.getInstance().getUser();
                mBookings = customer.getBookings();
                for (IBooking booking : mBookings) {
                    if (isCancelled()) break;
                    mTickets.add(booking.getTicket());
                    if (isCancelled()) break;
                    mOrderIDs.add(((CustomerBooking) booking).getOrderID());
                    if (isCancelled()) break;
                    mChildEvents.add(booking.getTicket().getChildEvent());
                    if (isCancelled()) break;
                    mVenues.add(booking.getTicket().getChildEvent().getVenue());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        /*
       * Callback fired once task is finished
       * Accepts a boolean to indicate success outcome of task
       * Update UI accordingly by refreshing the list adapter
       */
        @Override
        protected void onPostExecute(Boolean success) {

            Log.d(MainActivity.DEBUG_TAG, "ticket fragment thread completed");
            mReadTask = null;

            showProgress(mReadProgressBar, false);

            if (success) {
                if (mContext != null && isAdded()) {
                    if (!mBookings.isEmpty() && !mTickets.isEmpty() && !mOrderIDs.isEmpty() &&
                            !mChildEvents.isEmpty() && !mVenues.isEmpty()) refreshAdapter();
                }
            } else
                Toast.makeText(getActivity(), getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
        }

        /*
        * Callback fired once task is cancelled
        * Update progress bar state
        */
        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "ticket fragment thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(mReadProgressBar, false);
        }
    }
}