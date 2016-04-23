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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.Activities.VenueActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.VenueFragAdapter;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by aneurinc on 06/03/2016.
 */
public class VenueFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<IVenue> venues;
    private ProgressBar mProgressBar;
    private ReadVenues mTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venue, container, false);
        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.venue_progress);
        getVenues();
        return view;
    }

    private void getVenues() {
        mTask = new ReadVenues(getActivity());
        mTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), VenueActivity.class);
        intent.putExtra(VenueActivity.VENUE_ID, position);
        getActivity().startActivity(intent);
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


    private class ReadVenues extends AsyncTask<Void, Void, List<IVenue>> {

        private Activity mContext;

        public ReadVenues(Activity mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected List<IVenue> doInBackground(Void... params) {
            try {
                venues = UserWrapper.getInstance().getVenues();
            } catch (IOException e) {
                //TODO handle exception
            }
            return venues;
        }

        @Override
        protected void onPostExecute(List<IVenue> venues) {
            if (venues != null && !venues.isEmpty()) {
                showProgress(false);
                ListView list = (ListView) mContext.findViewById(R.id.venue_list);
                list.setAdapter(new VenueFragAdapter(mContext, venues));
                list.setOnItemClickListener(VenueFragment.this);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mTask.cancel(true);
        }
    }
}
