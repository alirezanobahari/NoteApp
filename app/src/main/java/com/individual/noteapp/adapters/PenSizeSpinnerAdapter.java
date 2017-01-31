package com.individual.noteapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.individual.noteapp.R;

import java.util.List;

/**
 * Created by Blackout on 1/30/2017.
 */

public class PenSizeSpinnerAdapter extends ArrayAdapter<Integer> {

    private LayoutInflater inflater;
    private List<Integer> items;
    private ViewHolder holder = null;

    public PenSizeSpinnerAdapter(Context context, int textViewResourceId, List<Integer> items) {
        super(context, textViewResourceId, items);
        inflater = ((Activity) context).getLayoutInflater();
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.pen_size_spinner_row, parent,
                false);
        row.setBackgroundColor(Color.WHITE);
        View penSize =  row.findViewById(R.id.penSize);
        ViewGroup.LayoutParams params = penSize.getLayoutParams();
        params.height = items.get(position);
        penSize.setLayoutParams(params);

        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.pen_size_spinner_row, parent, false);
            holder.penSize = row.findViewById(R.id.penSize);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ViewGroup.LayoutParams params = holder.penSize.getLayoutParams();
        params.height = items.get(position);
        holder.penSize.setLayoutParams(params);

        return row;
    }

    static class ViewHolder {
        View penSize;
    }


}

