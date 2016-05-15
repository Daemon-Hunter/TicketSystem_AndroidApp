package com.example.aneurinc.prcs_app.UI.fragments;

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
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.activities.ParentEventActivity;
import com.example.aneurinc.prcs_app.UI.custom_adapters.ParentEventFragAdapter;
import com.example.aneurinc.prcs_app.UI.custom_listeners.OnSwipeTouchListener;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ParentEventFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, SearchView.OnQueryTextListener, View.OnAttachStateChangeListener {

    // list of parent events
    private List<IParentEvent> mParentEvents;

    // async threads
    private ReadParentEvents mReadTask;
    private LoadMoreParentEvents mLoadMoreTask;
    private SearchParentEvents mSearchTask;

    private MainActivity mMainActivity;

    // UI references
    private ProgressBar mReadProgressBar, mLoadProgressBar;
    private GridView mGridView;
    private SearchView mSearchView;

    // Last visible item in the grid view
    private int mLastFirstVisibleItem;

    /*
    * Initialise fragment view
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_event, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.current_events);

        setHasOptionsMenu(true);

        mParentEvents = new ArrayList<>();

        mGridView = (GridView) view.findViewById(R.id.event_grid_view);
        mGridView.setAdapter(new ParentEventFragAdapter(getActivity(), mParentEvents));
        mGridView.setOnItemClickListener(ParentEventFragment.this);
        mGridView.setOnScrollListener(ParentEventFragment.this);

        setSwipe(mGridView);

        if (getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        }

        setSwipe(view);

        mReadProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.read_progress);
        mLoadProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.load_more_progress);

        readParentEvents();

        return view;
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
    * Call read Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void readParentEvents() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(mReadProgressBar, true);
            mReadTask = new ReadParentEvents();
            mReadTask.execute();
        }
    }

    /*
    * Call load Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void loadMoreParentEvents() {
        if (!isTaskRunning(mLoadMoreTask)) {
            showProgress(mLoadProgressBar, true);
            mLoadMoreTask = new LoadMoreParentEvents();
            mLoadMoreTask.execute();
        }
    }

    /*
    * Call search Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void searchParentEvents(String query) {
        if (!isTaskRunning(mSearchTask)) {
            mSearchTask = new SearchParentEvents(query);
            mSearchTask.execute();
        }
    }

    /*
    * Set swipe listener for view
    */
    private void setSwipe(View v) {
        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                mMainActivity.switchFragment(new ArtistFragment(), FragmentType.ARTIST);
            }
        });
    }

    /*
    * Handle Child Event list adapter clicks
    * Get the row index and pass ID of corresponding object through to Child
    * Event activity
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), ParentEventActivity.class);
        i.putExtra(ParentEventActivity.PARENT_EVENT_ID, mParentEvents.get(position).getID());
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
        if (isTaskRunning(mLoadMoreTask)) {
            showProgress(mLoadProgressBar, false);
            mLoadMoreTask.cancel(true);
        }
        if (isTaskRunning(mSearchTask)) {
            mSearchTask.cancel(true);
        }
    }

    /*
    * Check if passed in thread is in the Running state
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

        if (firstVisibleItem > mLastFirstVisibleItem) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount) loadMoreParentEvents();
        }

        mLastFirstVisibleItem = firstVisibleItem;

    }

    /*
    * Called when search query is submitted via button click
    * Starts search artist thread with query
    */
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchParentEvents(query);
        return false;
    }

    /*
    * Called when search query is submitted key press
    * Starts search artist thread with query
    */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            searchParentEvents(newText);
        }
        return false;
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
        readParentEvents();

    }

    /*
    * Clear and add new list to adapter
    * Update grid view
    */
    private void refreshAdapter() {
        ParentEventFragAdapter mAdapter = (ParentEventFragAdapter) mGridView.getAdapter();
        mAdapter.clear();
        mAdapter.addAll(mParentEvents);
        mAdapter.notifyDataSetChanged();
    }

    /*
    * Async task class to handle searching Parent Events from database
    */
    private class SearchParentEvents extends AsyncTask<Void, Void, Boolean> {

        private String mQuery;

        /*
        * Pass in search query
        */
        public SearchParentEvents(String query) {
            mQuery = query;
        }

        /*
        * Thread task executed here
        * Parent Events list is replaced with Parent Events that match the query
        * Returns true if successful database request
        */
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mParentEvents = UserWrapper.getInstance().searchParentEvents(mQuery);
            } catch (IOException e) {
                return false;
            }

            return true;
        }

        /*
        * Callback fired once task is complete
        * Updates grid view with new Parent Events
        */
        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            mSearchTask = null;
            if (success) refreshAdapter();
            else
                Toast.makeText(getActivity(), getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
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

    /*
    * Async task class to handle reading Parent Events from database
    */
    private class ReadParentEvents extends AsyncTask<Void, Void, Boolean> {

        /*
        * Thread task is executed here
        * Parent Event list is populated with values from the database
        * Returns true if read is successful
        */
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(MainActivity.DEBUG_TAG, "event fragment thread started");
            try {
                UserWrapper.getInstance().setAmountToLoad(18);
                mParentEvents = UserWrapper.getInstance().getParentEvents();
            } catch (IOException e) {
                return false;
            }

            return true;
        }

        /*
       * Callback fired once task is finished
       * Accepts a boolean to indicate success outcome of task
       * Update UI accordingly by refreshing the grid adapter
       */
        @Override
        protected void onPostExecute(Boolean success) {

            Log.d(MainActivity.DEBUG_TAG, "event fragment thread completed");

            mReadTask = null;

            showProgress(mReadProgressBar, false);

            if (success) {
                if (isAdded()) {
                    if (!mParentEvents.isEmpty()) {
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
            Log.d(MainActivity.DEBUG_TAG, "event fragment thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(mReadProgressBar, false);
        }
    }

    /*
    * Async task class to handle loading more Parent Events from database
    */
    private class LoadMoreParentEvents extends AsyncTask<Void, Void, Boolean> {

        /*
        * Thread task execution happens here
        * Gets all a set amount of parent events from the database and
        * adds it to the list of current parent events
        * Returns a boolean to indicate whether it was successful or not
        */
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                mParentEvents.addAll(UserWrapper.getInstance().loadMoreParentEvents());
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
            else Toast.makeText(getActivity(), getString(R.string.network_problem), Toast
                    .LENGTH_SHORT).show();
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
}
