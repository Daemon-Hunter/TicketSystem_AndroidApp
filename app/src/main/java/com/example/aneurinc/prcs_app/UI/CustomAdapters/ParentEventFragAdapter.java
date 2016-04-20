package com.example.aneurinc.prcs_app.UI.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.datamodel.IParentEvent;

import java.util.List;

/**
 * Created by Dominic on 15/04/2016.
 */
public class ParentEventFragAdapter extends BaseAdapter {

    private Activity context;
    private Bitmap[] image;
    private String[] title;

    public ParentEventFragAdapter(Activity a, List<IParentEvent> eventList) {
        context = a;
        updateGridList(eventList);

    }

    public void updateGridList(List<IParentEvent> events) {

        title = new String[events.size()];
        image = new Bitmap[events.size()];
        int i = 0;

        for (IParentEvent currEvent : events) {
            title[i] = currEvent.getParentEventName();
            image[i] = currEvent.getImage(0);
            i++;
        }

    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_single, null);

            // set up view holder
            viewHolder = new ViewHolder();
            viewHolder.gridImage = (ImageView) convertView.findViewById(R.id.grid_image);
            viewHolder.gridText = (TextView) convertView.findViewById(R.id.grid_text);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        if (image[position] != null) {
            // get width of single grid
            int xy = context.findViewById(R.id.event_grid_view).getWidth() / 3;
            // resize image to fit single grid
            viewHolder.gridImage.setImageBitmap(ImageUtils.scaleDown(image[position], xy, xy));
        }

        viewHolder.gridText.setText(title[position]);

        return convertView;
    }

    static class ViewHolder {
        ImageView gridImage;
        TextView gridText;
    }

}
