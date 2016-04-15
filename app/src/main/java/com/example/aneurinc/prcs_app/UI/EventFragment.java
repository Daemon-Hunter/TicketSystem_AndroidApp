package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 02/03/2016.
 */
public class EventFragment extends Fragment implements AdapterView.OnItemClickListener {

    private MainActivity main;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = new MainActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);

        GridView gridView = (GridView)view.findViewById(R.id.event_grid_view);
        gridView.setAdapter(new GridAdapter(this.getActivity(), Constants.eventImages, Constants.eventName));
        gridView.setOnItemClickListener(this);

        gridView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                Button b = (Button) getActivity().findViewById(R.id.btn_event);
                main.createFragment(new ArtistFragment(), "ARTISTS", b);
            }
        });

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getActivity(), EventActivity.class);
        i.putExtra(EventActivity.EventImageIndex, position);
        startActivity(i);

    }


}
