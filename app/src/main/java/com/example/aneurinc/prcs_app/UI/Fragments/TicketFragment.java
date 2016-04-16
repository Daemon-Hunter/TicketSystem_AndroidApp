package com.example.aneurinc.prcs_app.UI.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.aneurinc.prcs_app.Database.APIConnection;
import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.Database.MapToObject;
import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.Tickets.ITicket;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.TicketsListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aneurinc on 01/03/2016.
 */
public class TicketFragment extends Fragment {

    private List<ITicket> tickets = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        executeAsyncTask();
        return view;
    }

    private void executeAsyncTask() {
        ReadTickets task = new ReadTickets();
        task.execute();
    }

    private class ReadTickets extends AsyncTask<Void, Void, List<ITicket>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<ITicket> doInBackground(Void... params) {

            APIConnection connection = new APIConnection(DatabaseTable.TICKET);
            List<Map<String, String>> listOfMaps = connection.readAll();

            for (Map<String, String> currMap : listOfMaps) {
                tickets.add(MapToObject.ConvertTicket(currMap));
            }

            return tickets;
        }

        @Override
        protected void onPostExecute(List<ITicket> tickets) {
            ListView list = (ListView) getView().findViewById(R.id.my_tickets_list);
            list.setAdapter(new TicketsListAdapter(getActivity(), tickets));
        }
    }
}
