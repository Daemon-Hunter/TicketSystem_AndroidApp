package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

/**
 * Created by aneurinc on 11/03/2016.
 */
public class TicketActAdapter extends ArrayAdapter<ITicket> {

    // Reference to parent activity
    private final Activity mContext;

    // Ticket list
    private List<ITicket> mTickets;

    // Row colours
    private static final int ROW_COLOUR1 = 0x3003a9f4;
    private static final int ROW_COLOUR2 = 0x3081d4fa;

    /*
    * Initialise adapter
    */
    public TicketActAdapter(Activity context, List<ITicket> tickets) {

        super(context, R.layout.list_row_ticket_type);

        mContext = context;

        mTickets = tickets;

    }

    /*
    * Clear list
    */
    @Override
    public void clear() {
        mTickets.clear();
    }

    /*
    * Add collection to list
    */
    @Override
    public void addAll(Collection<? extends ITicket> collection) {
        mTickets.addAll(collection);
    }

    /*
    * Return view by specified postion
    */
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /*
    * Return list size
    */
    @Override
    public int getCount() {
        return mTickets.size();
    }

    /*
    * Return ticket by position
    */
    @Override
    public ITicket getItem(int position) {
        return mTickets.get(position);
    }

    /*
    * Return row alternate row colours
    */
    private int getRowColour(int position) {
        return position % 2 == 0 ? ROW_COLOUR1 : ROW_COLOUR2;
    }

    /*
    * Return row view
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        ITicket currTicket = getItem(position);

        if (convertView == null) {

            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_ticket_type, parent, false);

            // set up view holder
            viewHolder = new ViewHolder();
            viewHolder.ticketType = (TextView) convertView.findViewById(R.id.ticket_type);
            viewHolder.ticketCost = (TextView) convertView.findViewById(R.id.ticket_cost);
            viewHolder.ticketQty = (TextView) convertView.findViewById(R.id.ticket_type);
            viewHolder.plus = (ImageView) convertView.findViewById(R.id.plus);
            viewHolder.minus = (ImageView) convertView.findViewById(R.id.minus);

            // add on click listeners for plus and minus ticket qty
            setOnClickListeners(viewHolder, convertView);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {

            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();

        }

        // get text view from view holder and update value
        viewHolder.ticketType.setText(currTicket.getType());
        viewHolder.ticketCost.setText(Utilities.formatPrice(currTicket.getPrice()));


        convertView.setBackgroundColor(getRowColour(position));

        return convertView;
    }

    /*
    * Add onClick listeners for plus and minus buttons
    */
    private void setOnClickListeners(ViewHolder viewHolder, final View row) {

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.onclick));
                TextView ticketQty = (TextView) row.findViewById(R.id.ticket_qty);
                int qty = Integer.valueOf(ticketQty.getText().toString());
                ticketQty.setText(Integer.toString(++qty));
                updateTotal(row, 1);

            }
        });

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.onclick));
                TextView ticketQty = (TextView) row.findViewById(R.id.ticket_qty);
                int qty = Integer.valueOf(ticketQty.getText().toString());


                if (qty > 0) {
                    ticketQty.setText(Integer.toString(--qty));
                    updateTotal(row, -1);
                }

            }
        });

    }

    /*
    * Update total quantity and cost
    */
    private void updateTotal(View row, int val) {

        // get reference to ticket total text view
        TextView total = (TextView) mContext.findViewById(R.id.ticket_total_amount);
        String totalStr = total.getText().toString();
        totalStr = totalStr.replace("£", "");
        double currTotalVal = Double.parseDouble(totalStr);

        // get reference to ticket price in list view row
        TextView cost = (TextView) row.findViewById(R.id.ticket_cost);
        String costStr = cost.getText().toString();
        costStr = costStr.replace("£", "");
        double costVal = Double.parseDouble(costStr);

        // calculate new value of total ticket price
        double newTotalVal = currTotalVal + costVal * val;
        DecimalFormat df = new DecimalFormat("£0.00");
        total.setText(df.format(newTotalVal));

        // set checkout button to enabled
        ImageView checkout = (ImageView) mContext.findViewById(R.id.checkout);
        if (newTotalVal > 0) {
            checkout.setClickable(true);
        } else {
            checkout.setClickable(false);
        }

    }

    /*
    * Static view holder class to keep reference to row views
    */
    static class ViewHolder {
        TextView ticketType;
        TextView ticketCost;
        TextView ticketQty;
        ImageView plus;
        ImageView minus;
    }
}
