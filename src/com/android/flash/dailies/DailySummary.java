package com.android.flash.dailies;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.android.flash.R;
import com.android.flash.SibOne;
import com.android.flash.sibs.SibOneAdapter;

import java.util.ArrayList;

/**
 * User: johnwright
 * Date: 1/21/13
 * Time: 9:37 PM
 */
public class DailySummary extends ListActivity {
    ArrayList<SibOne> dailyWords;
    DailySummaryAdapter dailySummaryAdapter;
    //int position;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //position = getIntent().getExtras().getInt("position");
        dailyWords = DailyCoordinator.get().getDailyWords(true);

        dailySummaryAdapter = new DailySummaryAdapter(this, dailyWords);
        setListAdapter(dailySummaryAdapter);

        this.registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //String sibTwoName = myItems.get(position).getPair().getName();

        String sibTwoName = dailyWords.get(position).getPair().getName();
        Toast.makeText(getApplicationContext(), sibTwoName, Toast.LENGTH_SHORT)
                .show();
    }
}
