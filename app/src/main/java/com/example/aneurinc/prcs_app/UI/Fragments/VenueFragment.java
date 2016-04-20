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

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.VenueActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.VenueFragAdapter;
import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.datamodel.IVenue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by aneurinc on 06/03/2016.
 */
public class VenueFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<IVenue> venues = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venue, container, false);
        getVenues();
        return view;
    }

    private void getVenues() {
        ReadVenues task = new ReadVenues();
        task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), VenueActivity.class);
        intent.putExtra(VenueActivity.VenueImageIndex, position);
        getActivity().startActivity(intent);
    }

    private class ReadVenues extends AsyncTask<Void, Void, List<IVenue>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO display something like display a progress bar
        }

        @Override
        protected List<IVenue> doInBackground(Void... params) {

            return APIHandle.getVenueAmount(21, 0);
        }

        @Override
        protected void onPostExecute(List<IVenue> venues) {
            ListView list = (ListView) getActivity().findViewById(R.id.venue_list);
            list.setAdapter(new VenueFragAdapter(getActivity(), venues));
            list.setOnItemClickListener(VenueFragment.this);
        }
    }
}
