package com.example.aneurinc.prcs_app.UI.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.Activities.ParentEventActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ParentEventFragAdapter;
import com.google.jkellaway.androidapp_datamodel.datamodel.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ParentEventFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private List<IParentEvent> parentEventList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private ReadParentEvents mTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.event_progress);
        getParentEvents();
        return view;
    }

    private void getParentEvents() {
        mTask = new ReadParentEvents(getActivity());
        mTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getActivity(), ParentEventActivity.class);
        i.putExtra(ParentEventActivity.PARENT_EVENT_ID, parentEventList.get(position).getParentEventID());
        startActivity(i);

    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

    @Override
    public void onPause() {
        Log.d(MainActivity.DEBUG_TAG, "onPause: ");

        if (isTaskRunning()) {
            mTask.cancel(true);
        }

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(MainActivity.DEBUG_TAG, "onStop: ");

        if (isTaskRunning()) {
            mTask.cancel(true);
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(MainActivity.DEBUG_TAG, "onDestroy: ");

        if (isTaskRunning()) {
            mTask.cancel(true);
        }

        super.onDestroy();
    }

    private boolean isTaskRunning() {
        return mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
            getParentEvents();

        }
    }

    private class ReadParentEvents extends AsyncTask<Void, Void, List<IParentEvent>> {

        private Activity mContext;

        public ReadParentEvents(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected List<IParentEvent> doInBackground(Void... params) {

            try {
                if (parentEventList.size() > 0) {
                    parentEventList.addAll(UserWrapper.getInstance().loadMoreParentEvents());
                } else {
                    parentEventList = UserWrapper.getInstance().getParentEvents();
                }
            }catch (IOException e){
                //TODO handle exception
            }

            return parentEventList;
        }

        @Override
        protected void onPostExecute(List<IParentEvent> parentEvents) {
            showProgress(false);
            if (parentEvents != null && !parentEvents.isEmpty()) {
                GridView gridView = (GridView) mContext.findViewById(R.id.event_grid_view);
                gridView.setAdapter(new ParentEventFragAdapter(mContext, parentEvents));
                gridView.setOnItemClickListener(ParentEventFragment.this);
                gridView.setOnScrollListener(ParentEventFragment.this);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mTask.cancel(true);
        }
    }


}
