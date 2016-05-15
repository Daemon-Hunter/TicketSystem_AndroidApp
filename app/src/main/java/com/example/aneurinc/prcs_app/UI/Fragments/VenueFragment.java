package com.example.aneurinc.prcs_app.UI.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.activities.VenueActivity;
import com.example.aneurinc.prcs_app.UI.custom_adapters.VenueFragAdapter;
import com.example.aneurinc.prcs_app.UI.custom_listeners.OnSwipeTouchListener;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aneurinc on 06/03/2016.
 */
public class VenueFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, View.OnAttachStateChangeListener, SearchView.OnQueryTextListener {

    // list of venues
    private List<IVenue> mVenues;

    // UI references
    private ProgressBar mReadProgressBar, mLoadProgressBar;
    private ListView mListView;
    private SearchView mSearchView;

    // async tasks
    private ReadVenues mReadTask;
    private LoadMoreVenues mLoadMoreTask;
    private SearchVenues mSearchTask;

    // reference to parent activity
    private MainActivity mMainActivity;

    // last visible first item in list view
    private int mLastFirstVisibleItem;

    /*
    * Initialise fragment component and load layout
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.current_venues);

        mVenues = new ArrayList<>();

        setHasOptionsMenu(true);

        mListView = (ListView) view.findViewById(R.id.venue_list);
        mListView.setAdapter(new VenueFragAdapter(getActivity(), mVenues));
        mListView.setOnItemClickListener(VenueFragment.this);
        mListView.setOnScrollListener(VenueFragment.this);

        setSwipe(mListView);

        if (getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        }

        setSwipe(view);

        mReadProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.read_progress);
        mLoadProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.load_more_progress);

        readVenues();

        return view;
    }

    /*
    * Set swipe listener for view
    */
    private void setSwipe(View v) {
        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                mMainActivity.switchFragment(new ArtistFragment(), FragmentType.ARTIST);
            }

