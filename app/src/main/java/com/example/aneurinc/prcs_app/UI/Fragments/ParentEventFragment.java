package com.example.aneurinc.prcs_app.UI.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.ParentEventActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ParentEventFragAdapter;
import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.datamodel.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ParentEventFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<IParentEvent> parentEventList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private Activity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.event_progress);
        getParentEvents();
        return view;
    }

    private void getParentEvents() {
        ReadParentEvents task = new ReadParentEvents(mContext);
        task.execute();
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
            parentEventList = UserWrapper.getInstance().getParentEvents();
            return parentEventList;
        }

        @Override
        protected void onPostExecute(List<IParentEvent> parentEvents) {
            showProgress(false);
            GridView gridView = (GridView) mContext.findViewById(R.id.event_grid_view);
            gridView.setAdapter(new ParentEventFragAdapter(mContext, parentEvents));
            gridView.setOnItemClickListener(ParentEventFragment.this);
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }


}
