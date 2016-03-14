package com.example.aneurinc.prcs_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public void onPause() {
        // called before fragment or parent activity is destroyed,
        // so saved data here!
        super.onPause();
    }
}
