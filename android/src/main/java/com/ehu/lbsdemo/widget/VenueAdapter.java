package com.ehu.lbsdemo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ehu.lbsdemo.R;
import com.ehu.lbsdemo.model.Venue;

/**
 * Copyright James Deng 2014.
 */
public class VenueAdapter extends ArrayAdapter<Venue> {
    /**
     * Constructor
     *
     * @param context The current context.
     */
    public VenueAdapter(Context context) {
        super(context, R.layout.venue);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.venue, parent, false);
        } else {
            rowView = convertView;
        }
        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        TextView distanceView = (TextView) rowView.findViewById(R.id.distance);
        TextView addressView = (TextView) rowView.findViewById(R.id.address);

        Venue v = getItem(position);
        nameView.setText(v.name);
        distanceView.setText(v.location.distance + "m");
        addressView.setText(v.location.address);
        return rowView;
    }
}