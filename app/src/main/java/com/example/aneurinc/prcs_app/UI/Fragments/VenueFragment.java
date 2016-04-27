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
import java.util.List;

/**
 * Created by aneurinc on 06/03/2016.
 */
public class VenueFragment extends Fragment implements AdapterView.OnItemClickListener,
        Animator.AnimatorListener, AbsListView.OnScrollListener {

    private List<IVenue> mVenues;
    private ProgressBar mReadProgress, mLoadMoreProgress;
    private ReadVenues mReadTask;
    private LoadMoreVenues mLoadMoreTask;
    private static final int ANIM_TIME = 200;
    private MainActivity mMainActivity;
    private ListView mListView;
    private boolean isScrolling, atBottom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        mListView = (ListView) view.findViewById(R.id.venue_list);
        setSwipe(mListView);

        if (getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        }

        setSwipe(view);

        mReadProgress = (ProgressBar) view.getRootView().findViewById(R.id.read_progress);
        mLoadMoreProgress = (ProgressBar) view.getRootView().findViewById(R.id.load_more_progress);

        getVenues();

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

    private void getVenues() {
        mReadTask = new ReadVenues(getActivity());
        mReadTask.execute();
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
                    mLoadMoreTask = new LoadMoreVenues();
                    mLoadMoreTask.execute();
                }
            } else {
                atBottom = false;
            }
        }
    }

    private class ReadVenues extends AsyncTask<Void, Void, List<IVenue>> {

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
        protected List<IVenue> doInBackground(Void... params) {
            try {
                mVenues = UserWrapper.getInstance().getVenues();
            } catch (IOException e) {
                //TODO handle exception
            }
            return mVenues;
        }

        @Override
        protected void onPostExecute(List<IVenue> venues) {

            if (mContext != null && isAdded()) {
                showProgress(mReadProgress, false);
                if (venues != null && !venues.isEmpty()) {
                    mListView.setAdapter(new VenueFragAdapter(mContext, venues));
                    mListView.setOnItemClickListener(VenueFragment.this);
                    mListView.setOnScrollListener(VenueFragment.this);
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

    private class LoadMoreVenues extends AsyncTask<Void, Void, List<IVenue>> {

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Load More Venues thread started");
            showProgress(mLoadMoreProgress, true);
        }

        @Override
        protected List<IVenue> doInBackground(Void... params) {

            try {
                mVenues.addAll(UserWrapper.getInstance().loadMoreVenues());
                Thread.sleep(750);
            } catch (IOException e) {
            } catch (InterruptedException e) {
            }

            return mVenues;
        }

        @Override
        protected void onPostExecute(List<IVenue> iVenues) {
            showProgress(mLoadMoreProgress, false);
            ArrayAdapter mAdapter = (ArrayAdapter) mListView.getAdapter();
            mAdapter.notifyDataSetChanged();
            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Load More Venues thread finished");
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Load More Venues thread cancelled");
            showProgress(mLoadMoreProgress, false);
        }
    }
}
