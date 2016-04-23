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
import com.example.aneurinc.prcs_app.UI.Activities.ArtistActivity;
import com.example.aneurinc.prcs_app.UI.Activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ArtistFragAdapter;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private List<IArtist> artistList;
    private ProgressBar mProgressBar;
    private ReadArtists mTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.artist_progress);

        readArtists();

        return view;
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

    private void readArtists() {
        mTask = new ReadArtists(getActivity());
        mTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), ArtistActivity.class);
        i.putExtra(ArtistActivity.ARTIST_ID, artistList.get(position).getID());
        getActivity().startActivity(i);
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
            //// TODO: 22/04/2016 add refresh
        }

    }

    private class ReadArtists extends AsyncTask<Void, Void, List<IArtist>> {

        private Activity mContext;

        public ReadArtists(Activity mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected List<IArtist> doInBackground(Void... voids) {
            try {
                artistList = UserWrapper.getInstance().getArtists();
            } catch (IOException e) {
                //TODO handle exception
            }
            return artistList;
        }

        @Override
        protected void onPostExecute(List<IArtist> artists) {
            if (artists != null && !artists.isEmpty()) {
                showProgress(false);
                GridView gridView = (GridView) mContext.findViewById(R.id.artist_grid_view);
                gridView.setAdapter(new ArtistFragAdapter(mContext, artists));
                gridView.setOnItemClickListener(ArtistFragment.this);
                gridView.setOnScrollListener(ArtistFragment.this);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mTask.cancel(true);
        }
    }


}
