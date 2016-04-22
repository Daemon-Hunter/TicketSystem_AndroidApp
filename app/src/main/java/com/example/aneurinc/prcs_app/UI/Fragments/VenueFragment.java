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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.Activities.VenueActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.VenueFragAdapter;
import com.google.jkellaway.androidapp_datamodel.datamodel.IVenue;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by aneurinc on 06/03/2016.
 */
public class VenueFragment extends Fragment implements AdapterView.OnItemClickListener, Animator.AnimatorListener {

    private List<IVenue> venues;
    private ProgressBar mProgressBar;
    private ReadVenues mTask;
    private static final int ANIM_TIME = 200;

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


    private class ReadVenues extends AsyncTask<Void, Void, List<IVenue>> {

        private Activity mContext;

        public ReadVenues(Activity mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Venue Thread started");
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

            if (mContext != null && isAdded()) {
                showProgress(false);
                if (venues != null && !venues.isEmpty()) {
                    ListView list = (ListView) mContext.findViewById(R.id.venue_list);
                    list.setAdapter(new VenueFragAdapter(mContext, venues));
                    list.setOnItemClickListener(VenueFragment.this);
                }
            }
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Venue Thread cancelled");
            showProgress(false);
        }
    }
}
