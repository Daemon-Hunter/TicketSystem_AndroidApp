package com.example.aneurinc.prcs_app.UI.fragments;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener, Animator.AnimatorListener {

    private List<IArtist> artistList;
    private ProgressBar mProgressBar;
    private ReadArtists readTask;
    private LoadMoreArtists loadTask;
    private static final int ANIM_TIME = 200;
    private MainActivity mMainActivity;
    private GridView mGridView;
    private boolean isScrolling;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        mGridView = (GridView) view.findViewById(R.id.artist_grid_view);

        if (getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        }

        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.artist_progress);

        readArtists();


        setSwipe(view);

        return view;
    }

    private void setSwipe(View v) {
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
        if (isTaskRunning(readTask)) {
            readTask.cancel(true);
        }
        if (isTaskRunning(loadTask)) {
            loadTask.cancel(true);
        }
    }

    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    private void readArtists() {
        readTask = new ReadArtists(getActivity());
        readTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ArtistActivity.class);
        intent.putExtra(ArtistActivity.ARTIST_ID, artistList.get(position).getID());
        getActivity().startActivity(intent);
    }

    private void showProgress(final boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.animate().setDuration(ANIM_TIME).alpha(show ? 1 : 0).setListener(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mProgressBar.setVisibility(View.VISIBLE);
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
                if (!isTaskRunning(loadTask)) {
                    loadTask = new LoadMoreArtists();
                    loadTask.execute();
                }
            }
        }

    }

    private class ReadArtists extends AsyncTask<Void, Void, List<IArtist>> {

        private Activity mContext;

        public ReadArtists(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Read Artist thread started");
            showProgress(true);
        }

        @Override
        protected List<IArtist> doInBackground(Void... voids) {

            try {
                artistList = UserWrapper.getInstance().getArtists();
            } catch (IOException e) {
                // TODO: 26/04/2016 handle exception
            }
            return artistList;
        }

        @Override
        protected void onPostExecute(List<IArtist> artists) {

            if (isAdded()) {
                showProgress(false);
                if (!artists.isEmpty()) {
                    mGridView.setAdapter(new ArtistFragAdapter(mContext, artists));
                    mGridView.setOnItemClickListener(ArtistFragment.this);
                    mGridView.setOnScrollListener(ArtistFragment.this);
                    setSwipe(mGridView);
                }
            }

            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Read Artists thread finished");

        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Read Artists thread cancelled");
            showProgress(false);
        }
    }

    private class LoadMoreArtists extends AsyncTask<Void, Void, List<IArtist>> {

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Load More Artist thread started");
        }

        @Override
        protected List<IArtist> doInBackground(Void... params) {
            try {
                artistList.addAll(UserWrapper.getInstance().loadMoreArtists());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return artistList;
        }

        @Override
        protected void onPostExecute(List<IArtist> iArtists) {
            ArrayAdapter mAdapter = (ArrayAdapter) mGridView.getAdapter();
            mAdapter.notifyDataSetChanged();
            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Load More Artists thread finished");
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Load More Artist thread cancelled");
        }
    }
}
