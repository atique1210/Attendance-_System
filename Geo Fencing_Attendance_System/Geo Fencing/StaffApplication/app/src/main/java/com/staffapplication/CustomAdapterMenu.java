package com.staffapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dell on 07/02/2018.
 */

public class CustomAdapterMenu extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] mnuList;
    private final int[] icons;

    public CustomAdapterMenu(Activity context, String[] mnuList, int[] icons){
        super(context, R.layout.menu_cust,mnuList);
        this.context = context;
        this.mnuList= mnuList;
        this.icons=icons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.menu_cust, null, true);
        ImageView icon= (ImageView) rowView.findViewById(R.id.imgView);
        TextView list= (TextView) rowView.findViewById(R.id.menues);

        icon.setImageResource(icons[position]);
        list.setText(mnuList[position]);

        return rowView;
    }
}
