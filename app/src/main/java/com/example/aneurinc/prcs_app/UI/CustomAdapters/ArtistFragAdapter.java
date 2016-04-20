package com.example.aneurinc.prcs_app.UI.CustomAdapters;

/**
 * Created by Dominic on 14/04/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.Datamodel.Artist;
import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.Utility.Validator;

import java.util.List;

public class ArtistFragAdapter extends BaseAdapter {

    private Activity context;
    private String[] title;
    private Bitmap[] image;

    public ArtistFragAdapter(Activity c, List<Artist> artistList) {
        context = c;
        updateGridList(artistList);
    }

    public void updateGridList(List<Artist> artists) {

        title = new String[artists.size()];
        image = new Bitmap[artists.size()];
        int i = 0;

        for (Artist currArtist : artists) {
            image[i] = currArtist.getImage();
            title[i] = currArtist.getArtistName();
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
            int xy = context.findViewById(R.id.artist_grid_view).getWidth() / 3;
            // resize image to fit single grid
            viewHolder.gridImage.setImageBitmap(Validator.scaleDown(image[position], xy, xy));
        }

        viewHolder.gridText.setText(title[position]);

        return convertView;
    }

    static class ViewHolder {
        ImageView gridImage;
        TextView gridText;
    }


}