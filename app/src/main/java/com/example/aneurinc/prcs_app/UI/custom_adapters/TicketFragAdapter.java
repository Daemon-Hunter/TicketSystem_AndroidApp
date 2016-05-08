package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.util.List;

/**
 * Created by aneurinc on 11/04/2016.
 */
public class TicketFragAdapter extends ArrayAdapter<IBooking> {

    private final Activity mContext;
    private List<ITicket> mTickets;
    private List<IBooking> mBookings;
    private List<Integer> mOrderIDs;
    private List<IChildEvent> mChildEvents;
    private List<IVenue> mVenues;

    public TicketFragAdapter(Activity context, List<ITicket> tickets, List<IBooking> bookings, List<Integer> orderIDs, List<IChildEvent> childEvents, List<IVenue> venues) {

        super(context, R.layout.list_row_my_ticket);

        mContext = context;
        mOrderIDs = orderIDs;
        mTickets = tickets;
        mBookings = bookings;
        mChildEvents = childEvents;
        mVenues = venues;
    }

    @Override
    public int getCount() {
        return mBookings.size();
    }

    @Override
    public IBooking getItem(int position) {
        return mBookings.get(position);
    }

    private ITicket getTicket(int position) {
        return mTickets.get(position);
    }

    private Integer getOrderID(int position) {
        return mOrderIDs.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IBooking currBooking = getItem(position);
        ITicket currTicket = getTicket(position);
        Integer currOrderID = getOrderID(position);

        if (convertView == null) {

            // inflate view
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_my_ticket, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.event_name);
            viewHolder.ticketCost = (TextView) convertView.findViewById(R.id.event_cost);
            viewHolder.ticketQty = (TextView) convertView.findViewById(R.id.ticket_qty);
            viewHolder.ticketType = (TextView) convertView.findViewById(R.id.ticket_type);
            viewHolder.venueName = (TextView) convertView.findViewById(R.id.event_venue);
            viewHolder.bookingID = (TextView) convertView.findViewById(R.id.booking_id);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {
            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.eventName.setText(mChildEvents.get(position).getName());
        viewHolder.ticketCost.setText(Utilities.formatPrice(Double.parseDouble(currTicket.getPrice().toString())));
        viewHolder.ticketQty.setText(String.format("x%s", currBooking.getQuantity().toString()));
        viewHolder.ticketType.setText(currTicket.getType());
        viewHolder.venueName.setText(mVenues.get(position).getName());
        viewHolder.bookingID.setText(String.format(String.format("Order ID: %s", currOrderID
                .toString())));

        return convertView;
    }

    static class ViewHolder {
        TextView eventName;
        TextView ticketCost;
        TextView ticketType;
        TextView ticketQty;
        TextView venueName;
        TextView bookingID;
    }
}