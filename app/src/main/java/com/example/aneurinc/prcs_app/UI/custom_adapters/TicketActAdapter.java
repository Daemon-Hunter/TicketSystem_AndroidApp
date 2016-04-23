package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.utilities.Constants;

import java.text.DecimalFormat;

/**
 * Created by aneurinc on 11/03/2016.
 */
public class TicketActAdapter extends ArrayAdapter<String> {

    private final Activity context;

    public TicketActAdapter(Activity context) {

        super(context, R.layout.list_row_ticket_type, Constants.ticketType);

        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_ticket_type, parent, false);

            // set up view holder
            viewHolder = new ViewHolder();
            viewHolder.ticketType = (TextView) convertView.findViewById(R.id.ticket_type);
            viewHolder.ticketCost = (TextView) convertView.findViewById(R.id.ticket_cost);
            viewHolder.ticketQty = (TextView) convertView.findViewById(R.id.parent_event_date);
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
        viewHolder.ticketType.setText(Constants.ticketType[position]);
        viewHolder.ticketCost.setText(Constants.ticketCost[position]);

        // invert row colour
        int colorPos = position % Constants.rowColour.length;
        convertView.setBackgroundColor(Constants.rowColour[colorPos]);

        return convertView;
    }

    private void setOnClickListeners(ViewHolder viewHolder, final View row) {

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.onclick));
                TextView ticketQty = (TextView) row.findViewById(R.id.parent_event_date);
                int qty = Integer.valueOf(ticketQty.getText().toString());
                qty++;
                ticketQty.setText(Integer.toString(qty));
                updateTotal(row, 1);

            }
        });

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.onclick));
                TextView ticketQty = (TextView) row.findViewById(R.id.parent_event_date);
                int qty = Integer.valueOf(ticketQty.getText().toString());

                if (qty > 0) {
                    qty--;
                    ticketQty.setText(Integer.toString(qty));
                    updateTotal(row, -1);
                }

            }
        });

    }

    private void updateTotal(View row, int val) {

        // get reference to ticket total text view
        TextView total = (TextView) context.findViewById(R.id.ticket_total_amount);
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
        ImageView checkout = (ImageView) context.findViewById(R.id.checkout);
        if (newTotalVal > 0) {
            checkout.setClickable(true);
        } else {
            checkout.setClickable(false);
        }

    }

    static class ViewHolder {
        TextView ticketType;
        TextView ticketCost;
        TextView ticketQty;
        ImageView plus;
        ImageView minus;
    }
}
