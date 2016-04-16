package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

import java.text.DecimalFormat;

/**
 * Created by aneurinc on 03/04/2016.
 */
public class InvoiceListAdapter extends ArrayAdapter<String> {


    private final Activity context;

    public InvoiceListAdapter(Activity c) {

        super(c, R.layout.list_invoice, Constants.ticketType);

        this.context = c;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            // inflate view
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_invoice, parent, false);

            // set up view holder
            viewHolder = new ViewHolder();
            viewHolder.ticketType = (TextView) convertView.findViewById(R.id.ticket_type);
            viewHolder.ticketCost = (TextView) convertView.findViewById(R.id.ticket_cost);
            viewHolder.ticketQty = (TextView) convertView.findViewById(R.id.ticketType);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {

            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();

        }

        // get text view from view holder and update value
        viewHolder.ticketType.setText(Constants.ticketType[position]);
        viewHolder.ticketCost.setText(Constants.ticketCost[position]);
        viewHolder.ticketQty.setText(Constants.ticketQty[position]);

        // set parent cost and parent qty values to 0 if getView is called
        if (position == 0) {
            TextView parentCost = (TextView) context.findViewById(R.id.total_cost);
            TextView parentQty = (TextView) context.findViewById(R.id.total_qty);
            parentCost.setText(R.string.zero_cost);
            parentQty.setText(R.string.zero_qty);
        }

        updateTotal(convertView);

        return convertView;
    }

    private void updateTotal(View rowView) {

        // get reference to parent total rowCost text view and convert to double
        TextView parentCost = (TextView) context.findViewById(R.id.total_cost);
        String parentCostStr = parentCost.getText().toString();
        parentCostStr = parentCostStr.replace("£", "");
        double parentTotalVal = Double.parseDouble(parentCostStr);

        // get reference to parent total row qty text view and convert to int
        TextView parentQty = (TextView) context.findViewById(R.id.total_qty);
        String parentQtyStr = parentQty.getText().toString();
        int parentQtyVal = Integer.parseInt(parentQtyStr);

        // get reference to ticket row qty text view and convert to int
        TextView rowQty = (TextView) rowView.findViewById(R.id.ticketType);
        String rowQtyStr = rowQty.getText().toString();
        int rowQtyVal = Integer.parseInt(rowQtyStr);

        // get reference to ticket row cost text view and convert to double
        TextView rowCost = (TextView) rowView.findViewById(R.id.ticket_cost);
        String rowCostStr = rowCost.getText().toString();
        rowCostStr = rowCostStr.replace("£", "");
        double rowCostVal = Double.parseDouble(rowCostStr);

        // calculate new value of total ticket price and output total rowCost
        double totalCost = parentTotalVal + rowQtyVal * rowCostVal;
        DecimalFormat df = new DecimalFormat("#0.00");
        parentCost.setText("£" + df.format(totalCost));

        // calculate new value of total ticket rowQty and output total rowQty
        int totalQty = parentQtyVal + rowQtyVal;
        parentQty.setText(String.valueOf(totalQty));

    }

    static class ViewHolder {
        TextView ticketType;
        TextView ticketQty;
        TextView ticketCost;
    }

}


