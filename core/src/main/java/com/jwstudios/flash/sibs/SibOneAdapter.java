package com.jwstudios.flash.sibs;

import java.util.ArrayList;

import com.jwstudios.flash.R;
import com.jwstudios.flash.SibOne;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SibOneAdapter
        extends ArrayAdapter<SibOne> {

    private ArrayList<SibOne> myItems;
    private Activity context;

    public SibOneAdapter(Activity context, int textViewResourceId,
                         ArrayList<SibOne> objects) {
        super(context, textViewResourceId, objects);

        this.myItems = objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.playengrow, null);
        }

        SibOne sibOne = myItems.get(position);

        if (sibOne != null) {
            TextView nameTextView = (TextView) view.findViewById(R.id.word_text_view);
            nameTextView.setText(sibOne.getName());
        }

        return view;
    }
}
