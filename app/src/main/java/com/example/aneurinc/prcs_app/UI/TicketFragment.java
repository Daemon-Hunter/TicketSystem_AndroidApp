package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 01/03/2016.
 */
public class TicketFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
        MyTicketsListAdapter adapter = new MyTicketsListAdapter(getActivity());
        ListView list = (ListView) getView().findViewById(R.id.my_tickets_list);
        list.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        // called before fragment or parent activity is destroyed,
        // so saved data here!
        super.onPause();
    }
}
