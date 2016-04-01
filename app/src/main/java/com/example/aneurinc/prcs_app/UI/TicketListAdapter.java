package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

import java.text.DecimalFormat;

/**
 * Created by aneurinc on 11/03/2016.
 */
public class TicketListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] name;
    private final String[] cost;

    public TicketListAdapter(Activity context, String[] name, String[] cost) {

        super(context, R.layout.list_ticket_type, name);

        this.context = context;
        this.name = name;
        this.cost = cost;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_ticket_type, null, true);

        setOnClickListeners(rowView);

        TextView ticketType = (TextView) rowView.findViewById(R.id.ticket_type);
        ticketType.setText(name[position]);

        TextView ticketCost = (TextView) rowView.findViewById(R.id.ticket_cost);
        ticketCost.setText(cost[position]);

        int colorPos = position % Constants.rowColour.length;
        rowView.setBackgroundColor(Constants.rowColour[colorPos]);

        return rowView;
    }

    private void updateTotal(View row, int val) {

        // get reference to ticket total text view
        TextView total = (TextView) context.findViewById(R.id.ticket_total_amount);
        // convert to string
        String totalStr = total.getText().toString();
        // remove '£' symbol
        totalStr = totalStr.replace("£", "");
        // convert to double
        double currTotalVal = Double.parseDouble(totalStr);

        // get reference to ticket price in list view row
        TextView cost = (TextView) row.findViewById(R.id.ticket_cost);
        // convert to string
        String costStr = cost.getText().toString();
        // replace '£' symbol
        costStr = costStr.replace("£", "");
        // convert to double
        double costVal = Double.parseDouble(costStr);

        double newTotalVal;
        // calculate new value of total ticket price
        newTotalVal = currTotalVal + costVal * val;
        // define format
        DecimalFormat df = new DecimalFormat("#0.00");
        // output new value
        total.setText("£" + df.format(newTotalVal));

        // set checkout button to enabled
        ImageView checkout = (ImageView) context.findViewById(R.id.checkout);
        if (newTotalVal > 0) {
            checkout.setClickable(true);
        } else {
            checkout.setClickable(false);
        }

    }

    private void setOnClickListeners(final View rowView) {

        ImageView plus = (ImageView) rowView.findViewById(R.id.plus);
        ImageView minus = (ImageView) rowView.findViewById(R.id.minus);
        final TextView tv = (TextView) rowView.findViewById(R.id.qty);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView plus = (ImageView) rowView.findViewById(R.id.plus);
                plus.startAnimation(AnimationUtils.loadAnimation(context, R.anim.onclick));
                int qty = Integer.parseInt(tv.getText().toString());
                qty++;
                tv.setText(Integer.toString(qty));
                updateTotal(rowView, 1);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView minus = (ImageView) rowView.findViewById(R.id.minus);
                minus.startAnimation(AnimationUtils.loadAnimation(context, R.anim.onclick));
                int qty = Integer.parseInt(tv.getText().toString());

                if (qty > 0) {
                    qty--;
                    tv.setText(Integer.toString(qty));
                    updateTotal(rowView, -1);
                }
            }
        });

    }
}
