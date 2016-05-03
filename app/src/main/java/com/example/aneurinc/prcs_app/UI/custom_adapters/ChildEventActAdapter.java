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
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;

import java.util.List;

/**
 * Created by aneurinc on 29/02/2016.
 */
public class ChildEventActAdapter extends ArrayAdapter<IArtist> {

    private final Activity mContext;
    private List<IArtist> mArtists;
    private static final int ROW_COLOUR1 = 0x3003a9f4;
    private static final int ROW_COLOUR2 = 0x3081d4fa;

    public ChildEventActAdapter(Activity context, List<IArtist> artistList) {
        super(context, R.layout.list_row_artist_lineup);
        mContext = context;
        mArtists = artistList;
    }

    private int getRowColour(int position) {
        return position % 2 == 0 ? ROW_COLOUR1 : ROW_COLOUR2;
    }

    @Override
    public IArtist getItem(int position) {
        return mArtists.get(position);
    }

    @Override
    public int getCount() {
        return mArtists.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IArtist mArtist = getItem(position);

        if (convertView == null) {

            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_artist_lineup, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.artistName = (TextView) convertView.findViewById(R.id.artist_name);
            viewHolder.artistImage = (ImageView) convertView.findViewById(R.id.artist_image);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.artistName.setText(mArtist.getName());

        int xy = Utilities.getScreenWidth(mContext) / 5;
        Bitmap scaledImage = Utilities.scaleDown(mArtist.getImage(0), xy, xy);
        viewHolder.artistImage.setImageBitmap(scaledImage);

        convertView.setBackgroundColor(getRowColour(position));

        return convertView;
    }


    static class ViewHolder {
        ImageView artistImage;
        TextView artistName;
    }

}
