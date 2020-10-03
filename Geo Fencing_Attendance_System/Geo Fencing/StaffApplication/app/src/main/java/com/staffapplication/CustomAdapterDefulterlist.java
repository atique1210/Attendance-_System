package com.staffapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by dell on 23/02/2018.
 */

public class CustomAdapterDefulterlist  extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] nameList;
    private final String[] perList;

    public CustomAdapterDefulterlist(Activity context, String[] nameList, String[] perList){
        super(context, R.layout.cus_defulter,nameList);
        this.context = context;
        this.nameList= nameList;
        this.perList=perList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cus_defulter, null, true);
        TextView per= (TextView) rowView.findViewById(R.id.nameList);
        TextView list= (TextView) rowView.findViewById(R.id.per);

        per.setText(perList[position]);
        list.setText(nameList[position]);

        return rowView;
    }
}