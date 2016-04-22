package com.example.aneurinc.prcs_app.UI.Fragments;

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
public class ParentEventFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, Animator.AnimatorListener {

    private List<IParentEvent> parentEventList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private ReadParentEvents mTask;
    private static final int ANIM_TIME = 200;

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
        if (isTaskRunning()) {
            mTask.cancel(true);
        }
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
            // TODO: 22/04/2016 add refresh
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
            showProgress(true);
        }

        @Override
        protected List<IParentEvent> doInBackground(Void... params) {

            try {
                if (parentEventList.isEmpty()) {
                    parentEventList = UserWrapper.getInstance().getParentEvents();
                } else {
                    parentEventList.addAll(UserWrapper.getInstance().loadMoreParentEvents());
                }
            } catch (IOException e) {
                //TODO handle exception
            }

            return parentEventList;
        }


        @Override
        protected void onPostExecute(List<IParentEvent> parentEvents) {

            if (mContext != null && isAdded()) {
                showProgress(false);

                if (!parentEvents.isEmpty()) {
                    GridView gridView = (GridView) mContext.findViewById(R.id.event_grid_view);
                    gridView.setAdapter(new ParentEventFragAdapter(mContext, parentEvents));
                    gridView.setOnItemClickListener(ParentEventFragment.this);
                    gridView.setOnScrollListener(ParentEventFragment.this);
                }
            }
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Event Thread cancelled");
            showProgress(false);
        }
    }


}
