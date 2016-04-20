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
import com.example.aneurinc.prcs_app.Datamodel.ParentEvent;
import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.ParentEventActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ParentEventFragAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ParentEventFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<ParentEvent> parentEventList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        getParentEvents();
        return view;
    }

    private void getParentEvents() {
        ReadParentEvents task = new ReadParentEvents();
        task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getActivity(), ParentEventActivity.class);
        i.putExtra(ParentEventActivity.PARENT_EVENT_ID, parentEventList.get(position).getParentEventID());
        startActivity(i);

    }

    private class ReadParentEvents extends AsyncTask<Void, Void, List<ParentEvent>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO display something like display a progress bar
        }

        @Override
        protected List<ParentEvent> doInBackground(Void... params) {

            APIConnection connection = new APIConnection(DatabaseTable.PARENT_EVENT);
            List<Map<String, String>> listOfMaps = connection.readAll();

            for (Map<String, String> currMap : listOfMaps) {
                parentEventList.add(MapToObject.ConvertParentEvent(currMap));
            }

            return parentEventList;
        }

        @Override
        protected void onPostExecute(List<ParentEvent> parentEvents) {
            GridView gridView = (GridView) getActivity().findViewById(R.id.event_grid_view);
            gridView.setAdapter(new ParentEventFragAdapter(getActivity(), parentEventList));
            gridView.setOnItemClickListener(ParentEventFragment.this);
        }
    }


}
