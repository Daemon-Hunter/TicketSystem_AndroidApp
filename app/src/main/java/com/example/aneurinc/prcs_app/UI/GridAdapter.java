package com.example.aneurinc.prcs_app.UI;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 22/02/2016.
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private Integer[] mImages;
    private String[] mTitle;

    public GridAdapter(Context c, Integer[] i, String[] t) {
        mContext = c;
        mImages = i;
        mTitle = t;
    }

    @Override
    public int getCount() {
        return mImages.length;
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


        View grid;
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            // if it's not recycled, initialize some attributes

            grid = inflater.inflate(R.layout.grid_single, null);

            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            textView.setText(mTitle[position]);

            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
            imageView.setImageResource(mImages[position]);
            imageView.getLayoutParams().width = 275;
            imageView.getLayoutParams().height = 275;

        } else {

            grid = convertView;

        }

        return grid;
    }



}