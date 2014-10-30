package com.jwstudios.flash.dailies;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jwstudios.flash.SibOne;
import com.jwstudios.flash.util.NotificationFactory;

import java.util.ArrayList;

/**
 * User: johnwright
 * Date: 1/21/13
 * Time: 9:37 PM
 */
public class DailySummary extends ListActivity {
    ArrayList<SibOne> dailyWords;
    DailySummaryAdapter dailySummaryAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dailyWords = DailyCoordinator.get().getDailyWords(true, getApplicationContext());

        dailySummaryAdapter = new DailySummaryAdapter(this, dailyWords);
        setListAdapter(dailySummaryAdapter);

        this.registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        NotificationFactory.standardNotification(dailyWords.get(position).getFullDescription(), "View Word", this);
    }
}
