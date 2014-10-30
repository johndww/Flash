package com.jwstudios.flash.sibs;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jwstudios.flash.R;
import com.jwstudios.flash.SibOne;

public class SibTwoAdapter extends ArrayAdapter<SibOne> {

	private ArrayList<SibOne> myItems;
	private Activity context;

	public SibTwoAdapter(Activity context, int textViewResourceId,
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
			nameTextView.setText(sibOne.getPair().getName());
		}

		return view;
	}
}
