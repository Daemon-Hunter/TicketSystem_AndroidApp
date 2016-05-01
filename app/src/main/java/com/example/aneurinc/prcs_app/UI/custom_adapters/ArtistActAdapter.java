package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;

import java.util.List;

/**
 * Created by aneurinc on 19/03/2016.
 */
public class ArtistActAdapter extends ArrayAdapter<IChildEvent> {

    private final Activity mContext;
    private List<IChildEvent> mChildEvents;
    private static final int ROW_COLOUR1 = 0x3003a9f4;
    private static final int ROW_COLOUR2 = 0x3081d4fa;

    public ArtistActAdapter(Activity context, List<IChildEvent> childEvents) {
        super(context, R.layout.list_row_artist_child_events);
        mContext = context;
        mChildEvents = childEvents;
    }

    private int getRowColour(int position) {
        return position % 2 == 0 ? ROW_COLOUR1 : ROW_COLOUR2;
    }

    @Override
    public int getCount() {
        return mChildEvents.size();
    }

    @Override
    public IChildEvent getItem(int position) {
        return mChildEvents.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IChildEvent currItem = getItem(position);

        if (convertView == null) {
            // inflate view
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_artist_child_events, parent, false);

            // set up view holder
            viewHolder = new ViewHolder();
            viewHolder.eventImage = (ImageView) convertView.findViewById(R.id.artist_image);
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.artist_child_event_name);
            viewHolder.eventDate = (TextView) convertView.findViewById(R.id.artist_child_event_date);

            // store the holder with the view
            convertView.setTag(viewHolder);
        } else {
            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // alternate colour of rows
        convertView.setBackgroundColor(getRowColour(position));

        int xy = ImageUtils.getScreenWidth(mContext) / 5;
        Bitmap scaledImage = ImageUtils.scaleDown(currItem.getImage(0), xy, xy);

        String startDate = currItem.getStartDateTime().toString();
        String endDate = currItem.getEndDateTime().toString();
        viewHolder.eventDate.setText(startDate.substring(0, 10) + " - " + endDate.substring
                (0, 10));
        viewHolder.eventImage.setImageBitmap(scaledImage);
        viewHolder.eventName.setText(currItem.getName());

        return convertView;
    }

    static class ViewHolder {
        ImageView eventImage;
        TextView eventName;
        TextView eventDate;
    }
}
