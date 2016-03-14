package com.example.aneurinc.prcs_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by aneurinc on 06/03/2016.
 */
public class VenueFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        GridView gridView = (GridView)view.findViewById(R.id.venue_grid_view);
        gridView.setAdapter(new GridAdapter(this.getActivity(), Constants.eventImages));

        setOnItemClickListener(gridView);

        return view;
    }

    private void setOnItemClickListener(GridView gridView) {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Log.d(MainActivity.DEBUG_TAG, String.format("You clicked event tile: %d", position));

            }
        });
    }

    @Override
    public void onPause() {
        // called before fragment or parent activity is destroyed,
        // so saved data here!
        super.onPause();
    }
}
