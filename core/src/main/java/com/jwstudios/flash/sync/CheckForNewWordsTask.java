package com.jwstudios.flash.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.jwstudios.flash.data.Data;
import com.jwstudios.flash.util.SibCollectionUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * User: johnwright
 * Date: 3/29/14
 * Time: 12:11 PM
 */

public class CheckForNewWordsTask extends AsyncTask<SyncParm, Integer, Long> {
    // Do the long-running work in here
    Context context;
    Button button;
    int count;

    @Override
    protected Long doInBackground(SyncParm... syncParmses) {
        try {

            if (syncParmses.length == 0) {
                return 1L;
            }
            assignParms(syncParmses[0]);

            final int maxVersion = SibCollectionUtils.getMaxVersion(context);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(Data.URL.toString() + "?do=getWordCountForSync&v=" + maxVersion);
            final String response = httpclient.execute(request, new BasicResponseHandler());

            this.count = Integer.parseInt(response);
        } catch (Exception e) {
            this.count = 0;
        }
        return 0L;
    }

    private void assignParms(SyncParm parm) {
        this.context = parm.getContext();
        this.button = parm.getButton();
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        if (this.count > 0 && this.context != null && this.button != null) {
            this.button.setText("SYNC " + this.count + " NEW WORDS");
            this.button.setVisibility(View.VISIBLE);
            this.button.setEnabled(true);
        }
    }
}