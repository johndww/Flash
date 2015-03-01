package com.jwstudios.flash.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jwstudios.flash.R;
import com.jwstudios.flash.SibOne;
import com.jwstudios.flash.ViewWord;
import com.jwstudios.flash.util.PersistanceUtils;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author john.wright
 * @since 21
 */
public class SearchableActivity
        extends ListActivity {
    private List<SibOne> matches;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String predicate = intent.getStringExtra(SearchManager.QUERY);
            this.matches = search(predicate);
        }
        else {
            this.matches = Collections.EMPTY_LIST;
        }
    }

    private List<SibOne> search(final String predicate) {
        final ArrayList<SibOne> sibs = PersistanceUtils.getSibOnesList(getApplicationContext());
        final ArrayList<SibOne> matches = new ArrayList<SibOne>();
        for (final SibOne sib : sibs) {
            //TODO this is probably really expensive. we should index this somehow? or actually learn regex...
            // actually, turns out with only 200 words, this is super cheap - no need to change yet
            final boolean hit = sib.getName().matches("(?i).*" + predicate + ".*")
                    || sib.getPair().getName().matches("(?i).*" + predicate + ".*");
            if (hit) {
                matches.add(sib);
            }
        }
        if (matches.size() == 0) {
            final Toast toast =
                    Toast.makeText(getApplicationContext(), "No word found for " + predicate, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 50, 20);
            toast.show();
            finish();
        }
        setListAdapter(new SearchAdapter(this, R.layout.searchwords_row, matches));
        return matches;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final SibOne sibOne = this.matches.get(position);
        final Intent intent = new Intent(this, ViewWord.class);
        intent.putExtra("uuid", sibOne.getUniqueId().toString());
        startActivity(intent);
    }
}
