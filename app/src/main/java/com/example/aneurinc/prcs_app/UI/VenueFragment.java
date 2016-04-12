package com.example.aneurinc.prcs_app.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 06/03/2016.
 */
public class VenueFragment extends Fragment {

    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_venue, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
        VenueListAdapter adapter = new VenueListAdapter(getActivity());
        list = (ListView) getView().findViewById(R.id.venue_list);
        list.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        // called before fragment or parent activity is destroyed,
        // so saved data here!
        super.onPause();
    }
}
