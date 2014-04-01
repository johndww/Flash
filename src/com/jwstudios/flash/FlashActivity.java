package com.jwstudios.flash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import com.jwstudios.flash.dailies.Dailies;
import com.jwstudios.flash.dailies.DailyCoordinator;
import com.jwstudios.flash.dailies.DailySummary;
import com.jwstudios.flash.data.Data;
import com.jwstudios.flash.game.PlayRandom;
import com.jwstudios.flash.listwords.ListWords;
import com.jwstudios.flash.sync.*;
import com.jwstudios.flash.util.Fconstant;
import com.jwstudios.flash.util.PersistanceUtils;
import com.jwstudios.flash.util.SibCollectionUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.Set;

public class FlashActivity extends Activity {

    /**
     * Home page for the Flash App
     */
    private static final int REQUEST_CREATE = 10;

    public boolean admin = false;
    public int adminTryCount = 0;
    public int adminFailedAttempts = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        initDailyButton();

        initAds();
    }

    private void initAds() {
        AdView mAdView = new AdView(this);
        mAdView.setAdUnitId(Data.ADMOBID.toString());
        mAdView.setAdSize(AdSize.BANNER);
        //mAdView.setAdListener(new ToastAdListener(this));
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.ad);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mAdView, params);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    private void initDailyButton() {
        final boolean finished = DailyCoordinator.get().isFinished(getApplicationContext());
        final Button button = (Button) findViewById(R.id.dailies);

        if (finished) {
            button.setBackgroundResource(R.drawable.dailies_comp);
        } else {
            button.setBackgroundResource(R.drawable.dailies_incomp);
        }
    }

    /**
     * persist data if the android wants to kill us =(
     */
    protected void onPause() {
        super.onPause();
        adminTryCount = 0;
    }

    /**
     * resurrect our content (using internal storage)
     */
    protected void onResume() {
        super.onResume();
        adminTryCount = 0;
    }

    /**
     * go to the separate pages if a button is clicked LIKE A BOSS
     */
    public void onClick(View v) {

        Intent intent;
        int request_id = 0;

        final Context context = getApplicationContext();
        switch (v.getId()) {
            case R.id.sync_admin:
                if (admin) {
                    final Set<SibOne> unSyncedItems = SibCollectionUtils.getUnSyncedWords(context);

                    final SyncParm parm = new SyncParm(unSyncedItems, context);
                    new SyncNewWordsToServerTask().execute(parm);
                    new SyncNewWordsFromServerTask().execute(parm);
                }
                break;
            case R.id.sync:
                new SyncNewWordPacksFromServerTask().execute(new SyncParm(context));

                break;
            case R.id.admin:
                if (!admin) {
                    secretAdminAttempt();
                }
                break;
            case R.id.dailies:
                final boolean finished = DailyCoordinator.get().isFinished(context);
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

    private void secretAdminAttempt() {
        if (adminFailedAttempts > 4) {
            return;
        }
        if (!admin && adminTryCount < 5) {
            // they found the hidden button, trying to get admin, incr
            this.adminTryCount++;
        }
        else if (!admin && adminTryCount >= 5) {
            adminLogin();
            // reset try count regardless of correct/incorrect
            this.adminTryCount = 0;
        }
    }

    private boolean adminLogin() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("-");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String password = input.getText().toString();
                if (verifyPin(password)) {
                    loginSuccess();
                }
                else {
                    loginFailure();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                loginFailure();
            }
        });

        alert.create().show();
        return false;
    }

    private void loginSuccess() {
        this.admin = true;
        this.adminFailedAttempts = 0;
        final Button sync = (Button) findViewById(R.id.sync_admin);
        sync.setVisibility(View.VISIBLE);
    }

    private void loginFailure() {
        this.adminFailedAttempts++;
    }

    private boolean verifyPin(final String password) {
        return (Data.PIN.toString().equals(password));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initDailyButton();
        if (data != null) {
            if (requestCode == REQUEST_CREATE) {
                if (data.hasExtra("item1") && data.hasExtra("item2")) {
                    // process the two strings returned
                    PersistanceUtils.addPair(data.getExtras().getString("item1"), data.getExtras().getString("item2"), getApplicationContext());

                    Toast.makeText(getApplicationContext(),
                            R.string.item_created, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}