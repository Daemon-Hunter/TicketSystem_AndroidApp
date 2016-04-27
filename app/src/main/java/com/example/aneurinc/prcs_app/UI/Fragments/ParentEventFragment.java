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
public class ParentEventFragment extends Fragment implements AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener, Animator.AnimatorListener {

    private List<IParentEvent> mParentEvents = new ArrayList<>();
    private ProgressBar mReadProgress, mLoadMoreProgress;
    private ReadParentEvents mReadTask;
    private LoadMoreParentEvents mLoadMoreTask;
    private static final int ANIM_TIME = 200;
    private MainActivity mMainActivity;
    private GridView mGridView;
    private boolean isScrolling, atBottom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_event, container, false);

        mGridView = (GridView) view.findViewById(R.id.event_grid_view);
        setSwipe(mGridView);

        if (getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        }

        setSwipe(view);

        mReadProgress = (ProgressBar) view.getRootView().findViewById(R.id.read_progress);
        mLoadMoreProgress = (ProgressBar) view.getRootView().findViewById(R.id.load_more_progress);

        getParentEvents();
        return view;
    }

    private void getParentEvents() {
        mReadTask = new ReadParentEvents(getActivity());
        mReadTask.execute();
    }

    private void setSwipe(View v) {
        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                mMainActivity.switchFragment(new ArtistFragment(), FragmentType.ARTIST);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getActivity(), ParentEventActivity.class);
        i.putExtra(ParentEventActivity.PARENT_EVENT_ID, mParentEvents.get(position).getID());
        startActivity(i);

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
                atBottom = true;
                if (!isTaskRunning(mLoadMoreTask)) {
                    mLoadMoreTask = new LoadMoreParentEvents();
                    mLoadMoreTask.execute();
                }
            } else {
                atBottom = true;
            }
        }
    }

    private class ReadParentEvents extends AsyncTask<Void, Void, List<IParentEvent>> {

        private Activity mContext;

        public ReadParentEvents(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Event Thread started");
            showProgress(mReadProgress, true);
        }

        @Override
        protected List<IParentEvent> doInBackground(Void... params) {

            try {
                mParentEvents = UserWrapper.getInstance().getParentEvents();
            } catch (IOException e) {
                //TODO handle exception
            }

            return mParentEvents;
        }


        @Override
        protected void onPostExecute(List<IParentEvent> parentEvents) {

            if (mContext != null && isAdded()) {
                showProgress(mReadProgress, false);
                if (!parentEvents.isEmpty()) {
                    mGridView.setAdapter(new ParentEventFragAdapter(mContext, parentEvents));
                    mGridView.setOnItemClickListener(ParentEventFragment.this);
                    mGridView.setOnScrollListener(ParentEventFragment.this);
                }
            }

            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: ParentEvent thread finished");
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Event Thread cancelled");
            showProgress(mReadProgress, false);
        }
    }

    private class LoadMoreParentEvents extends AsyncTask<Void, Void, List<IParentEvent>> {

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Load More Parent Events thread started");
            showProgress(mLoadMoreProgress, true);
        }

        @Override
        protected List<IParentEvent> doInBackground(Void... params) {

            try {
                mParentEvents.addAll(UserWrapper.getInstance().loadMoreParentEvents());
                Thread.sleep(750);
            } catch (IOException e) {
            } catch (InterruptedException e) {
            }

            return mParentEvents;
        }

        @Override
        protected void onPostExecute(List<IParentEvent> iParentEvents) {
            showProgress(mLoadMoreProgress, false);
            ArrayAdapter mAdapter = (ArrayAdapter) mGridView.getAdapter();
            mAdapter.notifyDataSetChanged();
            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Load More Parent Events thread " +
                    "finished");
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Load More Parent Events thread cancelled");
            showProgress(mLoadMoreProgress, false);
        }
    }
}
