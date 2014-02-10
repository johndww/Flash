package com.android.flash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.android.flash.dailies.Dailies;
import com.android.flash.dailies.DailyCoordinator;
import com.android.flash.dailies.DailySummary;
import com.android.flash.game.PlayRandom;
import com.android.flash.listwords.ListWords;
import com.android.flash.sync.ResponseCode;
import com.android.flash.sync.WordSyncer;
import com.android.flash.util.Fconstant;
import com.android.flash.util.PersistanceUtils;
import com.android.flash.util.SibCollectionUtils;

import java.io.IOException;
import java.util.Set;

public class FlashActivity extends Activity {

    /**
     * Home page for the Flash App
     */
    private static final int REQUEST_CREATE = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        initDailyButton();

    }

    private void initDailyButton() {
        final boolean finished = DailyCoordinator.get().isFinished();
        final Button button = (Button) findViewById(R.id.dailies);

        if (finished) {
            button.setBackgroundResource(R.drawable.dailies_comp);
            //button.setBackgroundColor(0xFF00FF00);
        } else {
            button.setBackgroundResource(R.drawable.dailies_incomp);
            //button.setBackgroundColor(Color.RED);
        }
    }

    /**
     * persist data if the android wants to kill us =(
     */
    protected void onPause() {
        super.onPause();
    }

    /**
     * resurrect our content (using internal storage)
     */
    protected void onResume() {
        super.onResume();
    }

    /**
     * go to the separate pages if a button is clicked LIKE A BOSS
     */
    public void onClick(View v) {

        Intent intent;
        int request_id = 0;

        switch (v.getId()) {
            case R.id.sync:
                final Set<SibOne> unSyncedItems = SibCollectionUtils.getUnSyncedWords();
                try {
                    final ResponseCode resultToSyncAdds = WordSyncer.syncNewWordsToServer(unSyncedItems);
                    final ResponseCode resultFromSyncAdds = WordSyncer.syncNewWordsFromServer();

                    Toast.makeText(getApplicationContext(), "To Server: " + resultToSyncAdds.toString() + "\nFrom Server: " + resultFromSyncAdds.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                     throw new RuntimeException("error communicating with server" + e);
                }

                break;
            case R.id.dailies:
                final boolean finished = DailyCoordinator.get().isFinished();
                if (finished) {
                    //go to daily summary
                    intent = new Intent(this, DailySummary.class);
                    startActivity(intent);
                } else {

                    //not isFinished yet, start the dailies page
                    intent = new Intent(this, Dailies.class);
                    startActivityForResult(intent, request_id);
                }
                break;
            case R.id.create_item_button:
                request_id = REQUEST_CREATE;
                intent = new Intent(this, CreateItem.class);
                startActivityForResult(intent, request_id);
                break;
            case R.id.play_button:
                intent = new Intent(this, PlayRandom.class);
                startActivity(intent);
                break;
            case R.id.list_button:
                intent = new Intent(this, ListWords.class);
                RadioGroup lang = (RadioGroup) findViewById(R.id.lang);
                RadioGroup sort = (RadioGroup) findViewById(R.id.sort);
                switch (sort.getCheckedRadioButtonId()) {
                    case R.id.alphabetical:
                        if (lang.getCheckedRadioButtonId() == R.id.english) {
                            intent.putExtra("listtype", Fconstant.LISTTYPE_ENGALPH);
                        } else {
                            intent.putExtra("listtype", Fconstant.LISTTYPE_TELALPH);
                        }
                        break;
                    case R.id.date:
                        if (lang.getCheckedRadioButtonId() == R.id.english) {
                            intent.putExtra("listtype", Fconstant.LISTTYPE_ENGDATE);
                        } else {
                            intent.putExtra("listtype", Fconstant.LISTTYPE_TELDATE);
                        }
                }
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initDailyButton();
        if (data != null) {
            if (requestCode == REQUEST_CREATE) {
                if (data.hasExtra("item1") && data.hasExtra("item2")) {
                    // process the two strings returned
                    PersistanceUtils.addPair(data.getExtras().getString("item1"), data.getExtras().getString("item2"));

                    Toast.makeText(getApplicationContext(),
                            R.string.item_created, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}