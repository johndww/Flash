package com.jwstudios.flash.search;

import java.util.List;

import com.jwstudios.flash.R;
import com.jwstudios.flash.SibOne;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author john.wright
 * @since 21
 */

public class SearchAdapter
        extends ArrayAdapter<SibOne> {

    private List<SibOne> myItems;
    private Activity context;

    public SearchAdapter(final Activity context, final int textViewResourceId,
                         final List<SibOne> sibs) {
        super(context, textViewResourceId, sibs);

        this.myItems = sibs;
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.searchwords_row, null);
        }

        final SibOne sibOne = this.myItems.get(position);

        if (sibOne != null) {
            TextView nameTextView = (TextView) view.findViewById(R.id.search_row_word);
            nameTextView.setText(sibOne.getShortDescription());
        }
        return view;
    }
}
