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
import com.example.aneurinc.prcs_app.UI.activities.ArtistActivity;
import com.example.aneurinc.prcs_app.UI.activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.custom_adapters.ArtistFragAdapter;
import com.example.aneurinc.prcs_app.UI.custom_listeners.OnSwipeTouchListener;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, SearchView.OnQueryTextListener, View.OnAttachStateChangeListener {

    // UI references
    private GridView mGridView;
    private ProgressBar mReadProgressBar, mLoadProgressBar;
    private SearchView mSearchView;

    // list of all artists
    private List<IArtist> mArtists;

    // async threads
    private ReadArtists mReadTask;
    private SearchArtists mSearchTask;
    private LoadMoreArtists mLoadMoreTask;

    // Reference to parent activity
    private MainActivity mMainActivity;

    // last visible item in grid view
    private int mLastFirstVisibleItem;

    /*
    * Initialise artist fragment
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.current_artists);

        setHasOptionsMenu(true);

        mArtists = new ArrayList<>();

        mGridView = (GridView) view.findViewById(R.id.artist_grid_view);
        mGridView.setAdapter(new ArtistFragAdapter(getActivity(), mArtists));
        mGridView.setOnItemClickListener(ArtistFragment.this);
        mGridView.setOnScrollListener(ArtistFragment.this);

        setSwipe(mGridView);

        if (getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        }

        mReadProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.read_progress);
        mLoadProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.load_more_progress);

        readArtists();

        setSwipe(view);

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
    * Set swipe listener for view
    */
    public void setSwipe(View v) {
        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                mMainActivity.switchFragment(new VenueFragment(), FragmentType.VENUE);
            }

            @Override
            public void onSwipeRight() {
                mMainActivity.switchFragment(new ParentEventFragment(), FragmentType.PARENT_EVENT);
            }
        });
    }

    /*
    * Called when the fragment is stopped
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
    * Call read Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void readArtists() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(mReadProgressBar, true);
            mReadTask = new ReadArtists();
            mReadTask.execute();
        }
    }

    /*
    * Call search Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void searchArtist(String query) {
        if (!isTaskRunning(mSearchTask)) {
            mSearchTask = new SearchArtists(query);
            mSearchTask.execute();
        }
    }

    /*
    * Call load Async task if it is not already running
    * Show progress spinner to notify user of running task
    */
    private void loadMoreArtists() {
        if (!isTaskRunning(mLoadMoreTask)) {
            showProgress(mLoadProgressBar, true);
            mLoadMoreTask = new LoadMoreArtists();
            mLoadMoreTask.execute();
        }
    }

    /*
    * Handle Child Event list adapter clicks
    * Get the row index and pass ID of corresponding object through to Child
    * Event activity
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ArtistActivity.class);

        int[] data = new int[2];
        data[0] = mArtists.get(position).getID();
        data[1] = 1;

        intent.putExtra(ArtistActivity.ARTIST_ID, data);
        startActivity(intent);
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
            if (firstVisibleItem + visibleItemCount >= totalItemCount) loadMoreArtists();
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
        readArtists();
    }

    /*
    * Called when search query is submitted via button click
    * Starts search artist thread with query
    */
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchArtist(query);
        return false;
    }

    /*
    * Called when search query is submitted key press
    * Starts search artist thread with query
    */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            searchArtist(newText);
        }
        return false;
    }

    /*
    * Clear and add new list to adapter
    * Update grid view
    */
    private void refreshAdapter() {
        ArtistFragAdapter mAdapter = (ArtistFragAdapter) mGridView.getAdapter();
        mAdapter.clear();
        mAdapter.addAll(mArtists);
        mAdapter.notifyDataSetChanged();
    }

    /*
    * Async task class to handle reading artists from database
    */
    private class ReadArtists extends AsyncTask<Void, Void, Boolean> {

        /*
        * Thread task is executed here
        * Artist list is populated with values from the database
        * Returns true if read is successful
        */
        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.d(MainActivity.DEBUG_TAG, "artist fragment thread started");
            try {
                UserWrapper.getInstance().setAmountToLoad(18);
                mArtists = UserWrapper.getInstance().getArtists();
            } catch (IOException e) {
                e.printStackTrace();
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

            Log.d(MainActivity.DEBUG_TAG, "artist fragment thread completed");

            mReadTask = null;

            showProgress(mReadProgressBar, false);

            if (success) {
                if (isAdded()) {
                    if (!mArtists.isEmpty()) {
                        refreshAdapter();
                    }
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
            }

        }

        /*
        * Callback fired once task is cancelled
        * Update progress bar state
        */
        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "artist fragment thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(mReadProgressBar, false);
        }
    }

    /*
    * Async task to handle loading artists from database
    */
    private class LoadMoreArtists extends AsyncTask<Void, Void, Boolean> {

        /*
        * Thread task execution happens here
        * Gets all a set amount of artists from the database and
        * adds it to the list of current artists
        * Returns a boolean to indicate whether it was successful or not
        */
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mArtists.addAll(UserWrapper.getInstance().loadMoreArtists());
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
    * Async task that handles searching the database for artists fitting a criteria
    */
    private class SearchArtists extends AsyncTask<Void, Void, Boolean> {

        private String mQuery;

        /*
        * Pass in search query
        */
        public SearchArtists(String query) {
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
                mArtists = UserWrapper.getInstance().searchArtists(mQuery);
            } catch (IOException e) {
                e.printStackTrace();
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
