package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class EventFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);

        GridView gridView = (GridView)view.findViewById(R.id.event_grid_view);
        gridView.setAdapter(new GridAdapter(this.getActivity(), Constants.eventImages, Constants.eventName));

        setOnItemClickListener(gridView);

        return view;
    }

    private void setOnItemClickListener(GridView gridView) {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Log.d(MainActivity.DEBUG_TAG, String.format("You clicked event tile: %d", position));

                Intent i = new Intent(getActivity(), EventActivity.class);
                i.putExtra(EventActivity.EventImageIndex, position);
                startActivity(i);

            }
        });
    }

    @Override
    public void onPause() {
        // save data if necessary
        super.onPause();
    }

}
