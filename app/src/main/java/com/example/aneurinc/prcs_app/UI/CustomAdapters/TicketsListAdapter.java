package com.example.aneurinc.prcs_app.UI.CustomAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.Tickets.ITicket;
import com.example.aneurinc.prcs_app.UI.Utilities.Constants;
import com.example.aneurinc.prcs_app.Utility.Validator;

import java.util.List;

/**
 * Created by aneurinc on 11/04/2016.
 */
public class TicketsListAdapter extends ArrayAdapter<String> {

    // TODO get event name, event image and venue name

    private final Activity mContext;
    private String[] cost;
    private String[] type;

    public TicketsListAdapter(Activity context, List<ITicket> tickets) {

        super(context, R.layout.list_my_tickets, Constants.eventName);
        mContext = context;
        updateTicketList(tickets);
    }

    private void updateTicketList(List<ITicket> tickets) {

        int i = 0;
        int n = tickets.size();
        cost = new String[n];
        type = new String[n];

        for (ITicket t : tickets) {
            cost[i] = t.getPrice().toString();
            type[i] = t.getType();
            i++;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            // inflate view
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_my_tickets, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.event_name);
            viewHolder.ticketCost = (TextView) convertView.findViewById(R.id.event_cost);
            viewHolder.ticketType = (TextView) convertView.findViewById(R.id.ticketType);
            viewHolder.venueName = (TextView) convertView.findViewById(R.id.event_venue);
            viewHolder.eventImage = (ImageView) convertView.findViewById(R.id.event_image);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {
            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.eventName.setText("Event Name");
        viewHolder.eventImage.setImageResource(Constants.eventImages[position]);
        viewHolder.ticketCost.setText(Validator.formatPrice(Double.parseDouble(cost[position])));
        viewHolder.ticketType.setText(type[position]);
        viewHolder.venueName.setText("Venue Name");

        return convertView;
    }

    @Override
    public int getCount() {
        return type.length;
    }

    static class ViewHolder {
        TextView eventName;
        ImageView eventImage;
        TextView ticketCost;
        TextView ticketType;
        TextView venueName;
    }
}
