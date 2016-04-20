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
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.VenueActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.VenueFragAdapter;
import com.google.jkellaway.androidapp_datamodel.datamodel.IVenue;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by aneurinc on 06/03/2016.
 */
public class VenueFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<IVenue> venues = new LinkedList<>();
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venue, container, false);
        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.venue_progress);
        getVenues();
        return view;
    }

    private void getVenues() {
        ReadVenues task = new ReadVenues(getActivity());
        task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), VenueActivity.class);
        intent.putExtra(VenueActivity.VENUE_ID, venues.get(position).getVenueID());
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

    private class ReadVenues extends AsyncTask<Void, Void, List<IVenue>> {

        private final Activity mContext;

        public ReadVenues(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected List<IVenue> doInBackground(Void... params) {
            return new LinkedList<>();
        }

        @Override
        protected void onPostExecute(List<IVenue> venues) {
            showProgress(false);
            ListView list = (ListView) mContext.findViewById(R.id.venue_list);
            list.setAdapter(new VenueFragAdapter(mContext, venues));
            list.setOnItemClickListener(VenueFragment.this);
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}
