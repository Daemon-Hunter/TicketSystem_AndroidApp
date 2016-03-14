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
public class ArtistFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        GridView gridView = (GridView)view.findViewById(R.id.artist_grid_view);
        gridView.setAdapter(new GridAdapter(this.getActivity(), Constants.artistImages));

        addGridViewListener(gridView);

        return view;
    }

    private void addGridViewListener(GridView gridView) {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Log.d(MainActivity.DEBUG_TAG, String.format("You clicked event tile: %d", position));
                Intent i = new Intent(getActivity(), ArtistActivity.class);
                i.putExtra(ArtistActivity.EventImageIndex, position);
                getActivity().startActivity(i);

            }
        });
    }

}
