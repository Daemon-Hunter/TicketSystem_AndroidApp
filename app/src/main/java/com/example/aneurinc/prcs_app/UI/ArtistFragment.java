package com.example.aneurinc.prcs_app.UI;

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

import com.example.aneurinc.prcs_app.Database.APIConnection;
import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.Database.MapToObject;
import com.example.aneurinc.prcs_app.Datamodel.Artist;
import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.Utility.Information;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ArtistFragment extends Fragment implements AdapterView.OnItemClickListener {
    View view;
    GridView gridView;
    ArtistsGridAdapter gridAdapter;
    List<Artist> listOfArtists= new ArrayList<>();
    Information info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_artist, container, false);
        readAllAsyncTask task = new readAllAsyncTask();
        task.execute();
         info = Information.getInstance();

        gridView = (GridView) view.findViewById(R.id.artist_grid_view);
        gridView.setAdapter(new ArtistsGridAdapter(this.getActivity(),listOfArtists));
        gridView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getActivity(), ArtistActivity.class);
        i.putExtra(ArtistActivity.EventImageIndex, position);
        getActivity().startActivity(i);

    }
    private class readAllAsyncTask extends AsyncTask<Void,Void,List<Artist>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("PRE","Display loading bar");
            // Do something like display a progress bar
        }



        @Override
        protected List<Artist> doInBackground(Void... voids) {
            APIConnection connection = new APIConnection(DatabaseTable.ARTIST);
            List<Map<String,String>> listOfMaps = connection.readAll();
            for(Map<String,String> currMap : listOfMaps)
            {
                listOfArtists.add(MapToObject.ConvertArtist(currMap));

            }
            return listOfArtists;

        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            Log.e("End","Post Execute");
            Log.e("CUSTOMER",artists.get(0).getArtistName());
            Log.e("CUSTOMER",listOfArtists.get(1).getArtistName());
            Log.e("SIZE",Integer.toString(listOfArtists.size()));
            info.setListOfArtists(listOfArtists);
            gridAdapter = new ArtistsGridAdapter(getContext(),listOfArtists);
            gridView.setAdapter(gridAdapter);

        }
    }


}
