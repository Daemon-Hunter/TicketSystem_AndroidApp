package com.example.aneurinc.prcs_app.UI.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.ArtistActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ArtistFragAdapter;
import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.datamodel.Artist;
import com.google.jkellaway.androidapp_datamodel.datamodel.IArtist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<IArtist> artistList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        executeAsyncTask();

        return view;
    }

    private void executeAsyncTask() {
        ReadArtists task = new ReadArtists();
        task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getActivity(), ArtistActivity.class);
        i.putExtra(ArtistActivity.EventImageIndex, artistList.get(position).getArtistID());
        getActivity().startActivity(i);
    }

    private class ReadArtists extends AsyncTask<Void, Void, List<IArtist>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO display something like display a progress bar
        }

        @Override
        protected List<IArtist> doInBackground(Void... voids) {
            List<IArtist> a = APIHandle.getArtistAmount(21,0);

            Log.d("ArtistFragment", a.get(0).getArtistName());
            return a;
        }

        @Override
        protected void onPostExecute(List<IArtist> artists) {

            GridView gridView = (GridView) getActivity().findViewById(R.id.artist_grid_view);
            gridView.setAdapter(new ArtistFragAdapter(getActivity(), artists));
            gridView.setOnItemClickListener(ArtistFragment.this);

        }
    }


}
