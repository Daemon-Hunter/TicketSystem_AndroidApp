package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;

import java.util.List;

/**
 * Created by Dominic on 15/04/2016.
 */
public class ParentEventFragAdapter extends ArrayAdapter<IParentEvent> {

    private Activity mContext;
    private List<IParentEvent> mParentEvents;

    public ParentEventFragAdapter(Activity context, List<IParentEvent> parentEvents) {
        super(context, R.layout.grid_single);
        mContext = context;
        mParentEvents = parentEvents;
    }

    @Override
    public int getCount() {
        return mParentEvents.size();
    }

    @Override
    public IParentEvent getItem(int position) {
        return mParentEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IParentEvent currParentEvent = getItem(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        int xy = mContext.findViewById(R.id.event_grid_view).getWidth() / 3;

        viewHolder.gridImage.setImageBitmap(ImageUtils.scaleDown(currParentEvent.getImage(0),
                xy, xy));
        viewHolder.gridText.setText(currParentEvent.getName());

        return convertView;
    }

    static class ViewHolder {
        ImageView gridImage;
        TextView gridText;
    }

}
