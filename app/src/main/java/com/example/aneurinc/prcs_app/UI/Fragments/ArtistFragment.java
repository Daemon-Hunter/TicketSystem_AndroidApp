package com.example.aneurinc.prcs_app.UI.fragments;

import android.animation.Animator;
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
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, Animator.AnimatorListener, SearchView.OnQueryTextListener, View.OnAttachStateChangeListener {

    // UI references
    private GridView mGridView;
    private ProgressBar mReadProgress, mLoadMoreProgress;
    private SearchView mSearchView;

    // list of all artists
    private List<IArtist> mArtists;

    // async threads
    private ReadArtists mReadTask;
    private SearchArtists mSearchTask;
    private LoadMoreArtists mLoadMoreTask;

    // progress bar animation duration
    private static final int ANIM_TIME = 100;

    private MainActivity mMainActivity;
    private boolean isScrolling, atBottom;

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

        mReadProgress = (ProgressBar) view.getRootView().findViewById(R.id.read_progress);
        mLoadMoreProgress = (ProgressBar) view.getRootView().findViewById(R.id.load_more_progress);

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
            mReadTask.cancel(true);
        }
        if (isTaskRunning(mLoadMoreTask)) {
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
        mReadTask = new ReadArtists();
        mReadTask.execute();
    }

    private void searchArtist(String query) {
        mSearchTask = new SearchArtists(query);
        mSearchTask.execute();
    }

    private void loadMoreArtists() {
        mLoadMoreTask = new LoadMoreArtists();
        mLoadMoreTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ArtistActivity.class);
        intent.putExtra(ArtistActivity.ARTIST_ID, mArtists.get(position).getID());
        getActivity().startActivity(intent);
    }

    private void showProgress(ProgressBar progressBar, final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(ANIM_TIME).alpha(show ? 1 : 0).setListener(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        Log.d(MainActivity.DEBUG_TAG, "onAnimationStart: started");
        if (atBottom) {
            mLoadMoreProgress.setVisibility(View.GONE);
        } else {
            mReadProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (atBottom) {
            mLoadMoreProgress.setVisibility(View.VISIBLE);
        } else {
            mReadProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isScrolling = true;
        } else {
            isScrolling = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        // if user is scrolling and list view is at bottom of screen, load more artists
        if (isScrolling) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                atBottom = true;
                if (!isTaskRunning(mLoadMoreTask)) {
                    loadMoreArtists();
                }
            } else {
                atBottom = false;
            }
        }

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
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Read Artist thread started");
            showProgress(mReadProgress, true);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                mArtists = UserWrapper.getInstance().getArtists();
            } catch (IOException e) {
                // TODO: 26/04/2016 handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (isAdded()) {
                showProgress(mReadProgress, false);
                if (!mArtists.isEmpty()) {
                    refreshAdapter();
                }
            }

            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Read Artists thread finished");

        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Read Artists thread cancelled");
            showProgress(mReadProgress, false);
        }
    }

    private class LoadMoreArtists extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Load More Artist thread started");
            showProgress(mLoadMoreProgress, true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mArtists.addAll(UserWrapper.getInstance().loadMoreArtists());
                Thread.sleep(750);
            } catch (IOException e) {
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            showProgress(mLoadMoreProgress, false);
            refreshAdapter();
            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Load More Artists thread finished");
        }

        @Override
        protected void onCancelled() {
            showProgress(mLoadMoreProgress, false);
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Load More Artist thread cancelled");
        }
    }

    private class SearchArtists extends AsyncTask<Void, Void, Void> {

        private String mQuery;

        public SearchArtists(String query) {
            mQuery = query;
        }

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Artist Search thread started");
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
            refreshAdapter();
            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Artist Search thread finished");
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Artist Search thread cancelled");
        }
    }
}
