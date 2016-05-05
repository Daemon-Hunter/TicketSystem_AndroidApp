package com.example.aneurinc.prcs_app.UI.fragments;

import android.animation.Animator;
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
public class VenueFragment extends Fragment implements AdapterView.OnItemClickListener, Animator.AnimatorListener, AbsListView.OnScrollListener, View.OnAttachStateChangeListener, SearchView.OnQueryTextListener {

    // list of venues
    private List<IVenue> mVenues;

    // UI references
    private ProgressBar mReadProgress, mLoadMoreProgress;
    private ListView mListView;
    private SearchView mSearchView;

    // async tasks
    private ReadVenues mReadTask;
    private LoadMoreVenues mLoadMoreTask;
    private SearchVenues mSearchTask;

    // progress bar animation duration
    private static final int ANIM_TIME = 200;

    private MainActivity mMainActivity;

    // flags
    private boolean isScrolling, atBottom;

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

        mReadProgress = (ProgressBar) view.getRootView().findViewById(R.id.read_progress);
        mLoadMoreProgress = (ProgressBar) view.getRootView().findViewById(R.id.load_more_progress);

        readVenues();

        return view;
    }

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

    private void readVenues() {
        if (!isTaskRunning(mReadTask)) {
            mReadTask = new ReadVenues(getActivity());
            mReadTask.execute();
        }
    }

    private void loadMoreVenues() {
        if (!isTaskRunning(mLoadMoreTask)) {
            mLoadMoreTask = new LoadMoreVenues();
            mLoadMoreTask.execute();
        }
    }

    private void searchVenues(String query) {
        if (!isTaskRunning(mSearchTask)) {
            mSearchTask = new SearchVenues(query);
            mSearchTask.execute();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.getItem(0);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.addOnAttachStateChangeListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Intent intent = new Intent(getActivity(), VenueActivity.class);
        intent.putExtra(VenueActivity.VENUE_ID, mVenues.get(position).getID());
        getActivity().startActivity(intent);
    }

    private void showProgress(ProgressBar progressBar, final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(ANIM_TIME).alpha(show ? 1 : 0).setListener(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {
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
                if (!isTaskRunning(mLoadMoreTask)) {
                    atBottom = true;
                    loadMoreVenues();
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
        readVenues();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchVenues(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            searchVenues(newText);
        }
        return false;
    }

    private void refreshAdapter() {
        VenueFragAdapter mAdapter = (VenueFragAdapter) mListView.getAdapter();
        mAdapter.clear();
        mAdapter.addAll(mVenues);
        mAdapter.notifyDataSetChanged();
    }

    private class ReadVenues extends AsyncTask<Void, Void, Void> {

        private Activity mContext;

        public ReadVenues(Activity mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Venue Thread started");
            showProgress(mReadProgress, true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mVenues = UserWrapper.getInstance().getVenues();
            } catch (IOException e) {
                //TODO handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            showProgress(mReadProgress, false);

            if (mContext != null && isAdded()) {
                if (!mVenues.isEmpty()) {
                    refreshAdapter();
                }
            }

            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Venue thread finished");
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Venue Thread cancelled");
            showProgress(mReadProgress, false);
        }
    }

    private class LoadMoreVenues extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Load More Venues thread started");
            showProgress(mLoadMoreProgress, true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                mVenues.addAll(UserWrapper.getInstance().loadMoreVenues());
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
            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Load More Venues thread finished");
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Load More Venues thread cancelled");
            showProgress(mLoadMoreProgress, false);
        }
    }

    private class SearchVenues extends AsyncTask<Void, Void, Void> {

        private String mQuery;

        public SearchVenues(String query) {
            mQuery = query;
        }

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Search Venues thread started");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mVenues = UserWrapper.getInstance().searchVenues(mQuery);
            } catch (IOException e) {
                // TODO: 28/04/2016 handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            refreshAdapter();
            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Search Venues thread finished");
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Search Venue thread cancelled");
        }
    }
}
