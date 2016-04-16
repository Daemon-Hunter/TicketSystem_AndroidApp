package com.example.aneurinc.prcs_app.UI.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.aneurinc.prcs_app.Database.APIConnection;
import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.Database.MapToObject;
import com.example.aneurinc.prcs_app.Datamodel.Artist;
import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.ArtistActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ArtistGridAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private List<Artist> artistList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        executeAsyncTask();
        gridView = (GridView) view.findViewById(R.id.artist_grid_view);
        gridView.setOnItemClickListener(this);

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

    private class ReadArtists extends AsyncTask<Void, Void, List<Artist>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO display something like display a progress bar
        }

        @Override
        protected List<Artist> doInBackground(Void... voids) {

            APIConnection connection = new APIConnection(DatabaseTable.ARTIST);
            List<Map<String, String>> listOfMaps = connection.readAll();

            for (Map<String, String> currMap : listOfMaps) {
                artistList.add(MapToObject.ConvertArtist(currMap));
            }

            return artistList;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {

            gridView.setAdapter(new ArtistGridAdapter(getContext(), artistList));

        }
    }


}
