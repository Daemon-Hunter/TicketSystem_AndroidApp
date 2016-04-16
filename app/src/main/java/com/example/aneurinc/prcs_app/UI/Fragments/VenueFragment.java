package com.example.aneurinc.prcs_app.UI.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.aneurinc.prcs_app.Database.APIConnection;
import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.Database.MapToObject;
import com.example.aneurinc.prcs_app.Datamodel.Venue;
import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.VenueActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.VenueListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aneurinc on 06/03/2016.
 */
public class VenueFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<Venue> venues = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        ListView list = (ListView) view.findViewById(R.id.venue_list);
        list.setOnItemClickListener(this);

        executeAsyncTask();

        return view;
    }

    private void executeAsyncTask() {
        ReadVenues task = new ReadVenues();
        task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), VenueActivity.class);
        intent.putExtra(VenueActivity.VenueImageIndex, position);
        getActivity().startActivity(intent);
    }

    private class ReadVenues extends AsyncTask<Void, Void, List<Venue>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO display something like display a progress bar
        }

        @Override
        protected List<Venue> doInBackground(Void... params) {

            APIConnection connection = new APIConnection(DatabaseTable.VENUE);
            List<Map<String, String>> listOfMaps = connection.readAll();

            for (Map<String, String> currMap : listOfMaps) {
                venues.add((Venue)MapToObject.ConvertVenue(currMap));
            }

            return venues;
        }

        @Override
        protected void onPostExecute(List<Venue> venues) {
            ListView list = (ListView) getView().findViewById(R.id.venue_list);
            list.setAdapter(new VenueListAdapter(getActivity(), venues));
        }
    }
}
