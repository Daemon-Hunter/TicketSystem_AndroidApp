package com.example.aneurinc.prcs_app.UI.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.ArtistActivity;
import com.example.aneurinc.prcs_app.UI.Utilities.Constants;

/**
 * Created by aneurinc on 29/02/2016.
 */
public class ChildEventActAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

    private final Activity mContext;
    private static final int DATE_TYPE = 0;
    private static final int ARTIST_TYPE = 1;
    private static final int TYPE_MAX_COUNT = 2;
    private static final int DATE_ROW_COLOUR = 0x7029b6f6;

    public ChildEventActAdapter(Activity context) {
        super(context, R.layout.list_row_artist_lineup, Constants.artistName);

        mContext = context;

        ListView list = (ListView) context.findViewById(R.id.lineup_list);
        list.setOnItemClickListener(this);
    }

    private int getRowColour(int position) {
        return position % Constants.rowColour.length;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        // decide when to display date or lineup view for list row
        return position % 4 == 0 ? DATE_TYPE : ARTIST_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        int type = getItemViewType(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            viewHolder = new ViewHolder();

            switch (type) {

                case ARTIST_TYPE:
                    convertView = inflater.inflate(R.layout.list_row_artist_lineup, parent, false);
                    viewHolder.textView = (TextView) convertView.findViewById(R.id.item);
                    viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
                    break;

                case DATE_TYPE:
                    convertView = inflater.inflate(R.layout.list_row_artist_lineup_date, parent, false);
                    viewHolder.textView = (TextView) convertView.findViewById(R.id.performance_date);
                    viewHolder.imageView = (ImageView) convertView.findViewById(R.id.calendar);
                    break;

            }

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (type) {
            case DATE_TYPE:
                viewHolder.textView.setText("Friday 6th of March");
                viewHolder.imageView.setImageResource(R.drawable.calendar);
                convertView.setBackgroundColor(DATE_ROW_COLOUR);
                break;
            case ARTIST_TYPE:
                viewHolder.textView.setText(Constants.artistName[position]);
                viewHolder.imageView.setImageResource(Constants.artistImages[position]);
                convertView.setBackgroundColor(Constants.rowColour[getRowColour(position)]);
                break;
        }

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mContext.startActivity(new Intent(mContext, ArtistActivity.class));
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

}
