package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.util.List;

/**
 * Created by aneurinc on 11/04/2016.
 */
public class TicketFragAdapter extends ArrayAdapter<ITicket> {

    private final Activity mContext;
    private List<ITicket> mTickets;
    private List<IChildEvent> mChildEvents;
    private List<IVenue> mVenues;

    public TicketFragAdapter(Activity context, List<ITicket> tickets, List<IChildEvent> childEvents, List<IVenue> venues) {

        super(context, R.layout.list_row_my_ticket);

        mContext = context;
        mTickets = tickets;
        mChildEvents = childEvents;
        mVenues = venues;
    }

    @Override
    public int getCount() {
        return mTickets.size();
    }

    @Override
    public ITicket getItem(int position) {
        return mTickets.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        ITicket currTicket = getItem(position);

        if (convertView == null) {

            // inflate view
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_my_ticket, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.event_name);
            viewHolder.ticketCost = (TextView) convertView.findViewById(R.id.event_cost);
            viewHolder.ticketType = (TextView) convertView.findViewById(R.id.ticket_qty);
            viewHolder.venueName = (TextView) convertView.findViewById(R.id.event_venue);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {
            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.eventName.setText(mChildEvents.get(position).getName());
        viewHolder.ticketCost.setText(Utilities.formatPrice(Double.parseDouble(currTicket.getPrice()
                .toString())));
        viewHolder.ticketType.setText(currTicket.getType());
        viewHolder.venueName.setText(mVenues.get(position).getName());

        return convertView;
    }

    static class ViewHolder {
        TextView eventName;
        TextView ticketCost;
        TextView ticketType;
        TextView venueName;
    }
}
