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

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.ParentEventActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.ParentEventFragAdapter;
import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.datamodel.IParentEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class ParentEventFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<IParentEvent> parentEventList = new ArrayList<>();

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

    private class ReadParentEvents extends AsyncTask<Void, Void, List<IParentEvent>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO display something like display a progress bar
        }

        @Override
        protected List<IParentEvent> doInBackground(Void... params) {

            return APIHandle.getParentAmount(21, 0);
        }

        @Override
        protected void onPostExecute(List<IParentEvent> parentEvents) {
            GridView gridView = (GridView) getActivity().findViewById(R.id.event_grid_view);
            gridView.setAdapter(new ParentEventFragAdapter(getActivity(), parentEvents));
            gridView.setOnItemClickListener(ParentEventFragment.this);
        }
    }


}