            @Override
            public void onSwipeLeft() {
                mMainActivity.switchFragment(new TicketFragment(), FragmentType.TICKET);
            }
        });
    }

    /*
    * Call read Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void readVenues() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(mReadProgressBar, true);
            mReadTask = new ReadVenues(getActivity());
            mReadTask.execute();
        }
    }

    /*
    * Call load more Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void loadMoreVenues() {
        if (!isTaskRunning(mLoadMoreTask)) {
            showProgress(mLoadProgressBar, true);
            mLoadMoreTask = new LoadMoreVenues();
            mLoadMoreTask.execute();
        }
    }

    /*
    * Call search Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void searchVenues(String query) {
        if (!isTaskRunning(mSearchTask)) {
            mSearchTask = new SearchVenues(query);
            mSearchTask.execute();
        }
    }

    /*
    * Create and inflate toolbar menu
    */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.getItem(0);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.addOnAttachStateChangeListener(this);
    }

    /*
    * Handled by parent activity
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    /*
    * Handle list clicks
    * Get the row index and pass ID of corresponding object to
    * Venue activity
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Intent intent = new Intent(getActivity(), VenueActivity.class);
        intent.putExtra(VenueActivity.VENUE_ID, mVenues.get(position).getID());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    /*
    * Show progress spinner if show is true
    * Notifies the user of the progress of the Async task
    */
    private void showProgress(ProgressBar progressBar, final boolean show) {
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

        if (isTaskRunning(mLoadMoreTask)) {
            showProgress(mLoadProgressBar, false);
            mLoadMoreTask.cancel(true);
        }

        if (isTaskRunning(mSearchTask)) {
            mSearchTask.cancel(true);
        }
    }

    /*
    * Call read Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
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
            if (firstVisibleItem + visibleItemCount >= totalItemCount) loadMoreVenues();
        }

        mLastFirstVisibleItem = firstVisibleItem;
    }

    /*
    * Called when view is attached to window
    */
    @Override
    public void onViewAttachedToWindow(View v) {

    }

    /*
    * Called when view is detached from window
    * Cancel search task and re-populate grid view with all artists
    */
    @Override
    public void onViewDetachedFromWindow(View v) {
        if (isTaskRunning(mSearchTask)) {
            mSearchTask.cancel(true);
        }
        readVenues();
    }

    /*
    * Called when search query is submitted via button click
    * Starts search artist thread with query
    */
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchVenues(query);
        return false;
    }

    /*
    * Called when search query is submitted key press
    * Starts search artist thread with query
    */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            searchVenues(newText);
        }
        return false;
    }

    /*
    * Clear and add new list to adapter
    * Update grid view
    */
    private void refreshAdapter() {
        VenueFragAdapter mAdapter = (VenueFragAdapter) mListView.getAdapter();
        mAdapter.clear();
        mAdapter.addAll(mVenues);
        mAdapter.notifyDataSetChanged();
    }

    /*
    * Async task class to handle reading venues from database
    */
    private class ReadVenues extends AsyncTask<Void, Void, Boolean> {

        private Activity mContext;

        public ReadVenues(Activity mContext) {
            this.mContext = mContext;
        }

        /*
       * Thread task is executed here
       * Artist list is populated with values from the database
       * Returns true if read is successful
       */
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(MainActivity.DEBUG_TAG, "venue fragment thread started");
            try {
                UserWrapper.getInstance().setAmountToLoad(9);
                mVenues = UserWrapper.getInstance().getVenues();
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        /*
        * Callback fired once task thread is finished
        * Accepts a boolean to indicate success outcome of task
        * Update UI accordingly by refreshing the grid adapter
        */
        @Override
        protected void onPostExecute(Boolean success) {

            Log.d(MainActivity.DEBUG_TAG, "venue fragment thread completed");
            mReadTask = null;

            showProgress(mReadProgressBar, false);

            if (success) {
                if (mContext != null && isAdded()) {
                    if (!mVenues.isEmpty()) {
                        refreshAdapter();
                    }
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
            Log.d(MainActivity.DEBUG_TAG, "venue fragment thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(mReadProgressBar, false);
        }
    }

    /*
    * Async task to handle loading venues from database
    */
    private class LoadMoreVenues extends AsyncTask<Void, Void, Boolean> {

        /*
        * Thread task execution happens here
        * Gets all a set amount of artists from the database and
        * adds it to the list of current artists
        * Returns a boolean to indicate whether it was successful or not
        */
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                mVenues.addAll(UserWrapper.getInstance().loadMoreVenues());
                Thread.sleep(300);
            } catch (IOException e) {
                return false;
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        /*
       * Callback fired once task is complete
       * Accepts boolean to indicate outcome of task
       * Updates UI accordingly
       */
        @Override
        protected void onPostExecute(Boolean success) {
            mLoadMoreTask = null;
            showProgress(mLoadProgressBar, false);
            if (success) refreshAdapter();
            else
                Toast.makeText(getActivity(), getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
        }

        /*
        * Called if the task is cancelled
        * Cancel task and progress bar
        */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            mLoadMoreTask = null;
            showProgress(mLoadProgressBar, false);
        }
    }

    /*
    * Async task that handles searching the database for venues fitting a criteria
    */
    private class SearchVenues extends AsyncTask<Void, Void, Boolean> {

        private String mQuery;

        /*
       * Pass in search query
       */
        public SearchVenues(String query) {
            mQuery = query;
        }

        /*
        * Thread task executed here
        * Artist list is replaced with Artists that match the query
        * Returns true if successful database request
        */
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mVenues = UserWrapper.getInstance().searchVenues(mQuery);
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        /*
        * Callback fired once task is complete
        * Updates grid view with new artists
        */
        @Override
        protected void onPostExecute(Boolean success) {
            mSearchTask = null;
            if (success) refreshAdapter();
            else Toast.makeText(getActivity(), getString(R.string.network_problem), Toast
                    .LENGTH_SHORT).show();
        }

        /*
        * Callback fired if task is cancelled
        * Cancels search task
        */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            mSearchTask = null;
        }
    }
}
