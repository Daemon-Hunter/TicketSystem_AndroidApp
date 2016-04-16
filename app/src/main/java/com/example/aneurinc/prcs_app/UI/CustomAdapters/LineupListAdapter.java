package com.example.aneurinc.prcs_app.UI.CustomAdapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
public class LineupListAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

    private final Activity context;

    public LineupListAdapter(Activity context) {

        super(context, R.layout.list_artist_lineup, Constants.artistName);

        this.context = context;

        ListView list = (ListView) context.findViewById(R.id.lineup_list);
        list.setOnItemClickListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            // inflate view
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_artist_lineup, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.artistName = (TextView) convertView.findViewById(R.id.item);
            viewHolder.artistImage = (ImageView) convertView.findViewById(R.id.image);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {

            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();

        }

        if (position % 4 == 0) {

            convertView.setBackgroundColor(Color.parseColor("#90CAF9"));
            convertView.setOnClickListener(null);

            viewHolder.artistImage.setImageResource(R.drawable.calendar);
            viewHolder.artistName.setText(Constants.nameDates[position]);
            viewHolder.artistName.setTextColor(Color.WHITE);

        } else {

            int colorPos = position % Constants.rowColour.length;
            convertView.setBackgroundColor(Constants.rowColour[colorPos]);
            viewHolder.artistImage.setImageResource(Constants.artistImages[position]);
            viewHolder.artistName.setText(Constants.artistName[position]);
            viewHolder.artistName.setTextColor(ContextCompat.getColor(context, R.color.colorGrey400));

        }

        viewHolder.artistImage.getLayoutParams().height = 120;
        viewHolder.artistImage.getLayoutParams().width = 120;

        return convertView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getContext(), ArtistActivity.class);
        i.putExtra(ArtistActivity.EventImageIndex, position);
        context.startActivity(i);
    }

    static class ViewHolder {
        TextView artistName;
        ImageView artistImage;
    }
}
