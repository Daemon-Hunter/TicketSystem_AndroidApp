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
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.util.List;

/**
 * Created by aneurinc on 03/04/2016.
 */
public class ReceiptActAdapter extends ArrayAdapter<ITicket> {


    private final Activity context;
    private List<ITicket> mTickets;
    private List<IBooking> mBookings;

    public ReceiptActAdapter(Activity c, List<ITicket> tickets, List<IBooking> bookings) {

        super(c, R.layout.list_invoice);

        this.context = c;

        mTickets = tickets;

        mBookings = bookings;
    }

    @Override
    public ITicket getItem(int position) {
        return mTickets.get(position);
    }

    @Override
    public int getCount() {
        return mTickets.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        ITicket currTicket = getItem(position);

        if (convertView == null) {

            // inflate view
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_invoice, parent, false);

            // set up view holder
            viewHolder = new ViewHolder();
            viewHolder.ticketType = (TextView) convertView.findViewById(R.id.ticket_type);
            viewHolder.ticketCost = (TextView) convertView.findViewById(R.id.ticket_cost);
            viewHolder.ticketQty = (TextView) convertView.findViewById(R.id.ticket_qty);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {

            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();

        }

        // get text view from view holder and update value
        viewHolder.ticketType.setText(currTicket.getType());
        viewHolder.ticketCost.setText(Utilities.formatPrice(currTicket.getPrice()));
        viewHolder.ticketQty.setText(mBookings.get(position).getQuantity().toString());

        return convertView;
    }

    static class ViewHolder {
        TextView ticketType;
        TextView ticketQty;
        TextView ticketCost;
    }

}


