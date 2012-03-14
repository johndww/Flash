package com.android.flash.sibs;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.flash.R;
import com.android.flash.SibOne;

public class SibOneAdapter extends ArrayAdapter<SibOne> {

	private ArrayList<SibOne> myItems;
	private Activity context;
	
	private static String[] alphabet = { "a", "b", "c", "d", "e", "f", "g",
		"h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
		"u", "v", "w", "x", "y", "z" };

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

	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object[] getSections() {
		/**String sections[] = new String[26];
		for (int i=0; i<sections.length; i++) {
			sections[i] = alphabet[i];
		}*/
		return alphabet;
	}
}
