package com.example.aneurinc.prcs_app.UI.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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

    private int mLastFirstVisibleItem;

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.getItem(0);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.addOnAttachStateChangeListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void readParentEvents() {
        if (!isTaskRunning(mReadTask)) {
            mReadTask = new ReadParentEvents();
            mReadTask.execute();
        }
    }

    private void loadMoreParentEvents() {
        if (!isTaskRunning(mLoadMoreTask)) {
            mLoadMoreTask = new LoadMoreParentEvents();
            mLoadMoreTask.execute();
        }
    }

    private void searchParentEvents(String query) {
        if (!isTaskRunning(mSearchTask)) {
            mSearchTask = new SearchParentEvents(query);
            mSearchTask.execute();
        }
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
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void showProgress(final ProgressBar progressBar, final boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
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

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem > mLastFirstVisibleItem) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount) loadMoreParentEvents();
        }

        mLastFirstVisibleItem = firstVisibleItem;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchParentEvents(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            searchParentEvents(newText);
        }
        return false;
    }

    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        if (isTaskRunning(mSearchTask)) {
            mSearchTask.cancel(true);
        }
        readParentEvents();

    }

    private void refreshAdapter() {
        ParentEventFragAdapter mAdapter = (ParentEventFragAdapter) mGridView.getAdapter();
        mAdapter.clear();
        mAdapter.addAll(mParentEvents);
        mAdapter.notifyDataSetChanged();
    }

    private class SearchParentEvents extends AsyncTask<Void, Void, Void> {

        private String mQuery;

        public SearchParentEvents(String query) {
            mQuery = query;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mParentEvents = UserWrapper.getInstance().searchParentEvents(mQuery);
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
            mSearchTask = null;
        }
    }

    private class ReadParentEvents extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgress(mReadProgressBar, true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                UserWrapper.getInstance().setAmountToLoad(18);
                mParentEvents = UserWrapper.getInstance().getParentEvents();
            } catch (IOException e) {
                //TODO handle exception
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mReadTask = null;

            showProgress(mReadProgressBar, false);

            if (isAdded()) {
                if (!mParentEvents.isEmpty()) {
                    refreshAdapter();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mReadTask = null;
            showProgress(mReadProgressBar, false);
        }
    }

    private class LoadMoreParentEvents extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgress(mLoadProgressBar, true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                mParentEvents.addAll(UserWrapper.getInstance().loadMoreParentEvents());
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
            mLoadMoreTask = null;
            showProgress(mLoadProgressBar, false);
        }
    }
}
