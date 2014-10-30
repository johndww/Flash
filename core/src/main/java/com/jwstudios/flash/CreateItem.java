package com.jwstudios.flash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.jwstudios.flash.data.Data;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class CreateItem
        extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.createitem);

        initAds();
    }

    /**
     * Creating the item pair
     */
    public void onClick(View v) {
        // do some validating to make sure they actually filled the fields
        finish();
    }

    public void finish() {
        // if the user entered some values, then send them back to the main
        // activity
        if (((EditText) findViewById(R.id.itemName1)).getText().toString()
                .trim().equals("")
                || ((EditText) findViewById(R.id.itemName2)).getText()
                .toString().trim().equals("")) {
            setResult(RESULT_CANCELED);
        }
        else {
            Intent data = new Intent();
            data.putExtra("item1", ((EditText) findViewById(R.id.itemName1))
                    .getText().toString());
            data.putExtra("item2", ((EditText) findViewById(R.id.itemName2))
                    .getText().toString());
            setResult(RESULT_OK, data);
        }
        super.finish();
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
}
