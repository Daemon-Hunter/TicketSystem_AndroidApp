package com.example.aneurinc.prcs_app.UI.CustomAdapters;

import android.app.Activity;
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
import com.example.aneurinc.prcs_app.UI.Activities.ParentEventActivity;
import com.example.aneurinc.prcs_app.UI.Utilities.Constants;

/**
 * Created by aneurinc on 19/03/2016.
 */
public class UpcomingListAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

    private final Activity mContext;

    public UpcomingListAdapter(Activity context) {

        super(context, R.layout.list_row_upcoming, Constants.eventName);

        mContext = context;

        ListView list = (ListView) context.findViewById(R.id.upcoming_list);
        list.setOnItemClickListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            // inflate view
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_upcoming, parent, false);

            // set up view holder
            viewHolder = new ViewHolder();
            viewHolder.eventImage = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.eventDate = (TextView) convertView.findViewById(R.id.date);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {

            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();

        }

        // alternate colour of rows
        int colorPos = position % Constants.rowColour.length;
        convertView.setBackgroundColor(Constants.rowColour[colorPos]);

        // get view from view holder and update value
        viewHolder.eventImage.setImageResource(Constants.eventImages[position]);
        viewHolder.eventName.setText(Constants.eventName[position]);
        viewHolder.eventDate.setText(Constants.dates[position]);

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getContext(), ParentEventActivity.class);
        i.putExtra(ParentEventActivity.EventImageIndex, position);
        mContext.startActivity(i);
    }

    static class ViewHolder {
        ImageView eventImage;
        TextView eventName;
        TextView eventDate;
    }
}
