package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 11/03/2016.
 */
public class TicketListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] name;
    private final String[] cost;

    public TicketListAdapter(Activity context, String[] name, String[] cost) {

        super(context, R.layout.ticket_list, name);

        this.context = context;
        this.name = name;
        this.cost = cost;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.ticket_list, null, true);

        TextView ticketType = (TextView) rowView.findViewById(R.id.ticket_type);
        ticketType.setText(name[position]);

        TextView ticketCost = (TextView) rowView.findViewById(R.id.ticket_cost);
        ticketCost.setText(cost[position]);

        int colorPos = position % Constants.rowColour.length;
        rowView.setBackgroundColor(Constants.rowColour[colorPos]);

        return rowView;
    }
}
