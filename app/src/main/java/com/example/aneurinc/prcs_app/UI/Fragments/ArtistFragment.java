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

    private MainActivity mMainActivity;

    private int mLastFirstVisibleItem;

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.getItem(0);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.addOnAttachStateChangeListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

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

    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    private void readArtists() {
        if (!isTaskRunning(mReadTask)) {
            showProgress(mReadProgressBar, true);
            mReadTask = new ReadArtists();
            mReadTask.execute();
        }
    }

    private void searchArtist(String query) {
        if (!isTaskRunning(mSearchTask)) {
            mSearchTask = new SearchArtists(query);
            mSearchTask.execute();
        }
    }

    private void loadMoreArtists() {
        if (!isTaskRunning(mLoadMoreTask)) {
            showProgress(mLoadProgressBar, true);
            mLoadMoreTask = new LoadMoreArtists();
            mLoadMoreTask.execute();
        }
    }

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

    private void showProgress(final ProgressBar progressBar, final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (mLastFirstVisibleItem < firstVisibleItem) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount) loadMoreArtists();
        }

        mLastFirstVisibleItem = firstVisibleItem;

    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        if (isTaskRunning(mSearchTask)) {
            mSearchTask.cancel(true);
        }
        readArtists();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchArtist(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            searchArtist(newText);
        }
        return false;
    }

    private void refreshAdapter() {
        ArtistFragAdapter mAdapter = (ArtistFragAdapter) mGridView.getAdapter();
        mAdapter.clear();
        mAdapter.addAll(mArtists);
        mAdapter.notifyDataSetChanged();
    }

    private class ReadArtists extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Log.d(MainActivity.DEBUG_TAG, "artist fragment thread started");
            try {
                UserWrapper.getInstance().setAmountToLoad(18);
                mArtists = UserWrapper.getInstance().getArtists();
            } catch (IOException e) {
                // TODO: 26/04/2016 handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.d(MainActivity.DEBUG_TAG, "artist fragment thread completed");

            mReadTask = null;

            showProgress(mReadProgressBar, false);

            if (isAdded()) {
                if (!mArtists.isEmpty()) {
                    refreshAdapter();
                }
            }

        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "artist fragment thread cancelled");
            super.onCancelled();
            mReadTask = null;
            showProgress(mReadProgressBar, false);
        }
    }

    private class LoadMoreArtists extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mArtists.addAll(UserWrapper.getInstance().loadMoreArtists());
                Thread.sleep(500);
            } catch (IOException e) {
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mLoadMoreTask = null;
            showProgress(mLoadProgressBar, false);
            refreshAdapter();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mLoadMoreTask = null;
            showProgress(mLoadProgressBar, false);
        }
    }

    private class SearchArtists extends AsyncTask<Void, Void, Void> {

        private String mQuery;

        public SearchArtists(String query) {
            mQuery = query;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mArtists = UserWrapper.getInstance().searchArtists(mQuery);
            } catch (IOException e) {
                // TODO: 28/04/2016 handle exception
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mSearchTask = null;
            refreshAdapter();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mSearchTask = null;
        }
    }
}
