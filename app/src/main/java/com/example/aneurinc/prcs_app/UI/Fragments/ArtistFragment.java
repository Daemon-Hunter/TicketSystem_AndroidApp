package com.example.aneurinc.prcs_app.UI.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.example.aneurinc.prcs_app.UI.Activities.ArtistActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ArtistFragAdapter;
import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.datamodel.IArtist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<IArtist> artistList = new ArrayList<>();
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.artist_progress);

        readArtists();

        return view;
    }

    private void readArtists() {
        ReadArtists task = new ReadArtists();
        task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getActivity(), ArtistActivity.class);
        i.putExtra(ArtistActivity.EventImageIndex, artistList.get(position).getArtistID());
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

    private class ReadArtists extends AsyncTask<Void, Void, List<IArtist>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<IArtist> doInBackground(Void... voids) {
            return APIHandle.getArtistAmount(5, 0);
        }

        @Override
        protected void onPostExecute(List<IArtist> artists) {

            showProgress(false);
            GridView gridView = (GridView) getActivity().findViewById(R.id.artist_grid_view);
            gridView.setAdapter(new ArtistFragAdapter(getActivity(), artists));
            gridView.setOnItemClickListener(ArtistFragment.this);

        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }


}
